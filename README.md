# 🏪 GESTOCK - Sistema de Gestión de Inventario Multi-Tenant

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**Sistema integral de gestión de inventario para pequeños y medianos negocios**

[Características](#-características-principales) • [Instalación](#-instalación) • [API](#-api-endpoints) • [Arquitectura](#-arquitectura)

</div>

---

## 📋 Tabla de Contenidos

- [Visión General](#-visión-general)
- [Características Principales](#-características-principales)
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitectura](#-arquitectura)
- [Modelo de Datos](#-modelo-de-datos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [API Endpoints](#-api-endpoints)
- [Seguridad](#-seguridad)
- [Reglas de Negocio](#-reglas-de-negocio)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Manejo de Errores](#-manejo-de-errores)
- [Contribución](#-contribución)

---

## 🎯 Visión General

**Gestock** es un sistema de gestión de inventario diseñado específicamente para negocios que requieren:

- ✅ **Multi-tenancy**: Soporte para múltiples negocios en una sola instancia
- ✅ **Multi-almacén**: Gestión de stock distribuido en diferentes ubicaciones
- ✅ **Trazabilidad completa**: Auditoría de todas las transacciones de inventario
- ✅ **Seguridad robusta**: Autenticación JWT y aislamiento de datos por negocio
- ✅ **API RESTful**: Arquitectura moderna lista para integrarse con cualquier frontend

### 🎓 Contexto del Proyecto

Este proyecto fue desarrollado como **MVP para Proyecto de Grado**, enfocándose en implementar las funcionalidades críticas de gestión de inventario en un plazo de 2 semanas.

---

## ⭐ Características Principales

### 🔐 Seguridad Multi-Tenant
- Autenticación basada en **JWT** (JSON Web Tokens)
- **Aislamiento total** de datos entre negocios
- Sistema de roles y permisos (ADMIN, USER)
- Filtrado automático por `businessId` en todas las consultas

### 📦 Gestión de Inventario
- CRUD completo de **Productos**
- CRUD completo de **Almacenes** (warehouses)
- Gestión de **Stock por Almacén** (WarehouseProduct)
- **Transacciones de Stock** (ENTRADA/SALIDA) con actualización automática

### 📊 Trazabilidad y Auditoría
- Registro de todas las transacciones de stock
- Fecha y hora automática de cada operación
- Usuario responsable de cada transacción
- Historial completo por producto, almacén o negocio

### 🛡️ Manejo de Errores Profesional
- **GlobalExceptionHandler** con respuestas estandarizadas
- Excepciones personalizadas para casos de negocio
- Códigos HTTP apropiados para cada tipo de error
- Mensajes descriptivos en español

---

## 🚀 Stack Tecnológico

### Backend
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 3.5.0 | Framework principal |
| **Spring Security** | 6.x | Autenticación y autorización |
| **Spring Data JPA** | 3.x | Persistencia de datos |
| **Hibernate** | 6.x | ORM (Object-Relational Mapping) |
| **MapStruct** | 1.5.x | Mapeo de DTOs |
| **Lombok** | 1.18.x | Reducción de código boilerplate |
| **Gradle** | 8.14 | Gestión de dependencias y build |

### Base de Datos
| Base de Datos | Uso |
|---------------|-----|
| **PostgreSQL** | Producción (Recomendado) |
| **H2** | Desarrollo y Testing |

### Herramientas
- **JWT** (jjwt) - Tokens de autenticación
- **BCrypt** - Encriptación de contraseñas
- **Jackson** - Serialización JSON

---

## 🏗️ Arquitectura

### Arquitectura de Capas

```
┌─────────────────────────────────────────────────────────────┐
│                      CLIENTE (Frontend)                      │
└─────────────────────────────────────────────────────────────┘
                            ▼ HTTP/JSON
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE CONTROLADORES                     │
│  (AuthController, ProductController, TransactionController)  │
│               - Validación de entrada                        │
│               - Manejo de HTTP                               │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE SERVICIOS                        │
│     (ProductService, TransactionService, AuthService)        │
│               - Lógica de negocio                            │
│               - Validaciones de seguridad                    │
│               - Transacciones (@Transactional)               │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   CAPA DE REPOSITORIOS                       │
│  (ProductRepository, TransactionRepository, UserRepository)  │
│               - Consultas a la base de datos                 │
│               - Operaciones CRUD                             │
└─────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     BASE DE DATOS                            │
│                  (PostgreSQL / H2)                           │
└─────────────────────────────────────────────────────────────┘
```

### Estructura del Proyecto

```
src/main/java/com/gestock/GestockBackend/
├── 📁 config/                      # Configuración de Spring
│   └── SecurityConfig.java         # Configuración de seguridad JWT
│
├── 📁 controller/                  # Endpoints REST
│   ├── AuthController.java         # Login y registro
│   ├── BusinessController.java     # CRUD de negocios
│   ├── ProductController.java      # CRUD de productos
│   ├── WarehouseController.java    # CRUD de almacenes
│   ├── WarehouseProductController.java  # Gestión de stock
│   └── TransactionController.java  # Transacciones de inventario
│
├── 📁 model/
│   ├── 📁 entity/                  # Entidades JPA
│   │   ├── Business.java           # Negocio (multi-tenant)
│   │   ├── User.java               # Usuario
│   │   ├── Role.java               # Roles de usuario
│   │   ├── Warehouse.java          # Almacén
│   │   ├── Product.java            # Producto (catálogo global)
│   │   ├── WarehouseProduct.java   # Stock por almacén
│   │   ├── Transaction.java        # Transacción de stock
│   │   └── TransactionType.java    # Enum: ENTRADA/SALIDA
│   │
│   └── 📁 dto/                     # Data Transfer Objects
│       ├── LoginRequestDto.java
│       ├── TransactionRequestDto.java
│       ├── TransactionResponseDto.java
│       ├── WarehouseProductRequestDto.java
│       └── WarehouseProductResponseDto.java
│
├── 📁 repository/                  # Repositorios JPA
│   ├── BusinessRepository.java
│   ├── ProductRepository.java
│   ├── WarehouseRepository.java
│   ├── WarehouseProductRepository.java
│   ├── TransactionRepository.java
│   └── UserRepository.java
│
├── 📁 service/                     # Lógica de negocio
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── WarehouseService.java
│   ├── WarehouseProductService.java
│   └── TransactionService.java     # ⭐ Actualización automática de stock
│
├── 📁 mappers/                     # Mappers (MapStruct)
│   ├── TransactionMapper.java
│   ├── WarehouseProductMapper.java
│   └── ...
│
├── 📁 security/                    # Utilidades de seguridad
│   ├── JwtUtil.java                # Generación y validación de JWT
│   ├── JwtRequestFilter.java       # Filtro de requests
│   └── GestockUserDetails.java     # UserDetails personalizado
│
├── 📁 exception/                   # Manejo de excepciones
│   ├── GlobalExceptionHandler.java # Handler global
│   ├── ResourceNotFoundException.java
│   ├── InsufficientStockException.java
│   ├── BusinessAccessDeniedException.java
│   └── ErrorResponse.java          # DTO de respuesta de error
│
└── GestockBackendApplication.java  # Clase principal
```

---

## 💾 Modelo de Datos
![Modelo de Datos de Gestock](./docs/assets/gestock-db-diagram.png)


### Entidades Principales

#### 1. **Business** (Negocio)
Raíz del sistema multi-tenant. Cada negocio tiene sus propios datos aislados.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long (PK) | Identificador único |
| `name` | String | Nombre del negocio |

#### 2. **User** (Usuario)
Usuarios del sistema asociados a un negocio.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long (PK) | Identificador único |
| `email` | String (unique) | Email para login |
| `password` | String | Contraseña encriptada (BCrypt) |
| `business_id` | Long (FK) | Negocio al que pertenece |
| `role_id` | Long (FK) | Rol del usuario |

#### 3. **Warehouse** (Almacén)
Ubicaciones físicas donde se almacena el inventario.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long (PK) | Identificador único |
| `name` | String | Nombre del almacén |
| `address` | String | Dirección física |
| `business_id` | Long (FK) | Negocio propietario |

#### 4. **Product** (Producto)
Catálogo global de productos.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long (PK) | Identificador único |
| `name` | String | Nombre del producto |
| `price` | Double | Precio de venta |
| `description` | String | Descripción del producto |

#### 5. **WarehouseProduct** (Stock por Almacén)
Tabla de unión que gestiona el stock de cada producto en cada almacén.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `product_id` | Long (PK) | ID del producto |
| `warehouse_id` | Long (PK) | ID del almacén |
| `stock` | Integer | Cantidad disponible |

**Nota:** Usa clave primaria compuesta `(product_id, warehouse_id)`.

#### 6. **Transaction** (Transacción)
Registro de auditoría de todas las operaciones de stock.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long (PK) | Identificador único |
| `type` | TransactionType | ENTRADA o SALIDA |
| `quantity` | Integer | Cantidad movida |
| `description` | String | Descripción opcional |
| `created_at` | LocalDateTime | Fecha automática |
| `user_id` | Long (FK) | Usuario que realizó la acción |
| `warehouseProduct` | FK compuesta | Producto-Almacén afectado |

---

## 📥 Instalación

### Prerrequisitos

- **Java 21** o superior
- **PostgreSQL 16** (o usar H2 para desarrollo)
- **Gradle 8.14** (incluido con Gradle Wrapper)
- **Git**

### Pasos de Instalación

#### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/gestock-backend.git
cd gestock-backend
```

#### 2. Configurar la base de datos

**Opción A: PostgreSQL (Producción)**

```sql
-- Crear la base de datos
CREATE DATABASE gestockdb;

-- Crear usuario (opcional)
CREATE USER gestock_user WITH PASSWORD 'tu_password';
GRANT ALL PRIVILEGES ON DATABASE gestockdb TO gestock_user;
```

**Opción B: H2 (Desarrollo)**

No requiere configuración, la base de datos se crea automáticamente.

#### 3. Configurar `application.properties`

Editar `src/main/resources/application.properties`:

```properties
# Perfil activo: dev (H2) o prod (PostgreSQL)
spring.profiles.active=prod

# PostgreSQL Configuration (Producción)
spring.datasource.url=jdbc:postgresql://localhost:5432/gestockdb
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD_AQUI

# JWT Secret (Cambiar en producción)
jwt.secret=TU_SECRET_KEY_BASE64_AQUI
jwt.expiration=86400000
```

#### 4. Compilar el proyecto

```bash
# Linux/Mac
./gradlew build

# Windows
gradlew.bat build
```

#### 5. Ejecutar la aplicación

```bash
# Linux/Mac
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

La aplicación estará disponible en: **http://localhost:8080/gestock**

---

## ⚙️ Configuración

### Perfiles de Spring

El proyecto soporta dos perfiles:

#### **Perfil `dev` (H2 - Desarrollo)**

```properties
spring.profiles.active=dev
```

- Base de datos en memoria (H2)
- Consola H2 disponible en: http://localhost:8080/gestock/h2-console
- Los datos se pierden al reiniciar
- `spring.jpa.hibernate.ddl-auto=update`

#### **Perfil `prod` (PostgreSQL - Producción)**

```properties
spring.profiles.active=prod
```

- Base de datos persistente (PostgreSQL)
- **⚠️ IMPORTANTE:** `spring.jpa.hibernate.ddl-auto=create` (recrea el esquema en cada inicio)
- Cambiar a `update` o `validate` en producción real

### Variables de Entorno Importantes

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SERVER_PORT` | Puerto de la aplicación | `8080` |
| `DB_URL` | URL de PostgreSQL | `jdbc:postgresql://localhost:5432/gestockdb` |
| `DB_USERNAME` | Usuario de base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de base de datos | `1234` |
| `JWT_SECRET` | Clave secreta para JWT | (Ver application.properties) |
| `JWT_EXPIRATION` | Tiempo de expiración del token (ms) | `86400000` (24h) |

### CORS

El backend permite requests desde:
- `http://localhost:3000` (React dev server)

Para cambiar, modificar `SecurityConfig.java:82`:

```java
config.addAllowedOrigin("http://tu-frontend-url.com");
```

---

## 📡 API Endpoints

**Base URL:** `http://localhost:8080/gestock`

### 🔓 Autenticación (Públicos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/register` | Registrar nuevo usuario y negocio |
| POST | `/auth/login` | Iniciar sesión y obtener JWT |

**Ejemplo - Login:**

```bash
curl -X POST http://localhost:8080/gestock/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@empresa.com",
    "password": "password123"
  }'
```

**Respuesta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "admin@empresa.com",
  "businessId": 1,
  "role": "ADMIN"
}
```

---

### 🏢 Business (Negocios)

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/businesses` | ✅ | Listar todos los negocios |
| GET | `/businesses/{id}` | ✅ | Obtener negocio por ID |
| POST | `/businesses` | ✅ | Crear nuevo negocio |
| PUT | `/businesses/{id}` | ✅ | Actualizar negocio |
| DELETE | `/businesses/{id}` | ✅ | Eliminar negocio |

---

### 🏭 Warehouses (Almacenes)

| Método | Endpoint | Auth | Rol | Descripción |
|--------|----------|------|-----|-------------|
| GET | `/warehouses` | ✅ | ADMIN | Listar todos los almacenes |
| GET | `/warehouses/{id}` | ✅ | - | Obtener almacén por ID |
| GET | `/warehouses/by-business/{businessId}` | ✅ | - | Almacenes de un negocio |
| POST | `/warehouses` | ✅ | - | Crear almacén |
| PUT | `/warehouses/{id}` | ✅ | - | Actualizar almacén |
| DELETE | `/warehouses/{id}` | ✅ | - | Eliminar almacén |

---

### 📦 Products (Productos)

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/products` | ✅ | Listar todos los productos |
| GET | `/products/{id}` | ✅ | Obtener producto por ID |
| POST | `/products` | ✅ | Crear producto |
| PUT | `/products/{id}` | ✅ | Actualizar producto |
| DELETE | `/products/{id}` | ✅ | Eliminar producto |

---

### 🔢 Warehouse Products (Stock por Almacén)

| Método | Endpoint | Auth | Rol | Descripción |
|--------|----------|------|-----|-------------|
| GET | `/warehouse-products/by-business` | ✅ | - | Stock de todos los almacenes del negocio |
| GET | `/warehouse-products/by-warehouse/{warehouseId}` | ✅ | - | Stock de un almacén específico |
| GET | `/warehouse-products/{productId}/{warehouseId}` | ✅ | - | Stock de un producto en un almacén |
| POST | `/warehouse-products` | ✅ | ADMIN | Agregar producto a almacén |
| PUT | `/warehouse-products/{productId}/{warehouseId}` | ✅ | ADMIN | Actualizar stock manualmente |
| DELETE | `/warehouse-products/{productId}/{warehouseId}` | ✅ | ADMIN | Eliminar producto de almacén |

**Ejemplo - Agregar producto a almacén:**

```bash
curl -X POST http://localhost:8080/gestock/warehouse-products \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "warehouseId": 2,
    "quantity": 100
  }'
```

---

### 📊 Transactions (Transacciones de Stock)

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/transactions/by-business` | ✅ | Todas las transacciones del negocio |
| GET | `/transactions/by-warehouse/{warehouseId}` | ✅ | Transacciones de un almacén |
| GET | `/transactions/by-product/{productId}` | ✅ | Transacciones de un producto |
| GET | `/transactions/{id}` | ✅ | Obtener transacción por ID |
| POST | `/transactions` | ✅ | **Crear transacción (actualiza stock automáticamente)** |
| DELETE | `/transactions/{id}` | ✅ | Eliminar transacción |

**⭐ Ejemplo - Registrar ENTRADA de stock:**

```bash
curl -X POST http://localhost:8080/gestock/transactions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "ENTRADA",
    "quantity": 50,
    "description": "Compra de mercancía - Proveedor XYZ",
    "productId": 1,
    "warehouseId": 2
  }'
```

**Respuesta:**

```json
{
  "id": 15,
  "type": "ENTRADA",
  "quantity": 50,
  "description": "Compra de mercancía - Proveedor XYZ",
  "createdAt": "2025-01-24T14:30:00",
  "userId": 3,
  "userName": "admin@empresa.com",
  "productId": 1,
  "productName": "Laptop Dell Inspiron 15",
  "warehouseId": 2,
  "warehouseName": "Almacén Central",
  "businessId": 1
}
```

**⭐ Ejemplo - Registrar SALIDA de stock:**

```bash
curl -X POST http://localhost:8080/gestock/transactions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "SALIDA",
    "quantity": 10,
    "description": "Venta a cliente - Pedido #12345",
    "productId": 1,
    "warehouseId": 2
  }'
```

---

## 🔒 Seguridad

### Autenticación JWT

Todos los endpoints (excepto `/auth/**`) requieren un token JWT válido.

#### Formato del Header

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Información en el Token

El JWT contiene:
- `id` - ID del usuario
- `email` - Email del usuario
- `businessId` - ID del negocio (para filtrado multi-tenant)
- `role` - Rol del usuario (ADMIN, USER)

### Multi-Tenancy (Aislamiento de Datos)

**Implementación:**

1. El `businessId` se extrae automáticamente del JWT
2. Todos los servicios filtran por `businessId`
3. Validaciones de acceso en cada operación

**Ejemplo de código:**

```java
// En TransactionController
private Long getBusinessIdFromAuth(Authentication auth) {
    GestockUserDetails userDetails = (GestockUserDetails) auth.getPrincipal();
    return userDetails.getBusinessId();
}

// Uso en endpoint
@GetMapping("/by-business")
public ResponseEntity<List<TransactionResponseDto>> getAllByBusiness(Authentication auth) {
    Long businessId = getBusinessIdFromAuth(auth);
    return ResponseEntity.ok(transactionService.getTransactionsByBusinessId(businessId));
}
```

### Control de Acceso por Roles

Algunos endpoints requieren rol `ADMIN`:

```java
@PreAuthorize("hasRole('ADMIN')")
@PostMapping("/warehouse-products")
public ResponseEntity<WarehouseProductResponseDto> create(...) {
    // Solo usuarios con rol ADMIN pueden ejecutar
}
```

---

## 📏 Reglas de Negocio

### 1. Gestión de Stock

#### ✅ Transacciones de ENTRADA
- Incrementa el stock del `WarehouseProduct`
- Crea registro de auditoría en `Transaction`
- Actualización **transaccional** (rollback si falla)

```java
newStock = currentStock + quantity;
```

#### ✅ Transacciones de SALIDA
- **Valida que haya stock suficiente**
- Reduce el stock del `WarehouseProduct`
- Lanza `InsufficientStockException` si no hay stock

```java
if (currentStock < quantity) {
    throw new InsufficientStockException(...);
}
newStock = currentStock - quantity;
```

#### ⚠️ Restricciones
- No se permite stock negativo
- Las transacciones son **inmutables** (no se pueden editar)
- Solo se pueden crear o eliminar

### 2. Multi-Tenancy

#### ✅ Aislamiento Total
- Un usuario solo puede ver/modificar datos de su propio negocio
- Validación automática en todos los endpoints
- Lanza `BusinessAccessDeniedException` si intenta acceder a otro negocio

#### ✅ Validaciones de Seguridad

```java
// Validar que el warehouse pertenece al negocio del usuario
if (!warehouse.getBusiness().getId().equals(businessId)) {
    throw new BusinessAccessDeniedException("No tienes acceso a este almacén");
}
```

### 3. Integridad de Datos

#### ✅ Validaciones antes de Crear
- El warehouse debe existir
- El producto debe existir
- El warehouse debe pertenecer al negocio del usuario
- No se permite duplicar productos en el mismo almacén

### 4. Auditoría

#### ✅ Trazabilidad Completa
- Fecha automática (`@CreationTimestamp`)
- Usuario que realizó la acción
- Tipo de operación (ENTRADA/SALIDA)
- Cantidad movida
- Producto y almacén afectados

---

## 💡 Ejemplos de Uso

### Flujo Completo: Desde Registro hasta Transacción

#### 1. Registrar nuevo negocio y usuario

```bash
POST /gestock/auth/register
{
  "businessName": "Mi Tienda S.A.",
  "email": "admin@mitienda.com",
  "password": "password123"
}
```

#### 2. Iniciar sesión

```bash
POST /gestock/auth/login
{
  "email": "admin@mitienda.com",
  "password": "password123"
}

# Respuesta: { "token": "eyJhbG..." }
```

#### 3. Crear un almacén

```bash
POST /gestock/warehouses
Authorization: Bearer eyJhbG...
{
  "name": "Almacén Principal",
  "address": "Calle 123 #45-67",
  "businessId": 1
}
```

#### 4. Crear un producto

```bash
POST /gestock/products
Authorization: Bearer eyJhbG...
{
  "name": "Laptop Dell Inspiron 15",
  "price": 2500000,
  "description": "Laptop Core i5, 8GB RAM, 256GB SSD"
}
```

#### 5. Agregar producto al almacén (stock inicial)

```bash
POST /gestock/warehouse-products
Authorization: Bearer eyJhbG...
{
  "productId": 1,
  "warehouseId": 1,
  "quantity": 100
}
```

#### 6. Registrar entrada de mercancía

```bash
POST /gestock/transactions
Authorization: Bearer eyJhbG...
{
  "type": "ENTRADA",
  "quantity": 50,
  "description": "Compra a proveedor ABC",
  "productId": 1,
  "warehouseId": 1
}

# Stock actual: 100 + 50 = 150
```

#### 7. Registrar venta (salida)

```bash
POST /gestock/transactions
Authorization: Bearer eyJhbG...
{
  "type": "SALIDA",
  "quantity": 20,
  "description": "Venta a cliente - Pedido #001",
  "productId": 1,
  "warehouseId": 1
}

# Stock actual: 150 - 20 = 130
```

#### 8. Consultar historial de transacciones

```bash
GET /gestock/transactions/by-warehouse/1
Authorization: Bearer eyJhbG...

# Devuelve todas las transacciones del almacén
```

---

## ⚠️ Manejo de Errores

### GlobalExceptionHandler

El sistema tiene un manejador global de excepciones que devuelve respuestas estandarizadas.

### Formato de Respuesta de Error

```json
{
  "timestamp": "2025-01-24T14:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No tienes acceso a este almacén",
  "path": "/gestock/warehouse-products/1/2"
}
```

### Códigos HTTP y Excepciones

| Código | Excepción | Descripción |
|--------|-----------|-------------|
| **400** | `IllegalArgumentException` | Datos inválidos |
| **400** | `InsufficientStockException` | Stock insuficiente para salida |
| **401** | `BadCredentialsException` | Credenciales incorrectas |
| **403** | `AccessDeniedException` | Sin permisos |
| **403** | `BusinessAccessDeniedException` | Acceso a datos de otro negocio |
| **404** | `ResourceNotFoundException` | Recurso no encontrado |
| **404** | `EntityNotFoundException` | Entidad no encontrada |
| **500** | `Exception` | Error interno del servidor |

### Ejemplos de Errores

#### Stock Insuficiente

```bash
POST /gestock/transactions
{
  "type": "SALIDA",
  "quantity": 1000,  # Pero solo hay 130
  "productId": 1,
  "warehouseId": 1
}
```

**Respuesta: 400 Bad Request**

```json
{
  "timestamp": "2025-01-24T14:35:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Stock insuficiente para el producto ID: 1 en almacén ID: 1. Solicitado: 1000, Disponible: 130",
  "path": "/gestock/transactions"
}
```

#### Acceso Denegado a Otro Negocio

```bash
GET /gestock/warehouses/999
# El warehouse 999 pertenece a otro negocio
```

**Respuesta: 403 Forbidden**

```json
{
  "timestamp": "2025-01-24T14:40:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No tienes acceso a este almacén",
  "path": "/gestock/warehouses/999"
}
```

---

## 🧪 Testing

### Ejecutar Tests

```bash
./gradlew test
```

### Tests Recomendados

- ✅ Tests de integración de endpoints
- ✅ Tests de seguridad multi-tenant
- ✅ Tests de lógica de stock (ENTRADA/SALIDA)
- ✅ Tests de validaciones de negocio

---

## 🚀 Despliegue

### Generar JAR de Producción

```bash
./gradlew build -x test
```

El JAR se genera en: `build/libs/GestockBackend-0.0.1-SNAPSHOT.jar`

### Ejecutar en Producción

```bash
java -jar build/libs/GestockBackend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://your-db-host:5432/gestockdb \
  --spring.datasource.username=your_user \
  --spring.datasource.password=your_password \
  --jwt.secret=your_secret_key
```

### Variables de Entorno Recomendadas

```bash
export DB_URL=jdbc:postgresql://your-db-host:5432/gestockdb
export DB_USERNAME=gestock_user
export DB_PASSWORD=super_secret_password
export JWT_SECRET=your_base64_encoded_secret
export SERVER_PORT=8080
```

---

## 📚 Documentación Adicional

### Archivos de Referencia

- `CLAUDE.md` - Instrucciones para Claude Code
- `GestockMVPDoc.md` - Documentación del MVP original
- `application.properties` - Configuración de la aplicación

### Swagger/OpenAPI (Futuro)

En versiones futuras se agregará documentación interactiva con Swagger UI.

---

## 🤝 Contribución

### Cómo Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add: Amazing Feature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

### Estándares de Código

- **Java Code Style**: Google Java Style Guide
- **Commits**: Conventional Commits
- **Tests**: Cobertura mínima del 70%
- **Documentación**: Javadoc en métodos públicos

---

## 📝 Notas de Versión

### v1.0.0 (2025-01-24) - MVP Completo

#### ✨ Nuevas Características
- ✅ Sistema de transacciones de stock (ENTRADA/SALIDA)
- ✅ Actualización automática de stock
- ✅ GlobalExceptionHandler con respuestas estandarizadas
- ✅ Filtrado multi-tenant completo en todos los endpoints
- ✅ Excepciones personalizadas de negocio
- ✅ Auditoría completa de transacciones

#### 🔒 Seguridad
- ✅ Validación automática de `businessId` en todos los servicios
- ✅ Imposible acceder a datos de otros negocios
- ✅ Control de acceso por roles (ADMIN/USER)

#### 🐛 Correcciones
- ✅ Corregido mapeo de Transaction (userName ahora usa email)
- ✅ Validación de stock insuficiente antes de SALIDA
- ✅ Eliminado endpoint PUT de transacciones (inmutables)

---

## 📄 Licencia

Este proyecto es un **MVP académico** desarrollado para Proyecto de Grado.

---

## 👥 Autores

- **Equipo Gestock** - Proyecto de Grado 2025 (CESDE)
- Jhorman Andres Salazar Quiroz
- Juan Jose Villa Avendaño
- Jose David Cardona Lujan
- Valentina Ocampo
- Duber Andres Monsalve

---

## 🙏 Agradecimientos

- Spring Boot Team
- PostgreSQL Community
- Claude Code por asistencia en desarrollo

---

## 📞 Soporte

Para preguntas o reportar problemas:
- 📧 Email: salazarjhorman181@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/JhormanSalazar/GestockBackend/issues)

---

<div align="center">

**⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub ⭐**

</div>
