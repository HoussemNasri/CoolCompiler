package com.company;

public class Terminal extends GrammarSymbol {
  public static final Terminal EPSILON =
      new Terminal(String.valueOf(Character.toChars(0x03B5)));

  public Terminal(String name) {
    super(name);
  }

  @Override
  public FirstSet getFirstSet() {
    // If X is a terminal then First(X) is just X
    return new FirstSet(this);
  }
}
