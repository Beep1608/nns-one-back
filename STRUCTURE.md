# Project Structure

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── nns/
│   │   │           └── punto_venta/
│   │   │               ├── assemblers/
│   │   │               ├── common/
│   │   │               │   ├── configuration/
│   │   │               │   │   ├── exceptions/
│   │   │               │   │   │   └── GlobalExceptionHandler.java
│   │   │               │   │   └── security/
│   │   │               │   │       └── SecurityConfig.java
│   │   │               │   └── exceptions/
│   │   │               │       ├── GenericErrorException.java
│   │   │               │       └── ResourceNotFoundException.java
│   │   │               ├── modules/
│   │   │               │   ├── products/
│   │   │               │   │   ├── controllers/
│   │   │               │   │   │   └── ProductController.java
│   │   │               │   │   ├── dtos/
│   │   │               │   │   │   ├── ProductDto.java
│   │   │               │   │   │   ├── ProductRequestDto.java
│   │   │               │   │   │   └── ProductResponseDto.java
│   │   │               │   │   ├── entities/
│   │   │               │   │   │   └── ProductEntity.java
│   │   │               │   │   ├── exceptions/
│   │   │               │   │   │   └── ProductNotFoundException.java
│   │   │               │   │   ├── mappers/
│   │   │               │   │   │   └── ProductMapper.java
│   │   │               │   │   ├── repositories/
│   │   │               │   │   │   └── ProductRepository.java
│   │   │               │   │   └── services/
│   │   │               │   │       └── ProductService.java
│   │   │               │   ├── sales/
│   │   │               │   │   ├── controllers/
│   │   │               │   │   │   └── SaleController.java
│   │   │               │   │   ├── dtos/
│   │   │               │   │   │   ├── SaleRequestDto.java
│   │   │               │   │   │   └── SaleResponseDto.java
│   │   │               │   │   ├── entities/
│   │   │               │   │   │   └── SaleEntity.java
│   │   │               │   │   ├── exceptions/
│   │   │               │   │   │   └── SaleNotFoundException.java
│   │   │               │   │   ├── mappers/
│   │   │               │   │   │   └── SaleMapper.java
│   │   │               │   │   ├── repositories/
│   │   │               │   │   │   └── SaleRepository.java
│   │   │               │   │   └── services/
│   │   │               │   │       └── SaleService.java
│   │   │               │   └── security/ #Roles y Usuarios
│   │   │               │       ├── assemblers/
│   │   │               │       │   ├── RoleModelAssembler.java
│   │   │               │       │   └── UserModelAssembler.java
│   │   │               │       ├── controllers/
│   │   │               │       │   ├── RoleController.java
│   │   │               │       │   └── UserController.java
│   │   │               │       ├── dtos/
│   │   │               │       │   ├── RoleRequestDto.java
│   │   │               │       │   ├── RoleResponseDto.java
│   │   │               │       │   ├── UserRequestDto.java
│   │   │               │       │   ├── UserResponseDto.java
│   │   │               │       │   └── UserUpdateRequestDto.java
│   │   │               │       ├── entities/
│   │   │               │       │   ├── PermissionEntity.java
│   │   │               │       │   ├── RoleEntity.java
│   │   │               │       │   └── UserEntity.java
│   │   │               │       ├── exceptions/
│   │   │               │       │   └── UserNotFoundException.java
│   │   │               │       ├── mappers/
│   │   │               │       │   ├── RoleMapper.java
│   │   │               │       │   └── UserMapper.java
│   │   │               │       ├── repositories/
│   │   │               │       │   ├── RoleRepository.java
│   │   │               │       │   └── UserRepository.java
│   │   │               │       ├── services/
│   │   │               │       │   ├── RoleService.java
│   │   │               │       │   ├── UserDetailsImpl.java
│   │   │               │       │   └── UserService.java
│   │   │               │       └── validators/
│   │   │               │           ├── RolesExist.java
│   │   │               │           └── RolesExistValidator.java
│   │   │               └── PuntoVentaApplication.java
│   │   └── resources/
│   │       ├── db/
│   │       │   ├── changelog/
│   │       │   │   └── changelog.yml
│   │       │   ├── migrations/
│   │       │   │   └── migration-07-02-2026.sql
│   │       │   └── seeders/
│   │       │       └── seeder-09-02-2026.sql
│   │       ├── static/
│   │       ├── templates/
│   │       ├── application.properties
│   │       ├── application.properties.example
│   │       └── liquibase.properties
│   └── test/
│       └── java/
│           └── com/
│               └── nns/
│                   └── punto_venta/
│                       ├── controllers/
│                       │   ├── ProductControllerTest.java
│                       │   ├── SaleControllerTest.java
│                       │   └── UserControllerTest.java
│                       └── PuntoVentaApplicationTests.java
├── .gitattributes
├── .gitignore
├── compose.yml
├── Dockerfile
├── HELP.md
├── mvnw
├── mvnw.cmd
└── pom.xml
```
