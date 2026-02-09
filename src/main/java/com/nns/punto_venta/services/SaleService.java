package com.nns.punto_venta.services;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.dtos.products.ProductResponseDto;
import com.nns.punto_venta.dtos.sales.SaleRequestDto;
import com.nns.punto_venta.dtos.sales.SaleResponseDto;
import com.nns.punto_venta.entities.SaleEntity;
import com.nns.punto_venta.entities.UserEntity;
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
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        sale.setStatus("CANCELADA");
        sale.setDeletedAt(OffsetDateTime.now());
        
        // Devolvemos el stock
        productService.updateStock(productId, quantity);
        
        saleRepository.save(sale);
    }
}