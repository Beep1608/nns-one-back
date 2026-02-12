package com.nns.punto_venta.dtos.products;

import java.math.BigDecimal;

public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer userId;

    // Constructor vacío (necesario para frameworks como Jackson)
    public ProductRequestDto(String name, BigDecimal price, Integer stock, Integer userId)
    {
        this.name=name;
        this.price =price;
        this.stock= stock;
        this.userId = userId;
    }
    public ProductRequestDto() {}

    // Getters y Setters
    public String getName() { return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    public Integer getStock() {return stock;}
    public void setStock(Integer stock) {this.stock = stock;}

    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}
}
