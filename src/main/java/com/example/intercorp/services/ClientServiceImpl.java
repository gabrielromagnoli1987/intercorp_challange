package com.example.intercorp.services;

import com.example.intercorp.dto.ClientDetailDto;
import com.example.intercorp.dto.ClientDto;
import com.example.intercorp.dto.KpiClientsDto;
import com.example.intercorp.models.Client;
import com.example.intercorp.repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Value("${averageAgeOfDeath}")
    private int averageAgeOfDeath;

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
        return clientRepository.findAll().collectList().flatMap(
                ClientServiceImpl::calculateStandardDeviationAndAverageAge
        );
    }

    private static Mono<KpiClientsDto> calculateStandardDeviationAndAverageAge(List<Client> clients) {
        double sum = 0.0;
        int length = clients.size();
        for(Client client : clients) {
            sum += client.getAge().getYears();
        }
        double averageAge = sum / length;
        double standardDeviation = 0.0;
        for(Client client : clients) {
            standardDeviation += Math.pow(client.getAge().getYears() - averageAge, 2);
        }
        standardDeviation = Math.sqrt(standardDeviation / length);
        return Mono.just(new KpiClientsDto(averageAge, standardDeviation));
    }

    private LocalDate calculateEstimatedDateOfDeath(Client client) {
        Period averageAgeOfDeath = Period.of(this.averageAgeOfDeath, 0, 0);
        return client.getBirthDate().plus(averageAgeOfDeath);
    }

}
