section .rodata
   str_0 db "", 0
   str_1 db "/* world", 0
   str_2 db "hello */", 0
   str_3 db "=", 0
section .text
   align 16
   global rfac
   global ifac2f
   extern readString
   global mfac
   global fac
   global nfac
   global main
   extern error
   global ifac
   extern printInt
   extern printString
   extern readInt
   global repStr
   extern _addStrings
   extern _mallocArray
   extern _mallocSize
; code for function rfac
rfac:
   push rbp
   mov rbp, rsp
   sub rsp, 16
   mov [rbp - 8], rdi
   push rcx
   mov rax, 0
   push rax
   mov rax, [rbp - 8]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   je _rel_end_4
   mov rax, 0
_rel_end_4:
   pop rcx
   cmp rax, 1
   jne _else_6
; 
; return from function
   mov rax, 1
   mov rsp, rbp
   pop rbp
   ret
   jmp _end_if_5
_else_6:
; 
; return from function
   push rcx
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
; aligning stack
   sub rsp, 8
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 8]
   pop rcx
   sub rax, rcx
   pop rcx
   mov rdi, rax
   call rfac
   add rsp, 8
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   push rax
   mov rax, [rbp - 8]
   pop rcx
   imul rax, rcx
   pop rcx
   mov rsp, rbp
   pop rbp
   ret
_end_if_5:
; code for function ifac2f
ifac2f:
   push rbp
   mov rbp, rsp
   sub rsp, 32
   mov [rbp - 8], rdi
   mov [rbp - 16], rsi
   push rcx
   mov rax, [rbp - 16]
   push rax
   mov rax, [rbp - 8]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   je _rel_end_7
   mov rax, 0
_rel_end_7:
   pop rcx
   cmp rax, 1
   jne _end_if_8
; 
; return from function
   mov rax, [rbp - 8]
   mov rsp, rbp
   pop rbp
   ret
_end_if_8:
   push rcx
   mov rax, [rbp - 16]
   push rax
   mov rax, [rbp - 8]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jg _rel_end_9
   mov rax, 0
_rel_end_9:
   pop rcx
   cmp rax, 1
   jne _end_if_10
; 
; return from function
   mov rax, 1
   mov rsp, rbp
   pop rbp
   ret
_end_if_10:
; 
;  declaration int m;

   mov rax, 0
   mov [rbp - 24], rax
; 
; assignment m = (l + h) / 2;

   push rcx
   mov rax, 2
   push rax
   push rcx
   mov rax, [rbp - 16]
   push rax
   mov rax, [rbp - 8]
   pop rcx
   add rax, rcx
   pop rcx
   pop rcx
   push rdx
   xor rdx, rdx
   idiv rcx
   pop rdx
   pop rcx
   mov [rbp - 24], rax
; 
; return from function
   push rcx
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
; aligning stack
   sub rsp, 8
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 24]
   pop rcx
   add rax, rcx
   pop rcx
   mov rdi, rax
   mov rax, [rbp - 16]
   mov rsi, rax
   call ifac2f
   add rsp, 8
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   push rax
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
; aligning stack
   sub rsp, 8
   mov rax, [rbp - 8]
   mov rdi, rax
   mov rax, [rbp - 24]
   mov rsi, rax
   call ifac2f
   add rsp, 8
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   pop rcx
   imul rax, rcx
   pop rcx
   mov rsp, rbp
   pop rbp
   ret
; code for function mfac
mfac:
   push rbp
   mov rbp, rsp
   sub rsp, 16
   mov [rbp - 8], rdi
   push rcx
   mov rax, 0
   push rax
   mov rax, [rbp - 8]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   je _rel_end_11
   mov rax, 0
_rel_end_11:
   pop rcx
   cmp rax, 1
   jne _else_13
; 
; return from function
   mov rax, 1
   mov rsp, rbp
   pop rbp
   ret
   jmp _end_if_12
_else_13:
; 
; return from function
   push rcx
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
; aligning stack
   sub rsp, 8
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 8]
   pop rcx
   sub rax, rcx
   pop rcx
   mov rdi, rax
   call nfac
   add rsp, 8
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   push rax
   mov rax, [rbp - 8]
   pop rcx
   imul rax, rcx
   pop rcx
   mov rsp, rbp
   pop rbp
   ret
_end_if_12:
; code for function fac
fac:
   push rbp
   mov rbp, rsp
   sub rsp, 32
   mov [rbp - 8], rdi
; 
;  declaration int r;

   mov rax, 0
   mov [rbp - 16], rax
; 
;  declaration int n;

   mov rax, 0
   mov [rbp - 24], rax
; 
; assignment r = 1;

   mov rax, 1
   mov [rbp - 16], rax
; 
; assignment n = a;

   mov rax, [rbp - 8]
   mov [rbp - 24], rax
_start_while_14:
   push rcx
   mov rax, 0
   push rax
   mov rax, [rbp - 24]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jg _rel_end_16
   mov rax, 0
_rel_end_16:
   pop rcx
   cmp rax, 1
   jne _end_start_15
; 
; block
; 
; assignment r = r * n;

   push rcx
   mov rax, [rbp - 24]
   push rax
   mov rax, [rbp - 16]
   pop rcx
   imul rax, rcx
   pop rcx
   mov [rbp - 16], rax
; 
; assignment n = n - 1;

   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 24]
   pop rcx
   sub rax, rcx
   pop rcx
   mov [rbp - 24], rax
; 
; end block
   jmp _start_while_14
_end_start_15:
; 
; return from function
   mov rax, [rbp - 16]
   mov rsp, rbp
   pop rbp
   ret
; code for function nfac
nfac:
   push rbp
   mov rbp, rsp
   sub rsp, 16
   mov [rbp - 8], rdi
   push rcx
   mov rax, 0
   push rax
   mov rax, [rbp - 8]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jne _rel_end_17
   mov rax, 0
_rel_end_17:
   pop rcx
   cmp rax, 1
   jne _else_19
; 
; return from function
   push rcx
   mov rax, [rbp - 8]
   push rax
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
; aligning stack
   sub rsp, 8
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 8]
   pop rcx
   sub rax, rcx
   pop rcx
   mov rdi, rax
   call mfac
   add rsp, 8
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   pop rcx
   imul rax, rcx
   pop rcx
   mov rsp, rbp
   pop rbp
   ret
   jmp _end_if_18
_else_19:
; 
; return from function
   mov rax, 1
   mov rsp, rbp
   pop rbp
   ret
_end_if_18:
; code for function main
main:
   push rbp
   mov rbp, rsp
   sub rsp, 32
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, 10
   mov rdi, rax
   call fac
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
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
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, 10
   mov rdi, rax
   call rfac
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
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
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, 10
   mov rdi, rax
   call mfac
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
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
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, 10
   mov rdi, rax
   call ifac
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov rdi, rax
   call printInt
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
; 
;  declaration string r;

   mov rax, 0
   mov [rbp - 8], rax
; 
; block
; 
;  declaration int n = 10;

   mov rax, 10
   mov [rbp - 16], rax
; 
;  declaration int r = 1;

   mov rax, 1
   mov [rbp - 24], rax
_start_while_20:
   push rcx
   mov rax, 0
   push rax
   mov rax, [rbp - 16]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jg _rel_end_22
   mov rax, 0
_rel_end_22:
   pop rcx
   cmp rax, 1
   jne _end_start_21
; 
; block
; 
; assignment r = r * n;

   push rcx
   mov rax, [rbp - 16]
   push rax
   mov rax, [rbp - 24]
   pop rcx
   imul rax, rcx
   pop rcx
   mov [rbp - 24], rax
   mov rax, [rbp - 16]
   sub rax, 1
   mov [rbp - 16], rax
; 
; end block
   jmp _start_while_20
_end_start_21:
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
; end block
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, str_3
   mov rdi, rax
   mov rax, 60
   mov rsi, rax
   call repStr
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
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
   mov rax, str_2
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
   mov rax, str_1
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
; code for function ifac
ifac:
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
   mov rax, 1
   mov rdi, rax
   mov rax, [rbp - 8]
   mov rsi, rax
   call ifac2f
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov rsp, rbp
   pop rbp
   ret
; code for function repStr
repStr:
   push rbp
   mov rbp, rsp
   sub rsp, 32
   mov [rbp - 8], rdi
   mov [rbp - 16], rsi
; 
;  declaration string r = "";

   mov rax, str_0
   mov [rbp - 24], rax
; 
;  declaration int i = 0;

   mov rax, 0
   mov [rbp - 32], rax
_start_while_23:
   push rcx
   mov rax, [rbp - 16]
   push rax
   mov rax, [rbp - 32]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jl _rel_end_25
   mov rax, 0
_rel_end_25:
   pop rcx
   cmp rax, 1
   jne _end_start_24
; 
; block
; 
; assignment r = r + s;

   push rdi
   push rsi
   mov rax, [rbp - 24]
   mov rdi, rax
   mov rax, [rbp - 8]
   mov rsi, rax
   call _addStrings
   pop rsi
   pop rdi
   mov [rbp - 24], rax
   mov rax, [rbp - 32]
   add rax, 1
   mov [rbp - 32], rax
; 
; end block
   jmp _start_while_23
_end_start_24:
; 
; return from function
   mov rax, [rbp - 24]
   mov rsp, rbp
   pop rbp
   ret
