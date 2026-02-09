package com.nns.punto_venta.dtos.sales;

import java.math.BigDecimal;

public class SaleResponseDto {
    private Integer id;
    private String username;
    private BigDecimal totalAmount;
    private String status;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public BigDecimal getTotalAmount() {return totalAmount;}

    public void setTotalAmount(BigDecimal totalAmount) {this.totalAmount = totalAmount;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

}
