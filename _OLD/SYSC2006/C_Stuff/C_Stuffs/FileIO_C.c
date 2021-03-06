#include "FileIO_C.h"


unsigned int readFileText(FILE* file, char*** outputLines, unsigned int numLines, unsigned int bufferSize){
   int i = 0;
   unsigned int bufferSetSize = 0;

   *outputLines = (char**) malloc(sizeof(char*) * numLines); // outputLines now points to an array of character pointers.

   if (bufferSize != 0){
      bufferSetSize = bufferSize;
      char* a = (char*) malloc(sizeof(char) * numLines * bufferSize); // a is now a bufferSize * numLines sized memory block.
      if (a){
         for (i = 0; i < numLines; i++){
            (*outputLines)[i] = &(a[i * bufferSize]);
         }
      }
      else{
         free(*outputLines);
         free(a);
         return 0; // Memory failure.
      }
   }
   else{
      bufferSetSize = DEFAULT_MAX_READ;
      char* a = malloc(sizeof(char) * numLines * DEFAULT_MAX_READ);
      if (a){
         for (i = 0; i < numLines; i++){
            *(outputLines)[i] = &(a[i * DEFAULT_MAX_READ]);
         }
      }
      else{
         free(*outputLines);
         free(a);
         return 0; // Memory failure.
      }
   }

   i = 0;
   for (i = 0; i < numLines; i++){
      if (fgets( (*outputLines)[i], bufferSetSize, file) == NULL){
         break;
      }
      printf(numLines + '0');
      printf((*outputLines)[i]);
      printf("\n");
   }

   return i;
}

unsigned int readFileData(FILE* File, char** inputPtr, unsigned int maxRead){
   if (maxRead){
      char* buffer = malloc(sizeof(char) * maxRead);
      unsigned int readBytes = fread(buffer, sizeof(char), maxRead, File);
      *inputPtr = buffer;
      return readBytes;
   }
   else{
      char* buffer = malloc(sizeof(char) * DEFAULT_MAX_READ);
      unsigned int readBytes = fread(buffer, sizeof(char), maxRead, File);
      *inputPtr = buffer;
      return readBytes;
   }
}

void releaseLineBuffer(char*** inputLines, unsigned int numLines)
{
   ;//for
}









const struct library FileIO_C = {
    .readFileText = readFileText,
    .readFileData = readFileData,
};






