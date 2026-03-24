# Project Architecture

## Elements
0. Module
1. Controller
2. Dto
3. Mapper
4. Service
5. Respoistory
6. Entity
7. Exception

## Working between layers
## Module
1. It just holds all the next elements
2. It's used for sectioning every element in it's corresponding package

## Controller
1. Defines entrypoints that can be consumed by the client 
2. Especifies open api documentation for entry points
3. Delegates all heavy work to the `Service`, even response handling
5. Defines Requests `Dto`
6. Uses `@Valid`on `Dtos` if needed
7. Uses `@PathVariable`or `@ParameterObject`

## Dto
1. If it is a Request Dto, defines all need fields for doing the request operation
2. If it is a Response Dto, defines all necesary fields to build client response.
3. The could be used for other functions that not necesary implies a request

## Mapper
1. Defines a method to convert from `Dto` to `Entity`
2. Defines a methos to convert from `Entity`to `Dto`

## Service
1. Implements methods for all heavy operation
2. Implements business logic
3. Uses `Repository` for Database transactions 
4. Uses `@Transactional` if necessary

## Repository
1. Defines methods for database transactions
2. Extends `JpaRepository` for operations
3. Uses `@Query` on methods if necessary

## Entity
1. Represents a table of database
2. Defines all colums of a table
3. Defines al relations with other tables

## Exception
1. Used for business logic
2. Parameter validation