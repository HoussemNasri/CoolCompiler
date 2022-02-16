//
// Created by housi on 2021-06-06.
//

#include <stddef.h>
#include <stdarg.h>
#include <malloc.h>
#include <stdbool.h>

#ifndef PREDICTIVEPARSINGINC_PARSER_H
#define PREDICTIVEPARSINGINC_PARSER_H

#endif //PREDICTIVEPARSINGINC_PARSER_H

#define MAX_PRODUCTION_SIZE 20
#define NO_PROD {-1}
#define EPSILON {99}

//Non terminals
#define E 10
#define X 11
#define T 12
#define Y 13

//Terminals
#define INT 0
#define STAR 1
#define PLUS 2
#define LPAREN 3
#define RPAREN 4
#define EOF 5

bool parse(int tokens[]);

void error();

char *stringifySymbol(int symbol);
