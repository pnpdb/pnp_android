
   #include "iLBC_define.h"

   void syntFilter(
       float *Out,
       float *a,
       int len,


       float *mem
   ){
       int i, j;
       float *po, *pi, *pa, *pm;

       po=Out;

       for (i=0; i<LPC_FILTERORDER; i++) {
           pi=&Out[i-1];
           pa=&a[1];
           pm=&mem[LPC_FILTERORDER-1];
           for (j=1; j<=i; j++) {
               *po-=(*pa++)*(*pi--);
           }
           for (j=i+1; j<LPC_FILTERORDER+1; j++) {
               *po-=(*pa++)*(*pm--);
           }
           po++;
       }

       for (i=LPC_FILTERORDER; i<len; i++) {
           pi=&Out[i-1];
           pa=&a[1];
           for (j=1; j<LPC_FILTERORDER+1; j++) {
               *po-=(*pa++)*(*pi--);
           }
           po++;
       }

       memcpy(mem, &Out[len-LPC_FILTERORDER],
           LPC_FILTERORDER*sizeof(float));
   }














