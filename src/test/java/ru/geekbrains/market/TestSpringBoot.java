package ru.geekbrains.market;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.market.config.thymeleaf.AppConfig;
import ru.geekbrains.market.config.thymeleaf.ThymeleafSecurityConfig;
import ru.geekbrains.market.entities.Category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(classes = BCryptPasswordEncoder.class)
@ActiveProfiles("thymeleaf")
@Import({AppConfig.class, ThymeleafSecurityConfig.class})
public class TestSpringBoot {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void tryToStart() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "{ADMIN}")
    @Transactional
    public void correctRole() throws Exception {
        mockMvc.perform(get("/catalog/edit/0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = "{USER}")
    @Transactional
    public void badCredentialsRole() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


}
