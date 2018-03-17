
   #ifndef __iLBC_LPCENCOD_H
   #define __iLBC_LPCENCOD_H

   void LPCencode(
       float *syntdenum,
       float *weightdenum,
       int *lsf_index,
       float *data,
       iLBC_Enc_Inst_t *iLBCenc_inst
   );

   #endif

