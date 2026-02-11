package com.nns.punto_venta.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.nns.punto_venta.dtos.users.UserResponseDto;
import com.nns.punto_venta.services.UserService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@DisplayName("Test Unitarios de UserController")
public class UserControllerTest {
    
    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper;
    public UserControllerTest(MockMvc mockMvc, UserService userService, ObjectMapper objectMapper)
    {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("Debe retornar 200 y lista de usuarios al consultar GET /api/users")
    void getAll_Success() throws Exception
    {
        var users = List.of(
            new UserResponseDto(1, "Minion"),
            new UserResponseDto(2, "Fidel"),
            new UserResponseDto(3, "Chetos")
        );

        when(userService.findAll()).thenReturn(users);

        
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].nombre").value("Minion"));
     
    }
}
