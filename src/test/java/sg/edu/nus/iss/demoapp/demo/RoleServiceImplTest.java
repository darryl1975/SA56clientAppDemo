package sg.edu.nus.iss.demoapp.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
// import org.hamcrest.collection.IsArrayWithSize;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
// import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import sg.edu.nus.iss.demoapp.demo.model.Role;
import sg.edu.nus.iss.demoapp.demo.service.RoleServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
// import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
// import static org.mockito.ArgumentMatchers.isNotNull;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    private final MockWebServer mockWebServer = new MockWebServer();

    private final RoleServiceImpl roleServiceImpl = new RoleServiceImpl(mockWebServer.url("localhost/").toString());

    @Autowired
    @InjectMocks
    private RoleServiceImpl roleService;
    private Role role1;
    private Role role2;
    List<Role> roleList;
    
    @Before
    public void setUp() {
        roleList = new ArrayList<>();
        role1 = new Role();
        role1.setId((long) 1);
        role1.setDescription("Tester 1");
        role1.setRoleName("Tester1");
        role2 = new Role();
        role2.setId((long) 2);
        role2.setDescription("Tester 2");
        role2.setRoleName("Tester2");
        roleList.add(role1);
        roleList.add(role2);
    }


    @After
    public void tearDown() throws IOException {
        role1 = role2 = null;
        roleList = null;

        mockWebServer.shutdown();
    }

    @Test
    public void shouldReturnRole() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"id\": 1, \"roleName\": \"Road Bike\"}"));

        Role response = roleServiceImpl.findAll().get(0);
        assertThat(response.getId(), is(equalTo(1L)));
        assertThat(response.getRoleName(), is(equalTo("Road Bike")));
        System.out.println(response);
        assertThat(response.getRoleName(), is(equalTo("Road Bike")));

    }

    @Test
    public void testGetAll() {
        mockWebServer.enqueue(
            new MockResponse()
                    .setResponseCode(200)
                    .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .setBody("{\"id\": 1, \"roleName\": \"Road Bike\"}"));

        List<Role> lstRole = roleServiceImpl.findAll();

        assertTrue(lstRole.size() > 0);
    }

    @Test
    public void testCreate() {
        mockWebServer.enqueue(
            new MockResponse()
                    .setResponseCode(200)
                    .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .setBody("{\"id\": 1, \"roleName\": \"Road Bike\"}"));

        final Role roleToCreate = new Role();
		roleToCreate.setId(2L);
		roleToCreate.setRoleName("Triangle");
		roleToCreate.setDescription("Triangle for B, 3, P");

        Role roleCreated = roleServiceImpl.create(roleToCreate);

        
        List<Role> lstRole = roleServiceImpl.findAll();

        assertTrue(roleCreated.getId() > 0);
        assertTrue(lstRole.size() > 1);
    }
}
