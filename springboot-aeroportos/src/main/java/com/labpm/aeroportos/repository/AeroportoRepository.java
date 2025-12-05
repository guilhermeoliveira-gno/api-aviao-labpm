package com.labpm.aeroportos.repository;

import com.labpm.aeroportos.domain.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {
  Optional<Aeroporto> findByIata(String iata);
}