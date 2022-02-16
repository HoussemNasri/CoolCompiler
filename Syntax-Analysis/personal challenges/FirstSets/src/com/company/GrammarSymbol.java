package com.company;

public abstract class GrammarSymbol {

  private final String name;

  public GrammarSymbol(final String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  public boolean isTerminal() {
    return this instanceof Terminal;
  }

  public abstract FirstSet getFirstSet();

  @Override
  public String toString() {
    return isTerminal() ? "'" + name + "'" : name;
  }
}
