package com.labpm.aeroportos.repository;

import com.labpm.aeroportos.domain.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {
  Optional<Aeroporto> findByIata(String iata);
  @Query("select coalesce(max(a.id),0)+1 from Aeroporto a")
  Long nextId();
}