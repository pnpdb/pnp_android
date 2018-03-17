
   #ifndef __iLBC_ILBCENCODE_H
   #define __iLBC_ILBCENCODE_H

   #include "iLBC_define.h"

   short initEncode(
       iLBC_Enc_Inst_t *iLBCenc_inst,
       int mode
   );

   void iLBC_encode(

       unsigned char *bytes,
       float *block,
       iLBC_Enc_Inst_t *iLBCenc_inst
   );

   #endif
