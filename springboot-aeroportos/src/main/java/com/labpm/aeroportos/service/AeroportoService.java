package com.labpm.aeroportos.service;

import com.labpm.aeroportos.domain.Aeroporto;
import com.labpm.aeroportos.repository.AeroportoRepository;

public class AeroportoService {
  private final AeroportoRepository repo;
  public AeroportoService(AeroportoRepository repo) { this.repo = repo; }

  public Aeroporto buscarPorIata(String iata) {
    return repo.findByIata(iata).orElseThrow(() -> new AeroportoNaoEncontradoException("IATA não encontrado: " + iata));
  }

  public Aeroporto salvar(Aeroporto a) {
    if (a.getIata() == null || a.getIata().length() != 3) {
      throw new IllegalArgumentException("IATA deve ter 3 letras");
    }
    if (a.getAltitude() != null && a.getAltitude() < 0) {
      throw new IllegalArgumentException("Altitude não pode ser negativa");
    }
    return repo.save(a);
  }
}