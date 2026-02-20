package com.nns.punto_venta.modules.sales.dtos;

import java.math.BigDecimal;

public class SaleRequestDto {
    private Integer userId;
    private Integer productId;
    private BigDecimal totalAmount;

    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getUserId() { return userId; }

    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getProductId() { return productId; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}

