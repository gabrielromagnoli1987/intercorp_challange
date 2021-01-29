package com.example.intercorp.services;

import com.example.intercorp.dto.ClientDetailDto;
import com.example.intercorp.dto.ClientDto;
import com.example.intercorp.dto.KpiClientsDto;
import com.example.intercorp.models.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {

    Mono<Client> createClient(ClientDto clientDto);

    Flux<ClientDetailDto> getClients();

    Mono<KpiClientsDto> getKpiClients();
}
