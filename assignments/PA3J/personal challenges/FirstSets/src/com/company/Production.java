package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Production {
  private final List<GrammarSymbol> productionSymbols = new ArrayList<>();

  public Production(GrammarSymbol... symbols) {
    productionSymbols.addAll(Arrays.asList(symbols));
  }

  public List<GrammarSymbol> getSymbols() {
    return Collections.unmodifiableList(productionSymbols);
  }

  @Override
  public String toString() {
    return productionSymbols.toString();
  }
}
