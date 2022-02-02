//
// Created by housi on 2021-06-06.
//

#include <stdbool.h>

#ifndef PREDICTIVEPARSINGINC_STACK_H
#define PREDICTIVEPARSINGINC_STACK_H

#endif //PREDICTIVEPARSINGINC_STACK_H

typedef struct Stack {
    int *arr;
    int capacity;
    int size;
} Stack;

Stack* newStack();

void push(Stack *s, int element);
int pop(Stack *s);
int top(Stack *s);
int size(Stack *s);
bool isEmpty(Stack *s);
