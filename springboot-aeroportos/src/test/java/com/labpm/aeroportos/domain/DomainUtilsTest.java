package com.labpm.aeroportos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainUtilsTest {
  @Test
  void converterPesParaMetros_1000() {
    double m = DomainUtils.converterPesParaMetros(1000);
    assertEquals(304.8, m, 0.0001);
  }

  @Test
  void converterPesParaMetros_zero() {
    double m = DomainUtils.converterPesParaMetros(0);
    assertEquals(0.0, m, 0.0001);
  }

  @Test
  void obterIsoPais_brazil() {
    assertEquals("BR", DomainUtils.obterIsoPais("Brazil"));
  }

  @Test
  void obterIsoPais_spain() {
    assertEquals("ES", DomainUtils.obterIsoPais("Spain"));
  }
}