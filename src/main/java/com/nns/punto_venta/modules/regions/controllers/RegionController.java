package com.nns.punto_venta.modules.regions.controllers;

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

import com.nns.punto_venta.modules.regions.dtos.RegionRequestDto;
import com.nns.punto_venta.modules.regions.dtos.RegionResponseDto;
import com.nns.punto_venta.modules.regions.services.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/regions")
@Tag(name = "Regiones", description = "Operaciones relacionadas con las regiones geográficas")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las regiones", description = "Devuelve una lista de todas las regiones del tenant actual.")
    @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida exitosamente")
    public ResponseEntity<List<RegionResponseDto>> getAll() {
        return ResponseEntity.ok(regionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una región por ID", description = "Busca y devuelve los detalles de una región específica.")
    @ApiResponse(responseCode = "200", description = "Región encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Región no encontrada")
    public ResponseEntity<RegionResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva región", description = "Crea una región geográfica. Solo para administradores.")
    @ApiResponse(responseCode = "201", description = "Región creada exitosamente")
    @ApiResponse(responseCode = "403", description = "Acceso denegado")
    public ResponseEntity<RegionResponseDto> create(@Valid @RequestBody RegionRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una región", description = "Modifica los datos de una región existente.")
    @ApiResponse(responseCode = "200", description = "Región actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Región no encontrada")
    public ResponseEntity<RegionResponseDto> update(@PathVariable Long id, @Valid @RequestBody RegionRequestDto dto) {
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una región", description = "Elimina físicamente una región del sistema.")
    @ApiResponse(responseCode = "200", description = "Región eliminada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Región eliminada correctamente\"}")))
    @ApiResponse(responseCode = "404", description = "Región no encontrada")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        regionService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Región eliminada correctamente");
        return ResponseEntity.ok(response);
    }
}
