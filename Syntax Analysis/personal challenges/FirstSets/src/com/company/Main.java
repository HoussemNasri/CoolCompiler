package com.company;

public class Main {
  /*
   * Grammar :
   * E → TX             X → +E | ε
   * T → (E) | int Y    Y → *T | ε
   */
  public static void main(String[] args) {
    Terminal lparen = new Terminal("(");
    Terminal rparen = new Terminal(")");
    Terminal star = new Terminal("*");
    Terminal plus = new Terminal("+");
    Terminal intSymbol = new Terminal("int");

    NonTerminal E = new NonTerminal("E");
    NonTerminal T = new NonTerminal("T");
    NonTerminal X = new NonTerminal("X");
    NonTerminal Y = new NonTerminal("Y");

    // TX
    Production e1 = new Production(T, X);
    // (E)
    Production t1 = new Production(lparen, E, rparen);
    // int Y
    Production t2 = new Production(intSymbol, Y);
    // +E
    Production x1 = new Production(plus, E);
    // *T
    Production y1 = new Production(star, T);

    E.setProductions(e1);
    T.setProductions(t1, t2);
    X.setProductions(x1, new Production(Terminal.EPSILON));
    Y.setProductions(y1, new Production(Terminal.EPSILON));

    System.out.println(E.getFirstSet());
    System.out.println(T.getFirstSet());
    System.out.println(X.getFirstSet());
    System.out.println(Y.getFirstSet());
  }
}
