#include <stdio.h>
#include <stdlib.h>
#include <memory.h>


void printInt(int i) {
    printf("%d\n", i);
}

void printString(char* str) {
    printf("%s\n", str);
}

char* readString() {
    char *line = NULL;  /* forces getline to allocate with malloc */
    size_t len = 0;     /* ignored when line = NULL */
    ssize_t read;


    if ((read = getline(&line, &len, stdin)) != -1) {
        if (read > 0) {
            fprintf(stderr, "\n  read %zd chars from stdin, allocated %zd bytes for line : %s\n", read, len, line);
        }
    }

    return line;
}

long readInt() {
    long i = 0;

    scanf("%ld", &i);

    return i;
}

void error() {
    printf("runtime error");
    exit(1);
}

char* _addStrings(char* s1, char* s2) {
      char * s3 = (char *) malloc(1 + strlen(s1)+ strlen(s2));
      strcpy(s3, s1);
      strcat(s3, s2);

      return s3;
}

int* _mallocArray(int size) {
    if (size <= 0) {
        printf("can't malloc non positive size\n");
        exit(1);
    }

    int* arr = (int *) malloc((1 + size) * sizeof(long));

    if (!arr) {
        printf("malloc error of size %d\n", size);
        exit(1);
    }

    arr[0] = size;

    return arr;
}

int* _mallocSize(int size) {
    if (size < 0) {
        printf("can't malloc negative size");
    }

    int* arr = (int *) malloc(size * sizeof(long));

    return arr;
}
