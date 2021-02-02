package com.example.intercorp.services;

import com.example.intercorp.dto.ClientDetailDto;
import com.example.intercorp.dto.ClientDto;
import com.example.intercorp.models.Client;
import com.example.intercorp.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.Period;

@SpringBootTest
class ClientServiceImplTest {

    @MockBean
    ClientRepository clientRepository;

    @Value("${averageAgeOfDeath}")
    private int averageAgeOfDeath;

    @Test
    void createClient() {
        ClientDto clientDto = new ClientDto("Arnold", "Schwarzenegger", LocalDate.of(1947, 7, 30));
        Client client = new Client();
        BeanUtils.copyProperties(clientDto, client);

        Mockito.when(
                clientRepository.save(client)
        ).thenReturn(Mono.just(client).map(savedClient -> {
            savedClient.setId("1");
            return savedClient;
        }));

        StepVerifier
                .create(clientRepository.save(client))
                .expectSubscription()
                .expectNextMatches(savedClient -> !(savedClient.getId() == null))
                .verifyComplete();
    }

    @Test
    void getClients() {
        Client client1 = new Client("Jason", "Statham", LocalDate.of(1967, 7, 26));
        Client client2 = new Client("Keanu", "Reeves", LocalDate.of(1964, 9, 2));
        Client client3 = new Client("Arnold", "Schwarzenegger", LocalDate.of(1947, 7, 30));
        Client client4 = new Client("Sylvester", "Stallone", LocalDate.of(1946, 7, 6));
        Flux<Client> clientsFlux = Flux.just(client1, client2, client3, client4);
        Period averageAgeOfDeath = Period.of(this.averageAgeOfDeath, 0, 0);

        Mockito.when(
                clientRepository.findAll()
        ).thenReturn(clientsFlux);

        StepVerifier
                .create(clientRepository.findAll().map(
                        client -> {
                            ClientDetailDto clientDetailDto = new ClientDetailDto();
                            BeanUtils.copyProperties(client, clientDetailDto);
                            LocalDate averageDateOfDeath = client.getBirthDate().plus(averageAgeOfDeath);
                            clientDetailDto.setEstimatedDateOfDeath(averageDateOfDeath);
                            return clientDetailDto;
                        })
                )
                .expectSubscription()
                .expectNextMatches(clientDetailDto -> clientDetailDto.getEstimatedDateOfDeath().equals(client1.getBirthDate().plus(averageAgeOfDeath)))
                .expectNextCount(3) // there are 3 more in the stream
                .verifyComplete();
    }

}