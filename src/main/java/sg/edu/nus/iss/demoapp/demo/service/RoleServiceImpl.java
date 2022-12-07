package sg.edu.nus.iss.demoapp.demo.service;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sg.edu.nus.iss.demoapp.demo.model.Role;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    @Autowired
    WebClient webClient;

    public RoleServiceImpl(@Value("${content-service}") String baseURL) {
        this.webClient = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();
    }

    @Override
    public List<Role> findAll() {
        Flux<Role> retrievedRoleList = webClient.get()
                .uri("/roles/")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Role.class);
                    } else {
                        return response.createException().flatMapMany(Flux::error);
                    }
                });

        return retrievedRoleList.collectList().block();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            LOGGER.info("Request: {} {}" + clientRequest.method() + clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> LOGGER.info("{}={}" + name + value)));
            return next.exchange(clientRequest);
        };
    }

    @Override
    public Role findById(Long id) {
        Mono<Role> retrievedRole = webClient.get()
                .uri("/roles/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(Role.class);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });
        return retrievedRole.block();
    }

    @Override
    public Role create(Role rol) {
        Mono<Role> createdRole = webClient.post()
                .uri("/roles")
                .body(Mono.just(rol), Role.class)
                .retrieve()
                .bodyToMono(Role.class)
                .timeout(Duration.ofMillis(10_000));
        return createdRole.block();
    }

    @Override
    public Role update(Role rol) {
        Mono<Role> updatedRole = webClient.put()
                .uri("/roles/")
                .body(Mono.just(rol), Role.class)
                .retrieve()
                .bodyToMono(Role.class);
        return updatedRole.block();
    }

    @Override
    public Long delete(Long id) {
        Mono<Long> deletedRole = webClient.delete()
                .uri("/roles/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    return Mono.error(NotFoundException::new);
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    return Mono.error(UnknownError::new);
                })
                .bodyToMono(Long.class)
                .onErrorComplete();
        return deletedRole.block();
    }
}
