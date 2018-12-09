#!/bin/sh

java -cp ".:java-cup-11b-runtime.jar" Latte.Generate <$1 >$(basename $1 .lat).asm
nasm -f elf64 -F dwarf -g $(basename $1 .lat).asm
gcc -o $(basename $1 .lat) $(basename $1 .lat).o
./$(basename $1 .lat)
