#!/bin/sh

java -cp ".:java-cup-11b-runtime.jar:.:lib/commons-lang3-3.8.1.jar" Latte.Generate <$1 >$(basename $1 .lat).asm
nasm -f elf64 -F dwarf -g $(basename $1 .lat).asm
gcc -o lib/runtime.o -c lib/runtime.c
gcc -o $(basename $1 .lat) $(basename $1 .lat).o lib/runtime.o
./$(basename $1 .lat)
