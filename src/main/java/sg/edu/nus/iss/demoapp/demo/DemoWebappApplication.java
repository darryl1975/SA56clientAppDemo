package sg.edu.nus.iss.demoapp.demo;

// import java.rmi.ServerException;
// import java.io.Console;
// import java.time.Duration;
// import java.time.LocalDateTime;
// import java.time.OffsetDateTime;
// import java.util.ArrayList;
// import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
// import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.web.reactive.function.client.WebClient;

// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
// import sg.edu.nus.iss.demoapp.demo.model.Member;
// import sg.edu.nus.iss.demoapp.demo.model.Role;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemoWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWebappApplication.class, args);

		// WebClient webClient = WebClient.create("http://localhost:8081/api");

		// Mono<Member> retrievedMember = webClient.get()
		// .uri("/members/" + 7)
		// .accept(MediaType.APPLICATION_JSON)
		// .exchangeToMono(response -> {
		// if (response.statusCode().equals(HttpStatus.OK)) {
		// return response.bodyToMono(Member.class);
		// } else {
		// return response.createException().flatMap(Mono::error);
		// }
		// });

		// System.console().printf("Member Object: ");
		// System.out.print(retrievedMember.block());

		// Mono<Long> deletedMember = webClient.delete()
		// .uri("/members/" + 8)
		// .accept(MediaType.APPLICATION_JSON)
		// .retrieve()
		// .onStatus(HttpStatus:: is4xxClientError, response -> {
		// return Mono.error(NotFoundException::new);
		// })
		// .onStatus(HttpStatus::is5xxServerError, response -> {
		// return Mono.error(UnknownError::new);
		// })
		// .bodyToMono(Long.class)
		// .onErrorComplete();

		// System.console().printf("Deleted Member Object: ");
		// System.out.print(deletedMember.block());

		// Flux<Member> retrievedMemberList = webClient.get()
		// .uri("/members/")
		// .accept(MediaType.APPLICATION_JSON)
		// .exchangeToFlux(response -> {
		// if (response.statusCode().equals(HttpStatus.OK)) {
		// return response.bodyToFlux(Member.class);
		// } else {
		// return response.createException().flatMapMany(Flux::error);
		// }
		// });

		// System.out.println();
		// System.console().printf("Member List Object: ");
		// System.out.print(retrievedMemberList.collectList().block());

		// Member mem = new Member();
		// mem.setBirthDay(5);
		// mem.setBirthMonth(5);
		// mem.setEmail("member1@gmail.com");
		// mem.setDeleted(false);
		// mem.setFullName("Member 1");
		// mem.setMobilePhone("98765432");
		// mem.setPostalCode(123456);
		// mem.setCreatedBy("Darryl");
		// mem.setCreatedTime(OffsetDateTime.now());

		// Mono<Member> createdMember = webClient.post()
		// .uri("/members")
		// .body(Mono.just(mem), Member.class)
		// .retrieve()
		// .bodyToMono(Member.class)
		// .timeout(Duration.ofMillis(10_000));

		// System.out.println();
		// System.console().printf("Created Member Object: ");
		// System.out.print(createdMember.block());

		// /* Update demo - start */
		// // retrieve roles
		// Flux<Role> retrievedRoleList = webClient.get()
		// .uri("/roles/")
		// .accept(MediaType.APPLICATION_JSON)
		// .exchangeToFlux(response -> {
		// if (response.statusCode().equals(HttpStatus.OK)) {
		// return response.bodyToFlux(Role.class);
		// } else {
		// return response.createException().flatMapMany(Flux::error);
		// }
		// });

		// List<Role> roleList = retrievedRoleList.collectList().block();

		// Member mem7 = retrievedMember.block();
		// System.out.println();
		// System.console().printf("Retrieved Member 7 Object: ");
		// System.out.print(mem7);

		// List<Role> memberRoles = new ArrayList<Role>();
		// memberRoles.addAll(mem7.getRoles());
		// memberRoles.add(roleList.get(1));

		// mem7.setBirthDay(12);
		// mem7.setBirthMonth(12);
		// mem7.setMobilePhone("92353535");
		// mem7.setFullName("Member 7");
		// mem7.setEmail("member7@gmail.com");
		// mem7.setRoles(memberRoles);
		// mem7.setMyRoles(memberRoles);

		// Mono<Member> updatedMember = webClient.put()
		// .uri("/members/")
		// .body(Mono.just(mem7), Member.class)
		// .retrieve()
		// .bodyToMono(Member.class);

		// System.out.println();
		// System.console().printf("Updated Member 7 Object: ");
		// System.out.print(updatedMember.block());
		// /* Update demo - end */

		// Role roleToCreate = new Role();
		// roleToCreate.setRoleName("Triangle");
		// roleToCreate.setDescription("Triangle for B, 3, P");

		// Mono<Role> createdRole = webClient.post()
		// 		.uri("/roles")
		// 		.body(Mono.just(roleToCreate), Role.class)
		// 		.retrieve()
		// 		.bodyToMono(Role.class)
		// 		.timeout(Duration.ofMillis(10_000));

		// System.out.println();
		// System.console().printf("Created Role Object: ");
		// System.out.print(createdRole.block());
	}

}
