   #include "iLBC_define.h"
   #include "constants.h"
   #include <string.h>
   #include <math.h>

   void filteredCBvecs(
       float *cbvectors,

       float *mem,

       int lMem
   ){
       int j, k;
       float *pp, *pp1;
       float tempbuff2[CB_MEML+CB_FILTERLEN];
       float *pos;

       memset(tempbuff2, 0, (CB_HALFFILTERLEN-1)*sizeof(float));
       memcpy(&tempbuff2[CB_HALFFILTERLEN-1], mem, lMem*sizeof(float));
       memset(&tempbuff2[lMem+CB_HALFFILTERLEN-1], 0,
           (CB_HALFFILTERLEN+1)*sizeof(float));

       pos=cbvectors;
       memset(pos, 0, lMem*sizeof(float));
       for (k=0; k<lMem; k++) {
           pp=&tempbuff2[k];
           pp1=&cbfiltersTbl[CB_FILTERLEN-1];
           for (j=0;j<CB_FILTERLEN;j++) {
               (*pos)+=(*pp++)*(*pp1--);
           }
           pos++;
       }
   }


   void searchAugmentedCB(
       int low,
       int high,
       int stage,
       int startIndex,
       float *target,
       float *buffer,
       float *max_measure,
       int *best_index,
       float *gain,
       float *energy,
       float *invenergy
   ) {
       int icount, ilow, j, tmpIndex;
       float *pp, *ppo, *ppi, *ppe, crossDot, alfa;
       float weighted, measure, nrjRecursive;
       float ftmp;


       nrjRecursive = (float) 0.0;
       pp = buffer - low + 1;
       for (j=0; j<(low-5); j++) {
           nrjRecursive += ( (*pp)*(*pp) );
           pp++;
       }
       ppe = buffer - low;


       for (icount=low; icount<=high; icount++) {


           tmpIndex = startIndex+icount-20;

           ilow = icount-4;

           nrjRecursive = nrjRecursive + (*ppe)*(*ppe);
           ppe--;
           energy[tmpIndex] = nrjRecursive;

           crossDot = (float) 0.0;
           pp = buffer-icount;
           for (j=0; j<ilow; j++) {
               crossDot += target[j]*(*pp++);
           }

           alfa = (float) 0.2;
           ppo = buffer-4;
           ppi = buffer-icount-4;
           for (j=ilow; j<icount; j++) {
               weighted = ((float)1.0-alfa)*(*ppo)+alfa*(*ppi);
               ppo++;
               ppi++;
               energy[tmpIndex] += weighted*weighted;
               crossDot += target[j]*weighted;
               alfa += (float)0.2;
           }

           pp = buffer - icount;
           for (j=icount; j<SUBL; j++) {
               energy[tmpIndex] += (*pp)*(*pp);
               crossDot += target[j]*(*pp++);
           }

           if (energy[tmpIndex]>0.0) {
               invenergy[tmpIndex]=(float)1.0/(energy[tmpIndex]+EPS);
           } else {
               invenergy[tmpIndex] = (float) 0.0;
           }

           if (stage==0) {
               measure = (float)-10000000.0;

               if (crossDot > 0.0) {
                   measure = crossDot*crossDot*invenergy[tmpIndex];
               }
           }
           else {
               measure = crossDot*crossDot*invenergy[tmpIndex];
           }

           ftmp = crossDot*invenergy[tmpIndex];

           if ((measure>*max_measure) && (fabs(ftmp)<CB_MAXGAIN)) {
               *best_index = tmpIndex;
               *max_measure = measure;
               *gain = ftmp;
           }
       }
   }

   void createAugmentedVec(
       int index,
       float *buffer,
       float *cbVec
   ) {
       int ilow, j;
       float *pp, *ppo, *ppi, alfa, alfa1, weighted;

       ilow = index-5;

       pp = buffer-index;
       memcpy(cbVec,pp,sizeof(float)*index);

       alfa1 = (float)0.2;
       alfa = 0.0;
       ppo = buffer-5;
       ppi = buffer-index-5;
       for (j=ilow; j<index; j++) {
           weighted = ((float)1.0-alfa)*(*ppo)+alfa*(*ppi);
           ppo++;
           ppi++;
           cbVec[j] = weighted;
           alfa += alfa1;
       }

       pp = buffer - index;
       memcpy(cbVec+index,pp,sizeof(float)*(SUBL-index));

   }

