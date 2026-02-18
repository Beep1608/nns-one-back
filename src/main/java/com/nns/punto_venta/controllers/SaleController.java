package com.nns.punto_venta.controllers;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nns.punto_venta.dtos.sales.SaleRequestDto;
import com.nns.punto_venta.dtos.sales.SaleResponseDto;
import com.nns.punto_venta.services.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sales")
@Tag(name="Ventas", description="Operaciones relacionadas con las ventas")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Listar todas las ventas: GET http://localhost:8080/api/sales
    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Devuelve una lista de todas las ventas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente", content=@Content(mediaType="application/json", schema=@Schema(implementation=SaleResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<List<SaleResponseDto>> getAll() {
        return ResponseEntity.ok(saleService.findAll());
    }


    // Venta con descuento de stock: POST http://localhost:8080/api/sales/process
    @PostMapping("/process")
    @Operation(summary = "Procesar una venta con descuento de stock", description = "Procesa una venta de un producto, aplicando el descuento de stock correspondiente.")
    @ApiResponse(responseCode = "201", description = "Venta procesada exitosamente", content=@Content(mediaType="application/json", schema=@Schema(implementation=SaleResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content=@Content(mediaType="application/json"))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<SaleResponseDto> processSale(
            @RequestBody SaleRequestDto saleRequestDto, 
            @RequestParam Integer quantity) {
        
        
        SaleResponseDto sale = saleService.processProductSale(saleRequestDto, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    // Cancelar una venta: DELETE http://localhost:8080/api/sales/1
    // Nota: Requerimos el productId y quantity para devolver el stock al almacén
    @DeleteMapping("/{id}/cancel")
    @Operation(summary = "Cancelar una venta", description = "Cancela una venta específica del sistema, devolviendo el stock al almacén.")
    @ApiResponse(responseCode = "204", description = "Venta cancelada exitosamente")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content=@Content(mediaType="application/json"))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<Void> cancel(
            @PathVariable Integer id, 
            @RequestParam Integer productId, 
            @RequestParam Integer quantity) {
        
        saleService.cancelSale(id, productId, quantity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar ventas por consulta", description = "Busca ventas que coincidan con una consulta de texto en campos relevantes como el nombre del producto o el nombre del usuario.")
    @ApiResponse(responseCode = "200", description = "Ventas encontradas exitosamente", content=@Content(mediaType="application/json", schema=@Schema(implementation=SaleResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<List<SaleResponseDto>> searchSales(@RequestParam String query) {
        
        List<SaleResponseDto> sales = saleService.searchSalesByQuery(query);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar ventas por múltiples criterios", description = "Filtra las ventas según múltiples criterios como ID de usuario, monto total, estado, fechas de creación o actualización, y si se incluyen ventas eliminadas.")
    @ApiResponse(responseCode = "200", description = "Ventas filtradas exitosamente", content=@Content(mediaType="application/json", schema=@Schema(implementation=SaleResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<List<SaleResponseDto>> filterSales(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) BigDecimal totalAmount,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedDate,
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
            
        List<SaleResponseDto> results = saleService.findByAdvancedFilters(
                userId, totalAmount, status, createdDate, updatedDate, includeDeleted);
                
        return ResponseEntity.ok(results);
    }
}