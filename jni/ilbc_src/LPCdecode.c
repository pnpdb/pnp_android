
   #include <math.h>
   #include <string.h>

   #include "helpfun.h"
   #include "lsf.h"
   #include "iLBC_define.h"
   #include "constants.h"


   void LSFinterpolate2a_dec(
       float *a,
       float *lsf1,
       float *lsf2,
       float coef,
       int length
   ){
       float  lsftmp[LPC_FILTERORDER];

       interpolate(lsftmp, lsf1, lsf2, coef, length);
       lsf2a(a, lsftmp);
   }

   void SimplelsfDEQ(
       float *lsfdeq,
       int *index,
       int lpc_n
   ){
       int i, j, pos, cb_pos;

       pos = 0;
       cb_pos = 0;
       for (i = 0; i < LSF_NSPLIT; i++) {
           for (j = 0; j < dim_lsfCbTbl[i]; j++) {
               lsfdeq[pos + j] = lsfCbTbl[cb_pos +
                   (long)(index[i])*dim_lsfCbTbl[i] + j];
           }
           pos += dim_lsfCbTbl[i];
           cb_pos += size_lsfCbTbl[i]*dim_lsfCbTbl[i];
       }

       if (lpc_n>1) {

           pos = 0;
           cb_pos = 0;
           for (i = 0; i < LSF_NSPLIT; i++) {
               for (j = 0; j < dim_lsfCbTbl[i]; j++) {
                   lsfdeq[LPC_FILTERORDER + pos + j] =
                       lsfCbTbl[cb_pos +
                       (long)(index[LSF_NSPLIT + i])*
                       dim_lsfCbTbl[i] + j];
               }
               pos += dim_lsfCbTbl[i];
               cb_pos += size_lsfCbTbl[i]*dim_lsfCbTbl[i];
           }
       }
   }

   void DecoderInterpolateLSF(
       float *syntdenum,
       float *weightdenum,
       float *lsfdeq,
       int length,
       iLBC_Dec_Inst_t *iLBCdec_inst
   ){
       int    i, pos, lp_length;
       float  lp[LPC_FILTERORDER + 1], *lsfdeq2;

       lsfdeq2 = lsfdeq + length;
       lp_length = length + 1;

       if (iLBCdec_inst->mode==30) {

           LSFinterpolate2a_dec(lp, iLBCdec_inst->lsfdeqold, lsfdeq,
               lsf_weightTbl_30ms[0], length);
           memcpy(syntdenum,lp,lp_length*sizeof(float));
           bwexpand(weightdenum, lp, LPC_CHIRP_WEIGHTDENUM,
               lp_length);

           pos = lp_length;
           for (i = 1; i < 6; i++) {
               LSFinterpolate2a_dec(lp, lsfdeq, lsfdeq2,
                   lsf_weightTbl_30ms[i], length);
               memcpy(syntdenum + pos,lp,lp_length*sizeof(float));
               bwexpand(weightdenum + pos, lp,
                   LPC_CHIRP_WEIGHTDENUM, lp_length);
               pos += lp_length;
           }
       }
       else {
           pos = 0;
           for (i = 0; i < iLBCdec_inst->nsub; i++) {
               LSFinterpolate2a_dec(lp, iLBCdec_inst->lsfdeqold,
                   lsfdeq, lsf_weightTbl_20ms[i], length);
               memcpy(syntdenum+pos,lp,lp_length*sizeof(float));
               bwexpand(weightdenum+pos, lp, LPC_CHIRP_WEIGHTDENUM,
                   lp_length);
               pos += lp_length;
           }
       }

       if (iLBCdec_inst->mode==30)
           memcpy(iLBCdec_inst->lsfdeqold, lsfdeq2,
                       length*sizeof(float));
       else
           memcpy(iLBCdec_inst->lsfdeqold, lsfdeq,
                       length*sizeof(float));

   }
