package com.nns.punto_venta.controllers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nns.punto_venta.dtos.users.UserRequestDto;
import com.nns.punto_venta.dtos.users.UserResponseDto;
import com.nns.punto_venta.services.UserService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@DisplayName("Test Unitarios de UserController")
public class UserControllerTest {
    
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;

    private ObjectMapper objectMapper;
    
    @Autowired
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
            .andExpect(jsonPath("$.size()").value(3))
            .andExpect(jsonPath("$[0].username").value("Minion"));
     
    }


    @Test
    @DisplayName("Debe retornar 200 y el usuario con el id especificado al consultar GET /api/users/{id}")
    void getById_Success() throws Exception
    {
        UserResponseDto user = new UserResponseDto(1, "Minion");

        when(userService.findById(1)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("Minion"));
    }

    @Test
    @DisplayName("Debe retornar 201 Created y el usuario creado al enviar un usuario válido")
    void create_Success() throws Exception {
   
        UserRequestDto request = new UserRequestDto("Minion","12345"); 
        UserResponseDto response = new UserResponseDto(1, "Minion"); 

       //Nota: Usamos any() porque el objeto que recibe el controlador y 
       // el que le llega al servicio no son técnicamente la misma instancia de memoria (Spring crea uno nuevo al deserializar el JSON). 
       // Si pusieras request directamente en el when(), el test podría fallar porque Mockito compararía las direcciones de memoria.
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(response);

       
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON) // Indicamos que enviamos JSON
                .content(objectMapper.writeValueAsString(request))) // Convertimos el DTO a String JSON

     
                .andExpect(status().isCreated()) 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Minion"))
                .andDo(print()); // Para que veas en consola qué pasó
    }
    
    @Test
    @DisplayName("Debe retornar 200 OK al actualizar un usuario existente")
    void update_Success() throws Exception {
       
        Integer userId = 1;
        UserRequestDto request = new UserRequestDto("Minion Actualizado");
        UserResponseDto response = new UserResponseDto(userId, "Minion Actualizado");

        // Configuramos el Mock
        // Usamos eq(userId) para asegurar que el ID sea exactamente 1
        // Usamos any() para el DTO porque Spring creará una instancia nueva al recibir el JSON
        when(userService.updateUser(eq(userId), any(UserRequestDto.class))).thenReturn(response);


        mockMvc.perform(put("/api/users/{id}", userId) // Notar el /{id}
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Cuerpo de la petición

   
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("Minion Actualizado"))
                .andDo(print());
    }

    @Test
    @DisplayName("Debe retornar 204 No Content al eliminar un usuario")
    void delete_Success() throws Exception {

        Integer userId = 1;
        
        // Como deleteUser es void, Mockito por defecto no hace nada (doNothing).
        // No hace falta poner un 'when' a menos que quieras lanzar una excepción.
    

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent()); // Verifica el código 204
    

        // Verificamos que el controlador llamó al método deleteUser del servicio exactamente 1 vez
        verify(userService, times(1)).deleteUser(userId);
    }
}
