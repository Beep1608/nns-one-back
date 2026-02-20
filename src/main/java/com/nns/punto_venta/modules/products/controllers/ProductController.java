package com.nns.punto_venta.modules.products.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nns.punto_venta.modules.products.dtos.ProductRequestDto;
import com.nns.punto_venta.modules.products.dtos.ProductResponseDto;
import com.nns.punto_venta.modules.products.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag (name="Productos", description="Operaciones relacionadas con los productos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Listar todos los productos: GET http://localhost:8080/api/products
    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista de todos los productos registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente", content=@Content(mediaType="application/json", schema=@Schema(implementation=ProductResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content( mediaType="application/json"))
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        List<ProductResponseDto> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    // Obtener uno por ID: GET http://localhost:8080/api/products/1
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID", description = "Devuelve un producto específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente", content=@Content(mediaType="application/json", schema=@Schema(implementation=ProductResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content=@Content(mediaType="application/json"))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    // Crear un producto: POST http://localhost:8080/api/products
    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema.")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", content=@Content(mediaType="application/json", schema=@Schema(implementation=ProductResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content=@Content(mediaType="application/json"))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto createdProduct = productService.create(productRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Borrado lógico: DELETE http://localhost:8080/api/products/1
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto (soft delete)", description = "Elimina un producto específico del sistema según su ID, marcándolo como eliminado sin eliminarlo físicamente de la base de datos.")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content=@Content(mediaType="application/json"))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Ejemplo de actualización de stock: PATCH http://localhost:8080/api/products/1/stock
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar el stock de un producto", description = "Actualiza el stock de un producto específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content=@Content(mediaType="application/json"))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content=@Content(mediaType="application/json"))
    public ResponseEntity<Void> updateStock(@PathVariable Integer id, @RequestParam Integer quantity) {
        productService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }
}
