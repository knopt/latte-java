section .rodata
section .text
   align 16
   global swap
   extern printInt
   extern readString
   global extractMax
   global main
   extern printString
   extern readInt
   extern error
   global heapSort
   global heapDown
   extern _addStrings
   extern _mallocArray
   extern _mallocSize
   extern _emptyString
; code for function swap
swap:
   push rbp
   mov rbp, rsp
   sub rsp, 32
   mov [rbp - 8], rdi
   mov [rbp - 16], rsi
   mov [rbp - 24], rdx
; 
;  declaration int temp = tab [x];

   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, [rbp - 16]
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   mov [rbp - 32], rax
; 
; assignment tab [x] = tab [y];

   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, [rbp - 24]
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   push rcx
   push rdx
   mov rcx, rax
   mov rax, [rbp - 16]
   mov rdx, rax
   add rdx, 1
   imul rdx, 8
   mov rax, [rbp - 8]
   add rax, rdx
   pop rdx
   mov [rax], rcx
   pop rcx
; 
; assignment tab [y] = temp;

   mov rax, [rbp - 32]
   push rcx
   push rdx
   mov rcx, rax
   mov rax, [rbp - 24]
   mov rdx, rax
   add rdx, 1
   imul rdx, 8
   mov rax, [rbp - 8]
   add rax, rdx
   pop rdx
   mov [rax], rcx
   pop rcx
; 
; void return from function
   mov rsp, rbp
   pop rbp
   ret
; code for function extractMax
extractMax:
   push rbp
   mov rbp, rsp
   sub rsp, 32
   mov [rbp - 8], rdi
   mov [rbp - 16], rsi
; 
;  declaration int max = heap [0];

   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, 0
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   mov [rbp - 24], rax
; 
; assignment heap [0] = heap [heapSize - 1];

   push rcx
   mov rax, [rbp - 8]
   push rax
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 16]
   pop rcx
   sub rax, rcx
   pop rcx
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   push rcx
   push rdx
   mov rcx, rax
   mov rax, 0
   mov rdx, rax
   add rdx, 1
   imul rdx, 8
   mov rax, [rbp - 8]
   add rax, rdx
   pop rdx
   mov [rax], rcx
   pop rcx
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 8]
   mov rdi, rax
   mov rax, 0
   mov rsi, rax
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 16]
   pop rcx
   sub rax, rcx
   pop rcx
   mov rdx, rax
   call heapDown
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
; 
; return from function
   mov rax, [rbp - 24]
   mov rsp, rbp
   pop rbp
   ret
; code for function main
main:
   push rbp
   mov rbp, rsp
   sub rsp, 32
; 
;  declaration int n = readInt ();

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
   mov [rbp - 8], rax
; 
;  declaration int [] tab = new int [n];

   mov rax, [rbp - 8]
   mov rdi, rax
   push rdi
   call _mallocArray
   pop rdi
   mov [rbp - 16], rax
; 
;  declaration int i = 0;

   mov rax, 0
   mov [rbp - 24], rax
_start_while_0:
   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, [rbp - 24]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jl _rel_end_2
   mov rax, 0
_rel_end_2:
   pop rcx
   cmp rax, 1
   jne _end_start_1
; 
; block
; 
; assignment tab [i] = readInt ();

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
   push rcx
   push rdx
   mov rcx, rax
   mov rax, [rbp - 24]
   mov rdx, rax
   add rdx, 1
   imul rdx, 8
   mov rax, [rbp - 16]
   add rax, rdx
   pop rdx
   mov [rax], rcx
   pop rcx
   mov rax, [rbp - 24]
   add rax, 1
   mov [rbp - 24], rax
; 
; end block
   jmp _start_while_0
_end_start_1:
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 16]
   mov rdi, rax
   call heapSort
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   push rdx
   push rcx
   mov rax, [rbp - 16]
   mov rcx, rax
   mov rdx, [rcx]
_start_for_arr_3:
   cmp rdx, 0
   je _end_for_arr_4
   mov rax, rdx
   imul rax, 8
   add rax, rcx
   mov rax, [rax]
   mov [rbp - 32], rax
; 
; block
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 32]
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
   sub rdx, 1
   jmp _start_for_arr_3
_end_for_arr_4:
   pop rcx
   pop rdx
; 
; return from function
   mov rax, 0
   mov rsp, rbp
   pop rbp
   ret
; code for function heapSort
heapSort:
   push rbp
   mov rbp, rsp
   sub rsp, 16
   mov [rbp - 8], rdi
; 
;  declaration int i = heap . length / 2;

   push rcx
   mov rax, 2
   push rax
   mov rax, [rbp - 8]
   mov rax, [rax]
   pop rcx
   push rdx
   xor rdx, rdx
   idiv rcx
   pop rdx
   pop rcx
   mov [rbp - 16], rax
_start_while_5:
   push rcx
   mov rax, 0
   push rax
   mov rax, [rbp - 16]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jge _rel_end_7
   mov rax, 0
_rel_end_7:
   pop rcx
   cmp rax, 1
   jne _end_start_6
; 
; block
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 8]
   mov rdi, rax
   mov rax, [rbp - 16]
   mov rsi, rax
   mov rax, [rbp - 8]
   mov rax, [rax]
   mov rdx, rax
   call heapDown
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   mov rax, [rbp - 16]
   sub rax, 1
   mov [rbp - 16], rax
; 
; end block
   jmp _start_while_5
_end_start_6:
; 
; assignment i = heap . length - 1;

   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 8]
   mov rax, [rax]
   pop rcx
   sub rax, rcx
   pop rcx
   mov [rbp - 16], rax
_start_while_8:
   push rcx
   mov rax, 0
   push rax
   mov rax, [rbp - 16]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jge _rel_end_10
   mov rax, 0
_rel_end_10:
   pop rcx
   cmp rax, 1
   jne _end_start_9
; 
; block
; 
; assignment heap [i] = extractMax (heap, i + 1);

   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 8]
   mov rdi, rax
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 16]
   pop rcx
   add rax, rcx
   pop rcx
   mov rsi, rax
   call extractMax
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
   push rcx
   push rdx
   mov rcx, rax
   mov rax, [rbp - 16]
   mov rdx, rax
   add rdx, 1
   imul rdx, 8
   mov rax, [rbp - 8]
   add rax, rdx
   pop rdx
   mov [rax], rcx
   pop rcx
   mov rax, [rbp - 16]
   sub rax, 1
   mov [rbp - 16], rax
; 
; end block
   jmp _start_while_8
_end_start_9:
; 
; void return from function
   mov rsp, rbp
   pop rbp
   ret
; code for function heapDown
heapDown:
   push rbp
   mov rbp, rsp
   sub rsp, 48
   mov [rbp - 8], rdi
   mov [rbp - 16], rsi
   mov [rbp - 24], rdx
_start_while_11:
   push rcx
   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 24]
   pop rcx
   sub rax, rcx
   pop rcx
   push rax
   push rcx
   mov rax, 2
   push rax
   mov rax, [rbp - 16]
   pop rcx
   imul rax, rcx
   pop rcx
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jl _rel_end_13
   mov rax, 0
_rel_end_13:
   pop rcx
   cmp rax, 1
   jne _end_start_12
; 
; block
; 
;  declaration int left = index * 2 + 1;

   push rcx
   mov rax, 1
   push rax
   push rcx
   mov rax, 2
   push rax
   mov rax, [rbp - 16]
   pop rcx
   imul rax, rcx
   pop rcx
   pop rcx
   add rax, rcx
   pop rcx
   mov [rbp - 32], rax
; 
;  declaration int right = left + 1;

   push rcx
   mov rax, 1
   push rax
   mov rax, [rbp - 32]
   pop rcx
   add rax, rcx
   pop rcx
   mov [rbp - 40], rax
; 
;  declaration int max = left;

   mov rax, [rbp - 32]
   mov [rbp - 48], rax
   push rcx
   mov rax, [rbp - 24]
   push rax
   mov rax, [rbp - 40]
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jl _rel_end_14
   mov rax, 0
_rel_end_14:
   pop rcx
   cmp rax, 0
   je _and_end_15
   push rcx
   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, [rbp - 48]
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   push rax
   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, [rbp - 40]
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jg _rel_end_16
   mov rax, 0
_rel_end_16:
   pop rcx
_and_end_15:
   cmp rax, 1
   jne _end_if_17
; 
; block
; 
; assignment max = right;

   mov rax, [rbp - 40]
   mov [rbp - 48], rax
; 
; end block
_end_if_17:
   push rcx
   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, [rbp - 16]
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   push rax
   push rcx
   mov rax, [rbp - 8]
   push rax
   mov rax, [rbp - 48]
   pop rcx
   mov rax, [rcx + 8 * rax + 8]
   pop rcx
   pop rcx
   cmp rax, rcx
   mov rax, 1
   jg _rel_end_18
   mov rax, 0
_rel_end_18:
   pop rcx
   cmp rax, 1
   jne _else_20
; 
; block
   push rdi
   push rsi
   push rdx
   push rcx
   push r8
   push r9
   mov rax, [rbp - 8]
   mov rdi, rax
   mov rax, [rbp - 48]
   mov rsi, rax
   mov rax, [rbp - 16]
   mov rdx, rax
   call swap
   pop r9
   pop r8
   pop rcx
   pop rdx
   pop rsi
   pop rdi
; 
; assignment index = max;

   mov rax, [rbp - 48]
   mov [rbp - 16], rax
; 
; end block
   jmp _end_if_19
_else_20:
; 
; block
; 
; void return from function
   mov rsp, rbp
   pop rbp
   ret
; 
; end block
_end_if_19:
; 
; end block
   jmp _start_while_11
_end_start_12:
; 
; void return from function
   mov rsp, rbp
   pop rbp
   ret
