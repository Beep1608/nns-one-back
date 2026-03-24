package com.nns.punto_venta.modules.stores.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nns.punto_venta.modules.stores.dtos.StoreRequestDto;
import com.nns.punto_venta.modules.stores.dtos.StoreResponseDto;
import com.nns.punto_venta.modules.stores.services.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stores")
@Tag(name = "Sucursales", description = "Operaciones relacionadas con las sucursales (tiendas)")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las sucursales", description = "Devuelve una lista de todas las sucursales del tenant actual.")
    @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente")
    public ResponseEntity<List<StoreResponseDto>> getAll() {
        return ResponseEntity.ok(storeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una sucursal por ID", description = "Busca y devuelve los detalles de una sucursal específica.")
    @ApiResponse(responseCode = "200", description = "Sucursal encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    public ResponseEntity<StoreResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sucursal", description = "Crea una sucursal. Solo para administradores.")
    @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente")
    @ApiResponse(responseCode = "403", description = "Acceso denegado")
    public ResponseEntity<StoreResponseDto> create(@Valid @RequestBody StoreRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal", description = "Modifica los datos de una sucursal existente.")
    @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    public ResponseEntity<StoreResponseDto> update(@PathVariable Long id, @Valid @RequestBody StoreRequestDto dto) {
        return ResponseEntity.ok(storeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sucursal", description = "Elimina físicamente una sucursal del sistema.")
    @ApiResponse(responseCode = "200", description = "Sucursal eliminada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Sucursal eliminada correctamente\"}")))
    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        storeService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sucursal eliminada correctamente");
        return ResponseEntity.ok(response);
    }
}
