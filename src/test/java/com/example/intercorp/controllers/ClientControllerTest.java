package com.example.intercorp.controllers;

import com.example.intercorp.dto.KpiClientsDto;
import com.example.intercorp.services.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private ClientService clientService;

    @Test
    void getKpiClients() {

        double expectedAverageAge = 64.0;
        double expectedStandardDeviation = 9.565563234854496;
        KpiClientsDto kpiClientsDto = new KpiClientsDto(expectedAverageAge, expectedStandardDeviation);

        Mockito.when(clientService.getKpiClients()).thenReturn(Mono.just(kpiClientsDto));

        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + this.port).build();
        webTestClient.get()
                .uri("/kpi-clients")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("$.averageAge").isEqualTo(expectedAverageAge)
                .jsonPath("$.standardDeviation").isEqualTo(expectedStandardDeviation);

    }
}