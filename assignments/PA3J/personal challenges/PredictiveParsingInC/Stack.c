//
// Created by housi on 2021-06-06.
//

#include <malloc.h>
#include "Stack.h"
#define CAPACITY 1024

Stack* newStack(){
    Stack* stack = malloc(sizeof(Stack*));
    stack->capacity = CAPACITY;
    stack->size = 0;
    stack->arr = malloc(sizeof(int*) * stack->capacity);
    return stack;
}

void push(Stack *s, int element) {
    if (s->size + 1 <= s->capacity) {
        s->arr[(s->size)++] = element;
    } else {
        //Throw an error
    }
}

int pop(Stack *s) {
    if (isEmpty(s)) {
        //Throw an error
        return -1;
    } else {
        return s->arr[--(s->size)];
    }
}

int top(Stack *s) {
    if (isEmpty(s)) {
        //Throw an error
        return -1;
    } else {
        return s->arr[s->size - 1];
    }
}

int size(Stack *s) {
    return s->size;
}

bool isEmpty(Stack *s) {
    return s->size == 0;
}