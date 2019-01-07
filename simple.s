section .rodata
section .text
   align 16
   extern printInt
   extern readString
   global main
   extern printString
   extern readInt
   extern error
   global readInt2
   global readInt4
   global readInt3
   extern _addStrings
   extern _mallocArray
   extern _mallocSize
   extern _emptyString
; code for function main
main:
   push rbp
   mov rbp, rsp
   sub rsp, 32
; 
;  declaration int i, j, k;

   mov rax, 0
   mov [rbp - 8], rax
   mov rax, 0
   mov [rbp - 16], rax
   mov rax, 0
   mov [rbp - 24], rax
; 
; assignment i = readInt2 ();

   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   call readInt2
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov [rbp - 8], rax
; 
; assignment j = readInt3 (i);

   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 8]
   mov rdi, rax
   call readInt3
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov [rbp - 16], rax
; 
; assignment k = readInt4 ();

   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   call readInt4
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov [rbp - 24], rax
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 8]
   mov rdi, rax
   call printInt
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 16]
   mov rdi, rax
   call printInt
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 24]
   mov rdi, rax
   call printInt
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
; 
; return from function
   mov rax, 0
   mov rsp, rbp
   pop rbp
   ret
; code for function readInt2
readInt2:
   push rbp
   mov rbp, rsp
   sub rsp, 0
; 
; return from function
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   call readInt
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov rsp, rbp
   pop rbp
   ret
; code for function readInt4
readInt4:
   push rbp
   mov rbp, rsp
   sub rsp, 16
; 
;  declaration int a;

   mov rax, 0
   mov [rbp - 8], rax
; 
; return from function
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   call readInt
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov rsp, rbp
   pop rbp
   ret
; code for function readInt3
readInt3:
   push rbp
   mov rbp, rsp
   sub rsp, 16
   mov [rbp - 8], rdi
; 
; return from function
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   call readInt
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov rsp, rbp
   pop rbp
   ret
