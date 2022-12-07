package sg.edu.nus.iss.demoapp.demo.service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sg.edu.nus.iss.demoapp.demo.model.Member;
import sg.edu.nus.iss.demoapp.demo.model.Role;

@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    @Autowired
    WebClient webClient;

    public MemberServiceImpl(@Value("${content-service}") String baseURL) {
        this.webClient = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .build();
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
    public List<Member> findAll() {
        Flux<Member> retrievedMemberList = webClient.get()
                .uri("/members")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Member.class);
                    } else {
                        return response.createException().flatMapMany(Flux::error);
                    }
                });

        return retrievedMemberList.collectList().block();
    }

    @Override
    public Member findById(Long id) {
        Mono<Member> retrievedMember = webClient.get()
                .uri("/members/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(Member.class);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });
        return retrievedMember.block();
    }

    @Override
    public Member create(Member mem) {
        Mono<Member> createdMember = webClient.post()
                .uri("/members")
                .body(Mono.just(mem), Member.class)
                .retrieve()
                .bodyToMono(Member.class)
                .timeout(Duration.ofMillis(10_000));
        return createdMember.block();
    }

    @Override
    public Member update(Member mem) {
        Mono<Member> updatedMember = webClient.put()
                .uri("/members")
                .body(Mono.just(mem), Member.class)
                .retrieve()
                .bodyToMono(Member.class);
        return updatedMember.block();
    }

    @Override
    public Long delete(Long id) {
        Mono<Long> deletedMember = webClient.delete()
                .uri("/members/" + id)
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
        return deletedMember.block();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Mono<Member> retrievedMember = webClient.get()
                .uri("/members/email/" + email)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(Member.class);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });

        org.springframework.security.core.userdetails.User springUser = null;

        if (retrievedMember == null) {
            // throw new UsernameNotFoundException("User with email: " +email +" not
            // found");

            LOGGER.info("loadUserByUsername: no member found");
        } else {
            Member returnedMember = retrievedMember.block();

            List<Role> roles = returnedMember.getRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
			for(Role role : roles) {
				ga.add(new SimpleGrantedAuthority(role.getRoleName()));
			}

            springUser = new org.springframework.security.core.userdetails.User(
							email,
							returnedMember.getPassword(), ga);
        }

        return springUser;
    }

}
