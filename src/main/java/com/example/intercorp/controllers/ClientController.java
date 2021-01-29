package com.example.intercorp.controllers;

import com.example.intercorp.dto.ClientDetailDto;
import com.example.intercorp.dto.ClientDto;
import com.example.intercorp.dto.KpiClientsDto;
import com.example.intercorp.models.Client;
import com.example.intercorp.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    public Mono<Client> createClient(@RequestBody ClientDto clientDto) {
        return this.clientService.createClient(clientDto);
    }

    @GetMapping("/clients")
    public Flux<ClientDetailDto> getClients() {
        return this.clientService.getClients();
    }

    @GetMapping("/kpi-clients")
    public Mono<KpiClientsDto> getKpiClients() {
        return this.clientService.getKpiClients();
    }
}
