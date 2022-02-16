#include <stdio.h>
#include <io.h>
#include "Stack.h"
#include "Parser.h"
#include <locale.h>

int main() {
    setlocale(LC_CTYPE, "");
    //int * int
    int tokens[] = {INT, STAR, INT, PLUS, INT, EOF};
    bool res = parse(tokens);
    printf("Parsing Result : %s", res == 1 ? "True" : "False");

    return 0;
}
