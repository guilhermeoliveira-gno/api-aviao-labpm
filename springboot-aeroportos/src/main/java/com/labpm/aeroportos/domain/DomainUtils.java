package com.labpm.aeroportos.domain;

public final class DomainUtils {
  private DomainUtils() {}

  public static double converterPesParaMetros(double pes) {
    return pes * 0.3048d;
  }

  public static String obterIsoPais(String nome) {
    if (nome == null) return null;
    String n = nome.trim().toLowerCase();
    switch (n) {
      case "brazil": return "BR";
      case "united states": return "US";
      case "spain": return "ES";
      case "canada": return "CA";
      default: return null;
    }
  }
}