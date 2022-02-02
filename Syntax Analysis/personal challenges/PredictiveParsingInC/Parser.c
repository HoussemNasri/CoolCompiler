//
// Created by housi on 2021-06-06.
//

#include <stdio.h>
#include <string.h>
#include "Parser.h"
#include "Stack.h"

#define TERMINALS 6
#define NONTERMINALS 4

const int terminals[] = {0, 1, 2, 3, 4, 5};
const int nonterminals[] = {10, 11, 12, 13};

/**The first case in each entry is the number of tokens on that production, except for constant productions */
const int parsingTable[NONTERMINALS][TERMINALS][MAX_PRODUCTION_SIZE] =
        {
                {{2, T,   X}, NO_PROD,  NO_PROD, {2, T,      X},         NO_PROD, NO_PROD},
                {NO_PROD,     NO_PROD, {2, PLUS, E}, NO_PROD,            EPSILON, EPSILON},
                {{2, INT, Y}, NO_PROD,  NO_PROD, {3, LPAREN, E, RPAREN}, NO_PROD, NO_PROD},
                {NO_PROD, {2, STAR, T}, EPSILON,     NO_PROD,            EPSILON, EPSILON}
        };

bool isTerminal(int x) {
    for (int i = 0; i < TERMINALS; i++) {
        if (terminals[i] == x) {
            return true;
        }
    }
    return false;
}

bool isNonTerminal(int x) {
    for (int i = 0; i < NONTERMINALS; i++) {
        if (nonterminals[i] == x) {
            return true;
        }
    }
    return false;
}

void printStack(Stack s) {
    for (int i = 0; i < s.size; i++) {
        int len = strlen(stringifySymbol(s.arr[s.size - i - 1]));
        printf("|%5s%*s|\n",stringifySymbol(s.arr[s.size - i - 1]),len - 5, "");
    }
    printf("-------\n");
}

/* Returns true if the supplied tokens do match the grammar. */
bool parse(int *tokens) {
    Stack *stack = newStack();
    push(stack, EOF);
    push(stack, E);
    do {
        printf("next token : %s\n", stringifySymbol(*tokens));
        printStack(*stack);
        //Top of stack is Non-Terminal
        if (isNonTerminal(top(stack))) {
            printf("Matching Non-Terminal %s\n", stringifySymbol(top(stack)));
            if (parsingTable[top(stack) % 10][*tokens][0] != -1) {
                const int *symbols = parsingTable[top(stack) % 10][*tokens];
                int prodCount = symbols[0];
                pop(stack);
                if (symbols[0] != 99) {
                    for (int i = prodCount; i >= 1; i--) {
                        push(stack, symbols[i]);
                    }
                }
                printf("Stack Size : %d\n", size(stack));
            } else {
                error();
                return false;
            }
        }
            //Top of stack is Terminal
        else if (isTerminal(top(stack) % TERMINALS)) {
            printf("Matching Terminal %s\n", stringifySymbol(top(stack)));
            if (top(stack) == *tokens++) {
                printf("We found a match! %s == %s\n", stringifySymbol(top(stack)), stringifySymbol(*(tokens - 1)));
                //Popping the matched terminal
                pop(stack);
            } else {
                error();
                return false;
            }
        }
    } while (!isEmpty(stack));

    return true;
}

void error() {
    puts("Cannot parse this input!");
}

char *stringifySymbol(int symbol) {
    char *result;
    switch (symbol) {
        case E:
            result = "\"E\"";
            break;
        case X:
            result = "\"X\"";
            break;
        case T:
            result = "\"T\"";
            break;
        case Y:
            result = "\"Y\"";
            break;
        case INT:
            result = "'INT'";
            break;
        case STAR:
            result = "'*'";
            break;
        case PLUS:
            result = "'+'";
            break;
        case LPAREN:
            result = "'('";
            break;
        case RPAREN:
            result = "')'";
            break;
        case EOF:
            result = "'$'";
            break;
        default:
            result = "Error!";
    }
    return result;
}

