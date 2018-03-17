
   #include <math.h>
   #include <string.h>

   #include "iLBC_define.h"
   #include "constants.h"
   #include "filter.h"
   #include "helpfun.h"

   void AbsQuantW(
       iLBC_Enc_Inst_t *iLBCenc_inst,

       float *in,
       float *syntDenum,
       float *weightDenum,
       int *out,
       int len,
       int state_first
   ){
       float *syntOut;
       float syntOutBuf[LPC_FILTERORDER+STATE_SHORT_LEN_30MS];
       float toQ, xq;
       int n;
       int index;

       memset(syntOutBuf, 0, LPC_FILTERORDER*sizeof(float));

       syntOut = &syntOutBuf[LPC_FILTERORDER];

       if (state_first) {
           AllPoleFilter (in, weightDenum, SUBL, LPC_FILTERORDER);
       } else {
           AllPoleFilter (in, weightDenum,
               iLBCenc_inst->state_short_len-SUBL,
               LPC_FILTERORDER);
       }


       for (n=0; n<len; n++) {

           if ((state_first)&&(n==SUBL)){
               syntDenum += (LPC_FILTERORDER+1);
               weightDenum += (LPC_FILTERORDER+1);

               AllPoleFilter (&in[n], weightDenum, len-n,
                   LPC_FILTERORDER);

           } else if ((state_first==0)&&
               (n==(iLBCenc_inst->state_short_len-SUBL))) {
               syntDenum += (LPC_FILTERORDER+1);
               weightDenum += (LPC_FILTERORDER+1);

               AllPoleFilter (&in[n], weightDenum, len-n,
                   LPC_FILTERORDER);

           }

           syntOut[n] = 0.0;
           AllPoleFilter (&syntOut[n], weightDenum, 1,
               LPC_FILTERORDER);

           toQ = in[n]-syntOut[n];

           sort_sq(&xq, &index, toQ, state_sq3Tbl, 8);
           out[n]=index;
           syntOut[n] = state_sq3Tbl[out[n]];

           AllPoleFilter(&syntOut[n], weightDenum, 1,
               LPC_FILTERORDER);
       }
   }

   void StateSearchW(
       iLBC_Enc_Inst_t *iLBCenc_inst,

       float *residual,
       float *syntDenum,
       float *weightDenum,
       int *idxForMax,
       int *idxVec,
       int len,
       int state_first
   ){
       float dtmp, maxVal;
       float tmpbuf[LPC_FILTERORDER+2*STATE_SHORT_LEN_30MS];
       float *tmp, numerator[1+LPC_FILTERORDER];
       float foutbuf[LPC_FILTERORDER+2*STATE_SHORT_LEN_30MS], *fout;
       int k;
       float qmax, scal;

       memset(tmpbuf, 0, LPC_FILTERORDER*sizeof(float));
       memset(foutbuf, 0, LPC_FILTERORDER*sizeof(float));
       for (k=0; k<LPC_FILTERORDER; k++) {
           numerator[k]=syntDenum[LPC_FILTERORDER-k];
       }
       numerator[LPC_FILTERORDER]=syntDenum[0];
       tmp = &tmpbuf[LPC_FILTERORDER];
       fout = &foutbuf[LPC_FILTERORDER];

       memcpy(tmp, residual, len*sizeof(float));
       memset(tmp+len, 0, len*sizeof(float));
       ZeroPoleFilter(tmp, numerator, syntDenum, 2*len,
           LPC_FILTERORDER, fout);
       for (k=0; k<len; k++) {
           fout[k] += fout[k+len];
       }

       maxVal = fout[0];
       for (k=1; k<len; k++) {

           if (fout[k]*fout[k] > maxVal*maxVal){
               maxVal = fout[k];
           }
       }
       maxVal=(float)fabs(maxVal);

       if (maxVal < 10.0) {
           maxVal = 10.0;
       }
       maxVal = (float)log10(maxVal);
       sort_sq(&dtmp, idxForMax, maxVal, state_frgqTbl, 64);

       maxVal=state_frgqTbl[*idxForMax];
       qmax = (float)pow(10,maxVal);
       scal = (float)(4.5)/qmax;
       for (k=0; k<len; k++){
           fout[k] *= scal;
       }

       AbsQuantW(iLBCenc_inst, fout,syntDenum,
           weightDenum,idxVec, len, state_first);
   }











