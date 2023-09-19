package com.NoviBackend.WalletWatch.integrationtests;

import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.user.professional.ProfUserRepository;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.wallet.Wallet;
import com.NoviBackend.WalletWatch.wallet.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ProfUserRepository profUserRepository;


    @Test
    public void createUserTest() throws Exception{
        String userJson = "{\"username\": \"wouter\"," +
                "\"password\": \"passssss\"," +
                "\"firstName\": \"Wouter\"," +
                "\"surname\": \"glassmaker\"," +
                "\"emailAddress\": \"wouterglas@gmail.com\"}";

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header()
                        .string("Location", "http://localhost/users/1"));
    }

    @Test
    public void getProfsPageWithoutLoggedInUser() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/profs"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", password = "verygoodpassword", roles="USER" )
    public void getProfsId() throws Exception{
        createProf();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/profs/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ProfessionalUsersDto prof = objectMapper.readValue(responseBody, ProfessionalUsersDto.class);

        assertEquals("mario", prof.getUsername());
        assertEquals("noviCollege", prof.getCompany());
        assertEquals("i do not work here.", prof.getIntroduction());
    }

    private void createProf(){
        ProfessionalUser prof = new ProfessionalUser("mario", "its", "me",
                "itsme@mario.nl", "noviCollege", "i do not work here.");

        Wallet wallet = new Wallet();

        prof.setPersonalWallet(wallet);
        prof.shareWallet(true);

        walletRepository.save(wallet);
        profUserRepository.save(prof);
    }
}
