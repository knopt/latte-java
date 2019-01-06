section .rodata
   str_0 db "ala", 0
section .text
   align 16
   extern printInt
   extern readString
   global main
   extern printString
   extern readInt
   extern error
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
;  declaration string s1 = "ala";

   mov rax, str_0
   mov [rbp - 8], rax
; 
;  declaration string s2;

   call _emptyString
   mov [rbp - 16], rax
; 
;  declaration string s3 = s2 + s1;

   push rdi
   push rsi
   mov rax, [rbp - 16]
   mov rdi, rax
   mov rax, [rbp - 8]
   mov rsi, rax
   call _addStrings
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
   call printString
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
   call printString
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
   call printString
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
