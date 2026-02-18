package com.nns.punto_venta.services;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.dtos.products.ProductResponseDto;
import com.nns.punto_venta.dtos.sales.SaleRequestDto;
import com.nns.punto_venta.dtos.sales.SaleResponseDto;
import com.nns.punto_venta.entities.SaleEntity;
import com.nns.punto_venta.entities.UserEntity;
import com.nns.punto_venta.exceptions.sales.SaleNotFoundException;
import com.nns.punto_venta.mappers.sales.SaleMapper;
import com.nns.punto_venta.repositories.SaleRepository;
import com.nns.punto_venta.repositories.UserRepository;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final SaleMapper saleMapper;

    // Inyección por constructor corregida
    public SaleService(SaleRepository saleRepository, 
                       UserRepository userRepository, 
                       ProductService productService,
                       SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.saleMapper = saleMapper;
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDto> findAll() {
        return saleRepository.findAll().stream()
                .map(saleMapper::toResponseDto)
                .toList();
    }

    /**
     * Proceso de Venta simplificado (Solo monto)
     */
    @Transactional
    public SaleResponseDto createSale(SaleRequestDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        SaleEntity sale = saleMapper.toEntity(dto);
        sale.setUser(user);
        sale.setStatus("COMPLETADA");
        
        return saleMapper.toResponseDto(saleRepository.save(sale));
    }

    /**
     * Venta de un producto específico con descuento de stock
     */
    @Transactional
    public SaleResponseDto processProductSale(SaleRequestDto dto, Integer quantity) {
        // 1. Obtener producto y validar stock (findById ya lanza excepción en ProductService)
        // Nota: En un sistema real, el ProductResponseDto debería convertirse a Entity o buscarse de nuevo
        ProductResponseDto productDto = productService.findProductById(dto.getProductId());
        
        // 2. Calcular monto total
        BigDecimal total = productDto.getPrice().multiply(new BigDecimal(quantity));
        dto.setTotalAmount(total);

        // 3. Descontar stock (Usa la lógica de tu ProductService)
        productService.updateStock(dto.getProductId(), -quantity);

        // 4. Registrar la venta usando el método anterior
        return createSale(dto);
    }

    @Transactional
    public void cancelSale(Integer saleId, Integer productId, Integer quantity) {
        SaleEntity sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Venta no encontrada"));
        
        sale.setStatus("CANCELADA");
        sale.setDeletedAt(OffsetDateTime.now());
        
        // Devolvemos el stock
        productService.updateStock(productId, quantity);
        
        saleRepository.save(sale);
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDto> searchSalesByQuery(String query) {
        // La lógica de búsqueda ahora incluye la columna de cantidad convertida a texto
        return saleRepository.searchSales(query)
                .stream()
                .map(saleMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDto> findByAdvancedFilters(
        Integer userId, 
        BigDecimal totalAmount, 
        String status, 
        LocalDate createdDate, 
        LocalDate updatedDate, 
        boolean includeDeleted) {

    Specification<SaleEntity> spec = Specification.where((Specification<SaleEntity>) null);

    // 1. Filtro por Usuario
    if (userId != null) {
        spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
    }

    // 2. Filtro por Monto Total (exacto)
    if (totalAmount != null) {
        spec = spec.and((root, query, cb) -> cb.equal(root.get("totalAmount"), totalAmount));
    }

    // 3. Filtro por Estatus
    if (status != null && !status.isEmpty()) {
        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
    }

    // 4. Filtro por Fecha de Creación (asumiendo que es un campo LocalDateTime/OffsetDateTime)
    if (createdDate != null) {
        spec = spec.and((root, query, cb) -> {
            return cb.between(root.get("createdAt"), createdDate.atStartOfDay(), createdDate.plusDays(1).atStartOfDay());
        });
    }

    // 5. Borrado Lógico
    // Si includeDeleted es false, solo mostramos los que tienen deletedAt NULL
    if (!includeDeleted) {
        spec = spec.and((root, query, cb) -> cb.isNull(root.get("deletedAt")));
    }

    return saleRepository.findAll(spec).stream()
            .map(saleMapper::toResponseDto)
            .toList();
}
}