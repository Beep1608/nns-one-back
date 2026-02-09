package com.nns.punto_venta.controllers;


import java.util.List;

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

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Listar todas las ventas: GET http://localhost:8080/api/sales
    @GetMapping
    public ResponseEntity<List<SaleResponseDto>> getAll() {
        return ResponseEntity.ok(saleService.findAll());
    }

    // Crear una venta simple: POST http://localhost:8080/api/sales
    @PostMapping
    public ResponseEntity<SaleResponseDto> create(@RequestBody SaleRequestDto saleRequestDto) {
        // Usamos el método que asocia el usuario y guarda el monto
        SaleResponseDto sale = saleService.createSale(saleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    // Venta con descuento de stock: POST http://localhost:8080/api/sales/process
    @PostMapping("/process")
    public ResponseEntity<SaleResponseDto> processSale(
            @RequestBody SaleRequestDto saleRequestDto, 
            @RequestParam Integer quantity) {
        
        // Este método descuenta stock, calcula el total y registra la venta
        SaleResponseDto sale = saleService.processProductSale(saleRequestDto, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    // Cancelar una venta: DELETE http://localhost:8080/api/sales/1
    // Nota: Requerimos el productId y quantity para devolver el stock al almacén
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Integer id, 
            @RequestParam Integer productId, 
            @RequestParam Integer quantity) {
        
        saleService.cancelSale(id, productId, quantity);
        return ResponseEntity.noContent().build();
    }
}