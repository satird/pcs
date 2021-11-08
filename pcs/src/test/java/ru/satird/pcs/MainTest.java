package ru.satird.pcs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MainTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDefaultMessageTest() throws Exception {
        this.mockMvc.perform(get("/api/auth/"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("")));
    }

    @Test
    void shouldRequestAuthorizationTest() throws Exception {
        this.mockMvc.perform(get("/rest/api/ad"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void correctSignInTest() throws Exception {
        this.mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\": \"gitpushoriginmain@gmail.com\",\n" +
                        "    \"password\": \"123\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("gitpushoriginmain@gmail.com"));
    }

    @Test
    void notCorrectSignInTest() throws Exception {
        this.mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\": \"gitpushoriginmain@gmail.com\",\n" +
                        "    \"password\": \"122\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"));
    }

//    @Test
//    void correctStatusWhenGetAdList() throws Exception {
//        this.mockMvc.perform(get("/rest/api/ad")
//                .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b3VAbWFpbC5ydSIsImlhdCI6MTYzNjM4MDMzMiwiZXhwIjoxNjM2NjM5NTMyfQ.pMuY2Jj3VEBX5yhB9I-_Zu0Tjhxcjr91m9i91JVn0Pe-0bkF5lvXJdNw0Q2nSYWiEZar47V1FKwH02hPvxbidQ"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    void correctStatusAndAmountWhenShowAllUserTest() throws Exception {
//        this.mockMvc.perform(get("/rest/api/user")
//                .with(user("root").roles("ADMIN")))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(9));
//    }
}
