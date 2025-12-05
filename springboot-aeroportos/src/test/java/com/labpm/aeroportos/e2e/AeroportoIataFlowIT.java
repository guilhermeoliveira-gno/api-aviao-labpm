package com.labpm.aeroportos.e2e;

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
@ActiveProfiles("mysql-it")
class AeroportoIataFlowIT {

  @Autowired
  MockMvc mvc;

  @Test
  void fluxoCompletoPorIataMysql() throws Exception {
    String iata = pickIata();
    long id = pickId();

    mvc.perform(post("/api/v1/aeroportos")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\":" + id + ",\"name\":\"Teste Airport\",\"city\":\"Teste City\",\"country\":\"Teste Country\",\"iata\":\"" + iata + "\"}"))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.iata").value(iata));

    mvc.perform(get("/api/v1/aeroportos/" + iata))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.city").value("Teste City"));

    mvc.perform(put("/api/v1/aeroportos/" + iata)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"city\":\"Atualizada\"}"))
      .andExpect(status().isOk());

    mvc.perform(get("/api/v1/aeroportos/" + iata))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.city").value("Atualizada"));

    mvc.perform(delete("/api/v1/aeroportos/" + iata))
      .andExpect(status().isNoContent());

    mvc.perform(get("/api/v1/aeroportos/" + iata))
      .andExpect(status().isNotFound());
  }

  private String pickIata() throws Exception {
    String[] c = new String[]{"QQQ","ZZZ","XXX","TST","QWX","QWV","QAZ","QPL"};
    for (String s : c) {
      int st = mvc.perform(get("/api/v1/aeroportos/" + s)).andReturn().getResponse().getStatus();
      if (st == 404) return s;
    }
    return "QQQ";
  }

  private long pickId() throws Exception {
    long[] ids = new long[]{9000001L,9000002L,9000003L,9000004L,9000005L};
    for (long x : ids) {
      int st = mvc.perform(get("/api/v1/aeroportos/id/" + x)).andReturn().getResponse().getStatus();
      if (st == 404) return x;
    }
    return 9000001L;
  }
}