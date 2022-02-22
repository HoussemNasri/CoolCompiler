package com.company;

import java.util.Arrays;
import java.util.HashSet;

public class FirstSet extends HashSet<Terminal> {

  public FirstSet(Terminal... terminals) {
    addAll(Arrays.asList(terminals));
  }
}
