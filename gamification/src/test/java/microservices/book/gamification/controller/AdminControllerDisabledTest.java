package microservices.book.gamification.controller;

import microservices.book.gamification.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerDisabledTest {

    @MockBean
    private AdminService adminService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void deleteDatabaseTest() throws Exception {
        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get("/gamification/admin/delete-db")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verifyZeroInteractions(adminService);
    }
}
