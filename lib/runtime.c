#include <stdio.h>
#include <stdlib.h>
#include <memory.h>


void printInt(int i) {
    printf("%d", i);
}

void printString(char* str) {
    printf("%s", str);
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

int readInt() {
    int i = 0;
    scanf("%d", &i);

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