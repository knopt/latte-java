#!/bin/sh

nasm -f elf64 -F dwarf -g $1
gcc -o $(basename $1 .asm) $(basename $1 .asm).o
