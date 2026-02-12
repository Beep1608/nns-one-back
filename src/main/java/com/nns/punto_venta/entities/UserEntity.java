package com.nns.punto_venta.entities;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @CreationTimestamp // Llena automáticamente la fecha al insertar
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp // Actualiza automáticamente la fecha al editar
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    
    @Column(name = "last_active_at")
    private OffsetDateTime lastActiveAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductEntity> products;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleEntity> sales;

    // Relación con Roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles", // Nombre de la tabla intermedia en SQL
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();
    // --- Getters y Setters ---

    public void setId(Integer id) { this.id = id; }
    public Integer getId() { return id; }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return password; }

    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void setDeletedAt(OffsetDateTime deletedAt) { this.deletedAt = deletedAt; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }

    public void setProducts(List<ProductEntity> products) { this.products = products; }
    public List<ProductEntity> getProducts() { return products; }

    public void setSales(List<SaleEntity> sales) { this.sales = sales; }
    public List<SaleEntity> getSales() { return sales; }

    public OffsetDateTime getLastActiveAt() {return lastActiveAt;}

    public void setLastActiveAt(OffsetDateTime lastActiveAt) {this.lastActiveAt = lastActiveAt;}

    public Set<RoleEntity> getRoles() { return roles;}

    public void setRoles(Set<RoleEntity> roles) {this.roles = roles;}
}
