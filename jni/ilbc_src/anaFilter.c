   #include <string.h>
   #include "iLBC_define.h"

   void anaFilter(
       float *In,
       float *a,
       int len,
       float *Out,
       float *mem
   ){
       int i, j;
       float *po, *pi, *pm, *pa;

       po = Out;

       for (i=0; i<LPC_FILTERORDER; i++) {
           pi = &In[i];
           pm = &mem[LPC_FILTERORDER-1];
           pa = a;
           *po=0.0;


           for (j=0; j<=i; j++) {
               *po+=(*pa++)*(*pi--);
           }
           for (j=i+1; j<LPC_FILTERORDER+1; j++) {

               *po+=(*pa++)*(*pm--);
           }
           po++;
       }

       for (i=LPC_FILTERORDER; i<len; i++) {
           pi = &In[i];
           pa = a;
           *po=0.0;
           for (j=0; j<LPC_FILTERORDER+1; j++) {
               *po+=(*pa++)*(*pi--);
           }
           po++;
       }

       memcpy(mem, &In[len-LPC_FILTERORDER],
           LPC_FILTERORDER*sizeof(float));
   }

