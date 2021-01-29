package com.example.intercorp.services;

import com.example.intercorp.dto.ClientDetailDto;
import com.example.intercorp.dto.ClientDto;
import com.example.intercorp.dto.KpiClientsDto;
import com.example.intercorp.models.Client;
import com.example.intercorp.repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Mono<Client> createClient(ClientDto clientDto) {
        Client client = new Client();
        BeanUtils.copyProperties(clientDto, client);
        return this.clientRepository.save(client);
    }

    @Override
    public Flux<ClientDetailDto> getClients() {
        Flux<Client> clients = clientRepository.findAll();
        return clients.map(client -> {
            ClientDetailDto clientDetailDto = new ClientDetailDto();
            BeanUtils.copyProperties(client, clientDetailDto);
            LocalDate estimatedDateOfDeath = calculateEstimatedDateOfDeath(client);
            clientDetailDto.setEstimatedDateOfDeath(estimatedDateOfDeath);
            return clientDetailDto;
        });
    }

    @Override
    public Mono<KpiClientsDto> getKpiClients() {
        Flux<Client> clients = clientRepository.findAll();

        return calculateSummaryStatistics(clients).flatMap(doubleSummaryStatistics ->
                calculateStandardDeviation(clients, doubleSummaryStatistics.getAverage(), doubleSummaryStatistics.getCount()).map(
                    standardDeviation -> new KpiClientsDto(doubleSummaryStatistics.getAverage(), standardDeviation)
                )
        );
    }

    private static Mono<DoubleSummaryStatistics> calculateSummaryStatistics(Flux<Client> clients) {
        return clients.collect(Collectors.summarizingDouble(client -> client.getAge().getYears()));
    }

    private Mono<Double> calculateStandardDeviation(Flux<Client> clients, Double averageAge, long count) {
        return clients.collect(
                Collectors.summingDouble(client -> Math.pow(client.getAge().getYears() - averageAge, 2))
        ).map(
                sum -> Math.sqrt(sum / count)
        );
    }

    private LocalDate calculateEstimatedDateOfDeath(Client client) {
        Period averageAgeOfDeath = Period.of(90, 0, 0);
        return client.getBirthDate().plus(averageAgeOfDeath);
    }

}
