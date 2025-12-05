package com.labpm.aeroportos.controller;

import com.labpm.aeroportos.domain.Aeroporto;
import com.labpm.aeroportos.repository.AeroportoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/aeroportos")
public class AeroportoController {
  private final AeroportoRepository repo;
  public AeroportoController(AeroportoRepository repo) { this.repo = repo; }

  @GetMapping
  public List<Aeroporto> listar() { return repo.findAll(); }

  @GetMapping("/{iata}")
  public ResponseEntity<Aeroporto> porIata(@PathVariable("iata") String iata) {
    return repo.findByIata(iata).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<Aeroporto> porId(@PathVariable("id") Long id) {
    return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<?> criar(@RequestBody @Valid Aeroporto body) {
    if (body.getIata() == null || body.getIata().isBlank()) {
      return ResponseEntity.badRequest().body("Campo iata é obrigatório");
    }
    if (body.getId() == null) {
      body.setId(repo.nextId());
    }
    Aeroporto salvo = repo.save(body);
    return ResponseEntity.created(URI.create("/api/v1/aeroportos/id/" + salvo.getId())).body(salvo);
  }

  @PutMapping("/{iata}")
  public ResponseEntity<?> atualizarPorIata(@PathVariable("iata") String iata, @RequestBody @Valid Aeroporto body) {
    return repo.findByIata(iata).map(a -> {
      merge(a, body);
      repo.save(a);
      return ResponseEntity.ok("Aeroporto atualizado");
    }).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/id/{id}")
  public ResponseEntity<?> atualizarPorId(@PathVariable("id") Long id, @RequestBody @Valid Aeroporto body) {
    return repo.findById(id).map(a -> {
      merge(a, body);
      repo.save(a);
      return ResponseEntity.ok("Aeroporto atualizado");
    }).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{iata}")
  public ResponseEntity<?> excluirPorIata(@PathVariable("iata") String iata) {
    return repo.findByIata(iata).map(a -> { repo.delete(a); return ResponseEntity.noContent().build(); })
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/id/{id}")
  public ResponseEntity<?> excluirPorId(@PathVariable("id") Long id) {
    return repo.findById(id).map(a -> { repo.delete(a); return ResponseEntity.noContent().build(); })
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private void merge(Aeroporto a, Aeroporto b) {
    if (b.getName() != null) a.setName(b.getName());
    if (b.getCity() != null) a.setCity(b.getCity());
    if (b.getCountry() != null) a.setCountry(b.getCountry());
    if (b.getIata() != null) a.setIata(b.getIata());
    if (b.getIcao() != null) a.setIcao(b.getIcao());
    if (b.getLatitude() != null) a.setLatitude(b.getLatitude());
    if (b.getLongitude() != null) a.setLongitude(b.getLongitude());
    if (b.getAltitude() != null) a.setAltitude(b.getAltitude());
    if (b.getTz_offset() != null) a.setTz_offset(b.getTz_offset());
    if (b.getDst() != null) a.setDst(b.getDst());
    if (b.getTz() != null) a.setTz(b.getTz());
    if (b.getType() != null) a.setType(b.getType());
    if (b.getSource() != null) a.setSource(b.getSource());
  }
}