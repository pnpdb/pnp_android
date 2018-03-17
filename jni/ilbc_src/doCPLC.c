   #include <math.h>
   #include <string.h>
   #include <stdio.h>

   #include "iLBC_define.h"

   void compCorr(
       float *cc,
       float *gc,
       float *pm,
       float *buffer,
       int lag,
       int bLen,
       int sRange
   ){
       int i;
       float ftmp1, ftmp2, ftmp3;

       if ((bLen-sRange-lag)<0) {
           sRange=bLen-lag;
       }

       ftmp1 = 0.0;
       ftmp2 = 0.0;
       ftmp3 = 0.0;
       for (i=0; i<sRange; i++) {
           ftmp1 += buffer[bLen-sRange+i] *
               buffer[bLen-sRange+i-lag];
           ftmp2 += buffer[bLen-sRange+i-lag] *
                   buffer[bLen-sRange+i-lag];
           ftmp3 += buffer[bLen-sRange+i] *
                   buffer[bLen-sRange+i];
       }

       if (ftmp2 > 0.0) {
           *cc = ftmp1*ftmp1/ftmp2;
           *gc = (float)fabs(ftmp1/ftmp2);
           *pm=(float)fabs(ftmp1)/
               ((float)sqrt(ftmp2)*(float)sqrt(ftmp3));
       }
       else {
           *cc = 0.0;
           *gc = 0.0;
           *pm=0.0;
       }
   }


   void doThePLC(
       float *PLCresidual,
       float *PLClpc,
       int PLI,
       float *decresidual,
       float *lpc,
       int inlag,
       iLBC_Dec_Inst_t *iLBCdec_inst
   ){
       int lag=20, randlag;
       float gain, maxcc;
       float use_gain;
       float gain_comp, maxcc_comp, per, max_per;
       int i, pick, use_lag;
       float ftmp, randvec[BLOCKL_MAX], pitchfact, energy;

       if (PLI == 1) {

           iLBCdec_inst->consPLICount += 1;

           if (iLBCdec_inst->prevPLI != 1) {

               lag=inlag-3;
               compCorr(&maxcc, &gain, &max_per,
                   iLBCdec_inst->prevResidual,
                   lag, iLBCdec_inst->blockl, 60);
               for (i=inlag-2;i<=inlag+3;i++) {
                   compCorr(&maxcc_comp, &gain_comp, &per,
                       iLBCdec_inst->prevResidual,
                       i, iLBCdec_inst->blockl, 60);

                   if (maxcc_comp>maxcc) {
                       maxcc=maxcc_comp;

                       gain=gain_comp;
                       lag=i;
                       max_per=per;
                   }
               }

           }

           else {
               lag=iLBCdec_inst->prevLag;
               max_per=iLBCdec_inst->per;
           }

           use_gain=1.0;
           if (iLBCdec_inst->consPLICount*iLBCdec_inst->blockl>320)
               use_gain=(float)0.9;
           else if (iLBCdec_inst->consPLICount*
                           iLBCdec_inst->blockl>2*320)
               use_gain=(float)0.7;
           else if (iLBCdec_inst->consPLICount*
                           iLBCdec_inst->blockl>3*320)
               use_gain=(float)0.5;
           else if (iLBCdec_inst->consPLICount*
                           iLBCdec_inst->blockl>4*320)
               use_gain=(float)0.0;

           ftmp=(float)sqrt(max_per);
           if (ftmp>(float)0.7)
               pitchfact=(float)1.0;
           else if (ftmp>(float)0.4)
               pitchfact=(ftmp-(float)0.4)/((float)0.7-(float)0.4);
           else
               pitchfact=0.0;

           use_lag=lag;
           if (lag<80) {
               use_lag=2*lag;
           }

           energy = 0.0;
           for (i=0; i<iLBCdec_inst->blockl; i++) {

               iLBCdec_inst->seed=(iLBCdec_inst->seed*69069L+1) &
                   (0x80000000L-1);
               randlag = 50 + ((signed long) iLBCdec_inst->seed)%70;
               pick = i - randlag;

               if (pick < 0) {
                   randvec[i] =
                       iLBCdec_inst->prevResidual[
                                   iLBCdec_inst->blockl+pick];
               } else {
                   randvec[i] =  randvec[pick];
               }

               pick = i - use_lag;

               if (pick < 0) {
                   PLCresidual[i] =
                       iLBCdec_inst->prevResidual[
                                   iLBCdec_inst->blockl+pick];
               } else {
                   PLCresidual[i] = PLCresidual[pick];
               }


               if (i<80)
                   PLCresidual[i] = use_gain*(pitchfact *
                               PLCresidual[i] +
                               ((float)1.0 - pitchfact) * randvec[i]);
               else if (i<160)
                   PLCresidual[i] = (float)0.95*use_gain*(pitchfact *
                               PLCresidual[i] +
                               ((float)1.0 - pitchfact) * randvec[i]);
               else
                   PLCresidual[i] = (float)0.9*use_gain*(pitchfact *
                               PLCresidual[i] +
                               ((float)1.0 - pitchfact) * randvec[i]);

               energy += PLCresidual[i] * PLCresidual[i];
           }

           if (sqrt(energy/(float)iLBCdec_inst->blockl) < 30.0) {
               gain=0.0;
               for (i=0; i<iLBCdec_inst->blockl; i++) {
                   PLCresidual[i] = randvec[i];
               }
           }

           memcpy(PLClpc,iLBCdec_inst->prevLpc,
               (LPC_FILTERORDER+1)*sizeof(float));

       }

       else {
           memcpy(PLCresidual, decresidual,
               iLBCdec_inst->blockl*sizeof(float));
           memcpy(PLClpc, lpc, (LPC_FILTERORDER+1)*sizeof(float));
           iLBCdec_inst->consPLICount = 0;
       }

       if (PLI) {
           iLBCdec_inst->prevLag = lag;
           iLBCdec_inst->per=max_per;
       }

       iLBCdec_inst->prevPLI = PLI;
       memcpy(iLBCdec_inst->prevLpc, PLClpc,
           (LPC_FILTERORDER+1)*sizeof(float));
       memcpy(iLBCdec_inst->prevResidual, PLCresidual,
           iLBCdec_inst->blockl*sizeof(float));
   }

