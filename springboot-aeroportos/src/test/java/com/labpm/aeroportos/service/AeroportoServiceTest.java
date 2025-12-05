package com.labpm.aeroportos.service;

import com.labpm.aeroportos.domain.Aeroporto;
import com.labpm.aeroportos.repository.AeroportoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AeroportoServiceTest {

  @Mock
  AeroportoRepository repo;

  @InjectMocks
  AeroportoService service;

  @Test
  void buscarPorIata_inexistente_disparaExcecao() {
    when(repo.findByIata("XXX")).thenReturn(java.util.Optional.empty());
    assertThrows(AeroportoNaoEncontradoException.class, () -> service.buscarPorIata("XXX"));
  }

  @Test
  void salvar_iataInvalido_rejeita() {
    Aeroporto a = new Aeroporto();
    a.setIata("ABCD");
    assertThrows(IllegalArgumentException.class, () -> service.salvar(a));
  }

  @Test
  void salvar_altitudeNegativa_rejeita() {
    Aeroporto a = new Aeroporto();
    a.setIata("ABC");
    a.setAltitude(-1);
    assertThrows(IllegalArgumentException.class, () -> service.salvar(a));
  }
}