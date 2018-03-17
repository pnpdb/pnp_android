
   #include <math.h>
   #include <string.h>

   #include "iLBC_define.h"
   #include "constants.h"
   #include "filter.h"

   void StateConstructW(
       int idxForMax,
       int *idxVec,
       float *syntDenum,

       float *out,
       int len
   ){
       float maxVal, tmpbuf[LPC_FILTERORDER+2*STATE_LEN], *tmp,
           numerator[LPC_FILTERORDER+1];
       float foutbuf[LPC_FILTERORDER+2*STATE_LEN], *fout;
       int k,tmpi;

       maxVal = state_frgqTbl[idxForMax];
       maxVal = (float)pow(10,maxVal)/(float)4.5;

       memset(tmpbuf, 0, LPC_FILTERORDER*sizeof(float));
       memset(foutbuf, 0, LPC_FILTERORDER*sizeof(float));
       for (k=0; k<LPC_FILTERORDER; k++) {
           numerator[k]=syntDenum[LPC_FILTERORDER-k];
       }
       numerator[LPC_FILTERORDER]=syntDenum[0];
       tmp = &tmpbuf[LPC_FILTERORDER];
       fout = &foutbuf[LPC_FILTERORDER];

       for (k=0; k<len; k++) {
           tmpi = len-1-k;
           tmp[k] = maxVal*state_sq3Tbl[idxVec[tmpi]];
       }


       memset(tmp+len, 0, len*sizeof(float));
       ZeroPoleFilter(tmp, numerator, syntDenum, 2*len,
           LPC_FILTERORDER, fout);
       for (k=0;k<len;k++) {
           out[k] = fout[len-1-k]+fout[2*len-1-k];
       }
   }












