   #ifndef __iLBC_CONSTANTS_H
   #define __iLBC_CONSTANTS_H

   #include "iLBC_define.h"

   extern const iLBC_ULP_Inst_t ULP_20msTbl;
   extern const iLBC_ULP_Inst_t ULP_30msTbl;


   extern float hpi_zero_coefsTbl[];
   extern float hpi_pole_coefsTbl[];
   extern float hpo_zero_coefsTbl[];
   extern float hpo_pole_coefsTbl[];

   extern float lpFilt_coefsTbl[];

   extern float lpc_winTbl[];
   extern float lpc_asymwinTbl[];
   extern float lpc_lagwinTbl[];
   extern float lsfCbTbl[];
   extern float lsfmeanTbl[];
   extern int   dim_lsfCbTbl[];
   extern int   size_lsfCbTbl[];
   extern float lsf_weightTbl_30ms[];
   extern float lsf_weightTbl_20ms[];


   extern float state_sq3Tbl[];
   extern float state_frgqTbl[];


   extern float gain_sq3Tbl[];
   extern float gain_sq4Tbl[];
   extern float gain_sq5Tbl[];


   extern int search_rangeTbl[5][CB_NSTAGES];
   extern int memLfTbl[];
   extern int stMemLTbl;
   extern float cbfiltersTbl[CB_FILTERLEN];


   extern float polyphaserTbl[];
   extern float enh_plocsTbl[];


   #endif

