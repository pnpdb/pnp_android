
   #ifndef __iLBC_ICBSEARCH_H
   #define __iLBC_ICBSEARCH_H






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
   );

   #endif

