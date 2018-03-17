
   #include <math.h>
   #include <stdlib.h>

   #include "iLBC_define.h"
   #include "constants.h"
   #include "helpfun.h"
   #include "string.h"

   void packsplit(
       int *index,
       int *firstpart,
       int *rest,

       int bitno_firstpart,
       int bitno_total
   ){
       int bitno_rest = bitno_total-bitno_firstpart;

       *firstpart = *index>>(bitno_rest);
       *rest = *index-(*firstpart<<(bitno_rest));
   }

   void packcombine(
       int *index,
       int rest,
       int bitno_rest
   ){
       *index = *index<<bitno_rest;
       *index += rest;
   }

   void dopack(
       unsigned char **bitstream,
       int index,
       int bitno,
       int *pos
   ){
       int posLeft;

       if ((*pos)==0) {

           **bitstream=0;
       }

       while (bitno>0) {

           if (*pos==8) {
               *pos=0;
               (*bitstream)++;
               **bitstream=0;
           }

           posLeft=8-(*pos);

           if (bitno <= posLeft) {
               **bitstream |= (unsigned char)(index<<(posLeft-bitno));
               *pos+=bitno;
               bitno=0;
           } else {
               **bitstream |= (unsigned char)(index>>(bitno-posLeft));

               *pos=8;
               index-=((index>>(bitno-posLeft))<<(bitno-posLeft));

               bitno-=posLeft;
           }
       }
   }

   void unpack(
       unsigned char **bitstream,
       int *index,
       int bitno,
       int *pos

   ){
       int BitsLeft;

       *index=0;

       while (bitno>0) {

           if (*pos==8) {
               *pos=0;
               (*bitstream)++;
           }

           BitsLeft=8-(*pos);

           if (BitsLeft>=bitno) {
               *index+=((((**bitstream)<<(*pos)) & 0xFF)>>(8-bitno));

               *pos+=bitno;
               bitno=0;
           } else {

               if ((8-bitno)>0) {
                   *index+=((((**bitstream)<<(*pos)) & 0xFF)>>
                       (8-bitno));
                   *pos=8;
               } else {
                   *index+=(((int)(((**bitstream)<<(*pos)) & 0xFF))<<
                       (bitno-8));
                   *pos=8;
               }
               bitno-=BitsLeft;
           }
       }
   }

