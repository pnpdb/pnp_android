
   #include <math.h>
   #include <string.h>

   #include "iLBC_define.h"
   #include "gainquant.h"
   #include "createCB.h"
   #include "filter.h"
   #include "constants.h"


   void iCBSearch(
       iLBC_Enc_Inst_t *iLBCenc_inst,

       int *index,
       int *gain_index,





       float *intarget,
       float *mem,
       int lMem,
       int lTarget,
       int nStages,
       float *weightDenum,
       float *weightState,
       int block
   ){
       int i, j, icount, stage, best_index, range, counter;
       float max_measure, gain, measure, crossDot, ftmp;
       float gains[CB_NSTAGES];
       float target[SUBL];
       int base_index, sInd, eInd, base_size;
       int sIndAug=0, eIndAug=0;
       float buf[CB_MEML+SUBL+2*LPC_FILTERORDER];
       float invenergy[CB_EXPAND*128], energy[CB_EXPAND*128];
       float *pp, *ppi=0, *ppo=0, *ppe=0;
       float cbvectors[CB_MEML];
       float tene, cene, cvec[SUBL];
       float aug_vec[SUBL];

       memset(cvec,0,SUBL*sizeof(float));

       base_size=lMem-lTarget+1;

       if (lTarget==SUBL) {
           base_size=lMem-lTarget+1+lTarget/2;
       }

       memcpy(buf,weightState,sizeof(float)*LPC_FILTERORDER);
       memcpy(buf+LPC_FILTERORDER,mem,lMem*sizeof(float));
       memcpy(buf+LPC_FILTERORDER+lMem,intarget,lTarget*sizeof(float));

       AllPoleFilter(buf+LPC_FILTERORDER, weightDenum,
           lMem+lTarget, LPC_FILTERORDER);

       memcpy(target, buf+LPC_FILTERORDER+lMem, lTarget*sizeof(float));

       tene=0.0;

       for (i=0; i<lTarget; i++) {
           tene+=target[i]*target[i];
       }

       filteredCBvecs(cbvectors, buf+LPC_FILTERORDER, lMem);

       for (stage=0; stage<nStages; stage++) {

           range = search_rangeTbl[block][stage];

           max_measure = (float)-10000000.0;
           gain = (float)0.0;
           best_index = 0;

           crossDot=0.0;
           pp=buf+LPC_FILTERORDER+lMem-lTarget;
           for (j=0; j<lTarget; j++) {
               crossDot += target[j]*(*pp++);
           }

           if (stage==0) {

               ppe = energy;
               ppi = buf+LPC_FILTERORDER+lMem-lTarget-1;
               ppo = buf+LPC_FILTERORDER+lMem-1;

               *ppe=0.0;
               pp=buf+LPC_FILTERORDER+lMem-lTarget;
               for (j=0; j<lTarget; j++) {
                   *ppe+=(*pp)*(*pp); pp++;
               }

               if (*ppe>0.0) {
                   invenergy[0] = (float) 1.0 / (*ppe + EPS);
               } else {
                   invenergy[0] = (float) 0.0;

               }
               ppe++;

               measure=(float)-10000000.0;

               if (crossDot > 0.0) {
                      measure = crossDot*crossDot*invenergy[0];
               }
           }
           else {
               measure = crossDot*crossDot*invenergy[0];
           }

           ftmp = crossDot*invenergy[0];

           if ((measure>max_measure) && (fabs(ftmp)<CB_MAXGAIN)) {
               best_index = 0;
               max_measure = measure;
               gain = ftmp;
           }

           for (icount=1; icount<range; icount++) {

               crossDot=0.0;
               pp = buf+LPC_FILTERORDER+lMem-lTarget-icount;

               for (j=0; j<lTarget; j++) {
                   crossDot += target[j]*(*pp++);
               }

               if (stage==0) {
                   *ppe++ = energy[icount-1] + (*ppi)*(*ppi) -
                       (*ppo)*(*ppo);
                   ppo--;
                   ppi--;

                   if (energy[icount]>0.0) {
                       invenergy[icount] =
                           (float)1.0/(energy[icount]+EPS);
                   } else {
                       invenergy[icount] = (float) 0.0;
                   }

                   measure=(float)-10000000.0;

                   if (crossDot > 0.0) {
                       measure = crossDot*crossDot*invenergy[icount];
                   }
               }
               else {
                   measure = crossDot*crossDot*invenergy[icount];
               }

               ftmp = crossDot*invenergy[icount];

               if ((measure>max_measure) && (fabs(ftmp)<CB_MAXGAIN)) {
                   best_index = icount;
                   max_measure = measure;
                   gain = ftmp;
               }
           }

           if (lTarget==SUBL) {
               searchAugmentedCB(20, 39, stage, base_size-lTarget/2,
                   target, buf+LPC_FILTERORDER+lMem,
                   &max_measure, &best_index, &gain, energy,
                   invenergy);
           }

           base_index=best_index;

           if (CB_RESRANGE == -1) {
               sInd=0;
               eInd=range-1;
               sIndAug=20;
               eIndAug=39;
           }

           else {
               sIndAug=0;
               eIndAug=0;
               sInd=base_index-CB_RESRANGE/2;
               eInd=sInd+CB_RESRANGE;

               if (lTarget==SUBL) {

                   if (sInd<0) {

                       sIndAug = 40 + sInd;
                       eIndAug = 39;
                       sInd=0;

                   } else if ( base_index < (base_size-20) ) {

                       if (eInd > range) {
                           sInd -= (eInd-range);
                           eInd = range;
                       }
                   } else {

                       if (sInd < (base_size-20)) {
                           sIndAug = 20;
                           sInd = 0;
                           eInd = 0;
                           eIndAug = 19 + CB_RESRANGE;

                           if(eIndAug > 39) {
                               eInd = eIndAug-39;
                               eIndAug = 39;
                           }
                       } else {
                           sIndAug = 20 + sInd - (base_size-20);
                           eIndAug = 39;
                           sInd = 0;
                           eInd = CB_RESRANGE - (eIndAug-sIndAug+1);
                       }
                   }

               } else {

                   if (sInd < 0) {
                       eInd -= sInd;

                       sInd = 0;
                   }

                   if(eInd > range) {
                       sInd -= (eInd - range);
                       eInd = range;
                   }
               }
           }

           counter = sInd;
           sInd += base_size;
           eInd += base_size;


           if (stage==0) {
               ppe = energy+base_size;
               *ppe=0.0;

               pp=cbvectors+lMem-lTarget;
               for (j=0; j<lTarget; j++) {
                   *ppe+=(*pp)*(*pp); pp++;
               }

               ppi = cbvectors + lMem - 1 - lTarget;
               ppo = cbvectors + lMem - 1;

               for (j=0; j<(range-1); j++) {
                   *(ppe+1) = *ppe + (*ppi)*(*ppi) - (*ppo)*(*ppo);
                   ppo--;
                   ppi--;
                   ppe++;
               }
           }

           for (icount=sInd; icount<eInd; icount++) {

               crossDot=0.0;
               pp=cbvectors + lMem - (counter++) - lTarget;

               for (j=0;j<lTarget;j++) {

                   crossDot += target[j]*(*pp++);
               }

               if (energy[icount]>0.0) {
                   invenergy[icount] =(float)1.0/(energy[icount]+EPS);
               } else {
                   invenergy[icount] =(float)0.0;
               }

               if (stage==0) {

                   measure=(float)-10000000.0;

                   if (crossDot > 0.0) {
                       measure = crossDot*crossDot*
                           invenergy[icount];
                   }
               }
               else {
                   measure = crossDot*crossDot*invenergy[icount];
               }

               ftmp = crossDot*invenergy[icount];

               if ((measure>max_measure) && (fabs(ftmp)<CB_MAXGAIN)) {
                   best_index = icount;
                   max_measure = measure;
                   gain = ftmp;
               }
           }

           if ((lTarget==SUBL)&&(sIndAug!=0)) {
               searchAugmentedCB(sIndAug, eIndAug, stage,
                   2*base_size-20, target, cbvectors+lMem,
                   &max_measure, &best_index, &gain, energy,
                   invenergy);
           }

           index[stage] = best_index;

           if (stage==0){

               if (gain<0.0){
                   gain = 0.0;
               }

               if (gain>CB_MAXGAIN) {
                   gain = (float)CB_MAXGAIN;
               }
               gain = gainquant(gain, 1.0, 32, &gain_index[stage]);
           }
           else {
               if (stage==1) {
                   gain = gainquant(gain, (float)fabs(gains[stage-1]),
                       16, &gain_index[stage]);
               } else {
                   gain = gainquant(gain, (float)fabs(gains[stage-1]),
                       8, &gain_index[stage]);
               }
           }

           if (lTarget==(STATE_LEN-iLBCenc_inst->state_short_len)) {

               if (index[stage]<base_size) {
                   pp=buf+LPC_FILTERORDER+lMem-lTarget-index[stage];
               } else {
                   pp=cbvectors+lMem-lTarget-
                       index[stage]+base_size;
               }
           } else {

               if (index[stage]<base_size) {
                   if (index[stage]<(base_size-20)) {
                       pp=buf+LPC_FILTERORDER+lMem-
                           lTarget-index[stage];
                   } else {
                       createAugmentedVec(index[stage]-base_size+40,
                               buf+LPC_FILTERORDER+lMem,aug_vec);
                       pp=aug_vec;
                   }
               } else {
                   int filterno, position;

                   filterno=index[stage]/base_size;
                   position=index[stage]-filterno*base_size;

                   if (position<(base_size-20)) {
                       pp=cbvectors+filterno*lMem-lTarget-
                           index[stage]+filterno*base_size;
                   } else {
                       createAugmentedVec(
                           index[stage]-(filterno+1)*base_size+40,
                           cbvectors+filterno*lMem,aug_vec);
                       pp=aug_vec;
                   }
               }
           }

           for (j=0;j<lTarget;j++) {
               cvec[j] += gain*(*pp);
               target[j] -= gain*(*pp++);
           }

           gains[stage]=gain;

       }

       cene=0.0;
       for (i=0; i<lTarget; i++) {
           cene+=cvec[i]*cvec[i];
       }
       j=gain_index[0];

       for (i=gain_index[0]; i<32; i++) {
           ftmp=cene*gain_sq5Tbl[i]*gain_sq5Tbl[i];

           if ((ftmp<(tene*gains[0]*gains[0])) &&
               (gain_sq5Tbl[j]<(2.0*gains[0]))) {
               j=i;
           }
       }
       gain_index[0]=j;
   }
