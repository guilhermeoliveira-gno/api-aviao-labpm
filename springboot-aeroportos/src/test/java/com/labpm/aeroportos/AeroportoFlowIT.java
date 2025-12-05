package com.labpm.aeroportos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
class AeroportoFlowIT {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper om;

  @Test
  void fluxoCompletoPorIata() throws Exception {
    String iata = "TST";
    String body = "{" +
      "\"name\":\"Teste Airport\"," +
      "\"city\":\"Teste City\"," +
      "\"country\":\"Teste Country\"," +
      "\"iata\":\"" + iata + "\"" +
      "}";

    mvc.perform(post("/api/v1/aeroportos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.iata").value(iata));

    mvc.perform(get("/api/v1/aeroportos/" + iata))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.city").value("Teste City"));

    String upd = "{" +
      "\"city\":\"Atualizada\"" +
      "}";
    mvc.perform(put("/api/v1/aeroportos/" + iata)
        .contentType(MediaType.APPLICATION_JSON)
        .content(upd))
      .andExpect(status().isOk());

    mvc.perform(get("/api/v1/aeroportos/" + iata))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.city").value("Atualizada"));

    mvc.perform(delete("/api/v1/aeroportos/" + iata))
      .andExpect(status().isNoContent());

    mvc.perform(get("/api/v1/aeroportos/" + iata))
      .andExpect(status().isNotFound());
  }
}