package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NonTerminal extends GrammarSymbol {
  private final List<Production> productions = new ArrayList<>();

  public NonTerminal(String name) {
    super(name);
  }

  @Override
  public FirstSet getFirstSet() {
    FirstSet first = new FirstSet();

    // If there is a Production X → ε or
    // X → Y1Y2..Yk where Ai → ε
    // then add ε to first(X)
    for (Production production : productions) {
      boolean goesToEpsilon = true;
      for (GrammarSymbol symbol : production.getSymbols()) {
        if (!symbol.getFirstSet().contains(Terminal.EPSILON)) {
          goesToEpsilon = false;
          break;
        }
      }
      // Current production goes to epsilon
      if (goesToEpsilon) {
        first.add(Terminal.EPSILON);
        // We don't need to add more epsilons even when we find other productions that go to
        // epsilon.
        break;
      }
    }

    for (Production production : productions) {
      for (GrammarSymbol symbol : production.getSymbols()) {
        first.addAll(
            symbol.getFirstSet().stream()
                .filter(term -> term != Terminal.EPSILON)
                .collect(Collectors.toList()));
        if (!symbol.getFirstSet().contains(Terminal.EPSILON)) {
          break;
        }
      }
    }

    return first;
  }

  public void setProductions(Production... productions) {
    this.productions.clear();
    this.productions.addAll(Arrays.asList(productions));
  }

  public List<Production> getProductions() {
    return Collections.unmodifiableList(productions);
  }
}
