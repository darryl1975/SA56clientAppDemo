package sg.edu.nus.iss.demoapp.demo;

// import org.apache.catalina.security.SecurityConfig;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import sg.edu.nus.iss.demoapp.demo.configuration.WebSecurityConfig;
import sg.edu.nus.iss.demoapp.demo.controller.HomeController;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.when;
// import static org.mockito.Mockito.verify;
// import static org.springframework.http.HttpHeaders.ACCEPT;
// import static org.springframework.http.MediaType.APPLICATION_JSON;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
// import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
// import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Deprecated
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
// @Import(WebSecurityConfig.class)
// @WebMvcTest(HomeController.class)
public class HomeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Before
        public void setup() {
                // this.mockMvc = MockMvcBuilders.standaloneSetup(new HomeController()).build();
                this.mockMvc = MockMvcBuilders
                                .standaloneSetup(new HomeController())
                                .apply(springSecurity())
                                .alwaysDo(print())
                                .build();
        }

        @Test
        public void loginAvailableForAll() throws Exception {
                mockMvc
                                .perform(get("/login"))
                                .andExpect(status().isOk());
        }

        @Test
        public void adminCanLog() throws Exception {
                this.mockMvc
                                .perform(formLogin().user("admin").password("admin"))
                                .andExpect(status().isFound());
                // .andExpect(redirectedUrl("/welcome"))
                // .andExpect(authenticated().withUsername("admin"));

                this.mockMvc
                                .perform(logout())
                                .andExpect(status().isFound())
                                .andExpect(redirectedUrl("/login?logout"));
        }

        @Test
        public void shouldAllowAccessForAnonymousUser() throws Exception {
                // this.mockMvc
                // .perform(get("/home"))
                // .andExpect(status().isOk())
                // .andExpect(status().is2xxSuccessful());
                // .andExpect(view().name("home"));

                this.mockMvc
                                .perform(MockMvcRequestBuilders.get("/home"))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @WithMockUser(username = "darrylng")
        @Test
        public void shouldAllowAccessForAuthenticatedUserToWelcomePage() throws Exception {
                this.mockMvc
                                .perform(get("/welcome"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("welcomePage"));
        }

        @Test
        public void shouldRedirectAnonymousUserToLogin() throws Exception {
                this.mockMvc
                                .perform(get("/admin"))
                                .andExpect(status().is3xxRedirection());
        }

        @WithMockUser(username = "ram", password = "ram123", authorities = "Admin")
        @Test
        public void shouldAllowAccessMemberListingPage() throws Exception {
                this.mockMvc
                                .perform(get("/memberlisting"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("index"));
        }

        @WithMockUser(username = "raven", password = "raven123", authorities = "Employee")
        @Test
        public void shouldNotAllowAccessMemberListingPage() throws Exception {
                this.mockMvc
                                .perform(get("/memberlisting"))
                                .andExpect(status().is4xxClientError());
        }

        @WithMockUser(username = "ram", password = "ram123", authorities = "Admin")
        @Test
        public void shouldAllowAccessForAuthenticatedUserToAdminPage() throws Exception {
                // this.mockMvc
                // .perform(get("/admin")
                // .with(SecurityMockMvcRequestPostProcessors.user("ram").password("ram123").roles("Admin"))
                // .with(csrf()))
                // .andExpect(status().isOk());

                // GrantedAuthority authority = new SimpleGrantedAuthority("Admin");

                // this.mockMvc
                // .perform(get("/admin")
                // .with(SecurityMockMvcRequestPostProcessors.user("ram").password("ram123").authorities(authority))
                // .with(csrf()))
                // .andExpect(status().isOk());

                this.mockMvc
                                .perform(get("/admin"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("adminPage"));
        }

        @WithMockUser(username = "ravan", password = "ravan123", authorities = "Employee")
        @Test
        public void shouldAllowAccessForAuthenticatedUserToEmployeePage() throws Exception {
                this.mockMvc
                                .perform(get("/emp"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("empPage"));
        }

        @Test
        public void shouldShowCreateUserPage() throws Exception {
                this.mockMvc
                                .perform(get("/addnew"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("newmember"))
                                .andExpect(model().attributeExists("memberForm"))
                                .andExpect(model().attributeExists("roleList"));

        }

        @Test
        public void shouldCreateNewUser() throws Exception {
                this.mockMvc
                                .perform(post("/save")
                                                .param("fullName", "duke")
                                                .param("password", "C0124")
                                                .param("email", "duke@java.io")
                                                .param("mobilePhone", "91234567")
                                                .param("birthDay", "1")
                                                .param("birthMonth", "1")
                                                .param("postalCode", "123456"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(header().string("Location", "/memberlisting"));
                // .andExpect(header().string("Location", "http://localhost/login"));
        }
}
