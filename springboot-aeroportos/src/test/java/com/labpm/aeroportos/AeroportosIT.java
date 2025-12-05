package com.labpm.aeroportos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AeroportosIT {

  @Autowired
  MockMvc mvc;

  @Test
  void listaAeroportos() throws Exception {
    mvc.perform(get("/api/v1/aeroportos")).andExpect(status().isOk());
  }

  @Test
  void buscaPorIata() throws Exception {
    mvc.perform(get("/api/v1/aeroportos/GKA")).andExpect(status().isOk());
  }
}