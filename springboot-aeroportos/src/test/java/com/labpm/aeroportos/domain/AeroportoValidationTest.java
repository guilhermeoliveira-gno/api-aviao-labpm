package com.labpm.aeroportos.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AeroportoValidationTest {
  static Validator validator;

  @BeforeAll
  static void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void iata_com_quatro_letras_invalido() {
    Aeroporto a = new Aeroporto();
    a.setName("N"); a.setCity("C"); a.setCountry("B");
    a.setIata("ABCD");
    assertFalse(validator.validate(a).isEmpty());
  }

  @Test
  void altitude_negativa_invalida() {
    Aeroporto a = new Aeroporto();
    a.setName("N"); a.setCity("C"); a.setCountry("B");
    a.setIata("ABC");
    a.setAltitude(-1);
    assertFalse(validator.validate(a).isEmpty());
  }
}