package com.labpm.aeroportos.service;

public class AeroportoNaoEncontradoException extends RuntimeException {
  public AeroportoNaoEncontradoException(String message) { super(message); }
}