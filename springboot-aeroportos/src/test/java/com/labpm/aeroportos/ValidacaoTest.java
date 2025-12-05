package com.labpm.aeroportos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidacaoTest {
  @Test
  void iataValido() {
    assertTrue("GKA".matches("^[A-Z]{3}$"));
  }
}