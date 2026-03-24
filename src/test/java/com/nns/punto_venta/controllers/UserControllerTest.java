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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nns.punto_venta.modules.security.assemblers.UserModelAssembler;
import com.nns.punto_venta.modules.security.services.JwtService;
import com.nns.punto_venta.modules.tenant.services.TenantUserDetailImpl;
import com.nns.punto_venta.modules.security.repositories.RoleRepository;
import com.nns.punto_venta.modules.security.controllers.UserController;
import com.nns.punto_venta.modules.security.dtos.UserRequestDto;
import com.nns.punto_venta.modules.security.dtos.UserResponseDto;
import com.nns.punto_venta.modules.security.dtos.UserUpdateRequestDto;
import com.nns.punto_venta.modules.security.entities.UserEntity;
import com.nns.punto_venta.modules.security.services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@DisplayName("Test Unitarios de UserController")
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserModelAssembler userAssembler;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private TenantUserDetailImpl tenantUserDetailImpl;

    @MockitoBean
    private RoleRepository roleRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserService userService, UserModelAssembler userAssembler, JwtService jwtService, TenantUserDetailImpl tenantUserDetailImpl, RoleRepository roleRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.jwtService = jwtService;
        this.tenantUserDetailImpl = tenantUserDetailImpl;
        this.roleRepository = roleRepository;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("Debe retornar 200 y formato HAL con enlaces al consultar GET /api/users")
    void getAll_Success() throws Exception {
        // 1. IMPORTANTE: El servicio ahora devuelve ENTIDADES
        UserEntity user1 = new UserEntity();
        user1.setId(1);
        user1.setUsername("Minion");

        UserEntity user2 = new UserEntity();
        user2.setId(2);
        user2.setUsername("Fidel");

        List<UserEntity> entities = List.of(user1, user2);
        Page<UserEntity> userPage = new PageImpl<>(entities, PageRequest.of(0, 10), 2);

        // Mockeamos el servicio
        when(userService.findAll(any(Pageable.class))).thenReturn(userPage);
        
        // Mockeamos el assembler para evitar NPE en HATEOAS
        when(userAssembler.toModel(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            return new UserResponseDto(entity.getId(), entity.getUsername());
        });

        // 3. Ejecución y validación de la estructura HAL
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))

                .andExpect(jsonPath("$._embedded.users.size()").value(2))
                .andExpect(jsonPath("$._embedded.users[0].username").value("Minion"));
    }

    @Test
    @DisplayName("Debe retornar 200 y el usuario con el id especificado al consultar GET /api/users/{id}")
    void getById_Success() throws Exception {
        UserResponseDto user = new UserResponseDto(1, "Minion");

        when(userService.findById(1)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Minion"));
    }

    @Test
    @DisplayName("Debe retornar 201 Created y el usuario creado al enviar un usuario válido")
    void create_Success() throws Exception {

        when(roleRepository.findByName(any())).thenReturn(java.util.Optional.of(new com.nns.punto_venta.modules.security.entities.RoleEntity()));

        UserResponseDto response = new UserResponseDto(1, "Minion");
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(response);

        // Enviamos como parámetros para que @ParameterObject los capture
        // password "123456" para cumplir con min=6
        mockMvc.perform(post("/api/users")
                .param("username", "Minion")
                .param("password", "123456")
                .param("role", "Admin")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Minion"))
                .andDo(print());
    }

    @Test
    @DisplayName("Debe retornar 200 OK al actualizar un usuario existente")
    void update_Success() throws Exception {

        Integer userId = 1;
        UserRequestDto request = new UserRequestDto("Minion Actualizado");
        UserResponseDto response = new UserResponseDto(userId, "Minion Actualizado");

        when(userService.updateUser(eq(userId), any(UserUpdateRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("Minion Actualizado"))
                .andDo(print());
    }

    @Test
    @DisplayName("Debe retornar 204 No Content al eliminar un usuario")
    void delete_Success() throws Exception {

        Integer userId = 1;

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }
}
