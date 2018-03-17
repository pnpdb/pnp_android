   #ifndef __iLBC_DOLPC_H
   #define __iLBC_DOLPC_H

   void doThePLC(
       float *PLCresidual,
       float *PLClpc,
       int PLI,
       float *decresidual,
       float *lpc,
       int inlag,
       iLBC_Dec_Inst_t *iLBCdec_inst
   );

   #endif

