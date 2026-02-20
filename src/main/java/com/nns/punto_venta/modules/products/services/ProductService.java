package com.nns.punto_venta.modules.products.services;



import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.modules.products.dtos.ProductRequestDto;
import com.nns.punto_venta.modules.products.dtos.ProductResponseDto;
import com.nns.punto_venta.modules.products.entities.ProductEntity;
import com.nns.punto_venta.modules.security.entities.UserEntity;
import com.nns.punto_venta.modules.products.exceptions.ProductNotFoundException;
import com.nns.punto_venta.modules.products.mappers.ProductMapper;
import com.nns.punto_venta.modules.products.repositories.ProductRepository;
import com.nns.punto_venta.modules.security.repositories.UserRepository;

@Service
public class ProductService {

    
    private ProductRepository productRepository;

    private UserRepository userRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, ProductMapper productMapper)
    {
        this.productRepository= productRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
    }

   // Obtener todos los productos activos (Devuelve DTOs)
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll() {
        List<ProductEntity> entities = productRepository.findAll();
        return productMapper.toResponseDtoList(entities);
    }

    // Buscar por ID (Devuelve DTO)
    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Integer id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
        return productMapper.toResponseDto(entity);
    }

    // Crear un producto (Recibe ProductDto, devuelve ProductResponseDto)
    @Transactional
    public ProductResponseDto create(ProductRequestDto dto) {
        // 1. Buscamos el usuario dueño del producto
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Convertimos DTO a Entidad
        ProductEntity product = productMapper.toEntity(dto);
        
        // 3. Establecemos la relación manualmente
        product.setUser(user);
        
        // 4. Guardamos y devolvemos la respuesta mapeada
        ProductEntity savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    // Actualizar stock
    @Transactional
    public void updateStock(Integer productId, Integer quantity) {
        // Aquí usamos el método interno que devuelve la entidad para poder editarla
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                
        int newStock = product.getStock() + quantity;
        
        if (newStock < 0) {
            throw new RuntimeException("No hay suficiente stock para el producto: " + product.getName());
        }
        
        product.setStock(newStock);
        productRepository.save(product);
    }

    // Borrado lógico
    @Transactional
    public void delete(Integer id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                
        product.setDeletedAt(java.time.OffsetDateTime.now());
        productRepository.save(product);
    }
}

