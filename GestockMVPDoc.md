# 📚 Documentación del Proyecto: GESTOCK (MVP para Proyecto de Grado)

---

## 1. Visión General del Proyecto

| Característica | Detalle |
| :--- | :--- |
| **Nombre del Proyecto** | **Gestock** (Gestión de Stock/Inventario) |
| **Público Objetivo** | Pequeños y medianos **negocios (multi-negocio)** |
| **Stack Tecnológico** | **Backend:** Spring Boot (Java), JPA/Hibernate |
| | **Frontend:** React (por definir) |
| | **Base de Datos:** PostgreSQL (Recomendado) / H2 (Desarrollo) |
| **Objetivo del MVP** | Implementar la **funcionalidad central** de inventario, multi-almacén y seguridad en **2 semanas** (Proyecto de Grado). |
| **Escalabilidad** | Arquitectura de **capas**, seguridad basada en **JWT** y diseño de DB orientado a la auditoría. |

---

## 2. Alcance del Producto Mínimo Viable (MVP)

El MVP se centrará en la **Gestión de Stock con contexto de Negocio y Almacén**.

| Módulo | Entidades Implicadas | Funcionalidad Mínima Requerida |
| :--- | :--- | :--- |
| **Seguridad/Auth** | `Usuario`, `Role`, `Negocio` | **Registro** de Negocio y Usuario Admin. **Login** mediante **JWT**. |
| **Maestros** | `Almacén` | **CRUD** de Almacén, asociado al Negocio. |
| **Inventario** | `Producto` | **CRUD** de Producto. Listado de stock **filtrado por Negocio** y Almacén. |
| **Transacciones** | `Movimiento` | Creación de **Movimiento** (`ENTRADA` / `SALIDA`). **Actualización transaccional** del `stock_actual`. |

---

## 3. Arquitectura del Backend (Spring Boot)

La arquitectura sigue el patrón de **Capas** para garantizar la separación de responsabilidades, la mantenibilidad y la escalabilidad.

### 3.1. Estructura de Carpetas

    gestock/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── gestock/
                        ├── **GestockApplication.java** // Clase principal
                        ├── **config/** // Configuración de Spring Security, CORS.
                        │   └── SecurityConfig.java
                        ├── **controller/** // Capa de API REST (manejo de peticiones HTTP)
                        │   ├── AuthController.java         // Login y registro
                        │   └── ...Controller.java          // Controladores para Negocio, Almacén, Producto, Movimiento
                        ├── **model/** // Entidades JPA y DTOs
                        │   └── ...java                     // (Negocio, Almacen, Usuario, Role, Producto, Movimiento)
                        ├── **repository/** // Capa de Acceso a Datos (JPA Repositories)
                        │   └── ...Repository.java
                        ├── **service/** // Capa de Lógica de Negocio (Business Logic)
                        │   ├── AuthService.java            // Lógica de autenticación
                        │   ├── **ProductoService.java** // Contiene la Lógica transaccional de stock
                        │   └── ...Service.java
                        └── **security/** // Utilidades de Seguridad (JWT)
                            └── JwtUtil.java

---

## 4. Diseño de Base de Datos (Esquema Relacional)

Diseño enfocado en el concepto **Multi-Negocio** y la **integridad de Stock**.

### 4.1. Entidades de Seguridad y Contexto

| Tabla | Columna | Tipo de Dato | Observación |
| :--- | :--- | :--- | :--- |
| **ROL** | **id** | (PK) | Identificador único. |
| | **nombre** | (VARCHAR) | Nombre del rol (ej. 'ADMIN'). **Único**. |
| **NEGOCIO** | **id** | (PK) | Identificador único del negocio. |
| | **nombre** | (VARCHAR) | Nombre de la empresa. |
| **USUARIO** | **id** | (PK) | Identificador único. |
| | **negocio\_id** | (FK a NEGOCIO) | Asocia el usuario a su negocio (aislamiento de datos). |
| | **rol\_id** | (FK a ROL) | Define los permisos. |
| | **email** | (VARCHAR) | Usado para login. **Único**. |
| | **password** | (VARCHAR) | Contraseña **cifrada**. |

### 4.2. Entidades de Inventario

| Tabla | Columna | Tipo de Dato | Observación |
| :--- | :--- | :--- | :--- |
| **ALMACEN** | **id** | (PK) | Identificador único. |
| | **negocio\_id** | (FK a NEGOCIO) | Asocia el almacén a un negocio. |
| | **nombre** | (VARCHAR) | Nombre del almacén. |
| **PRODUCTO** | **id** | (PK) | Identificador único del registro de stock. |
| | **almacen\_id** | (FK a ALMACEN) | Ubicación del stock. |
| | **sku** | (VARCHAR) | Código del producto. (Debe ser **Único** por `almacen_id`). |
| | **stock\_actual** | (DECIMAL) | Cantidad disponible (Valor calculado para **lectura rápida**). |
| | **nombre** | (VARCHAR) | Nombre del producto. |
| | **precio\_venta** | (DECIMAL) | Precio estándar de venta. |

### 4.3. Entidades de Transacciones (Auditoría)

| Tabla | Columna | Tipo de Dato | Observación |
| :--- | :--- | :--- | :--- |
| **MOVIMIENTO** | **id** | (PK) | Identificador único. |
| | **producto\_id** | (FK a PRODUCTO) | Producto cuyo stock se afecta. |
| | **almacen\_id** | (FK a ALMACEN) | Almacén de origen/destino. |
| | **tipo** | (ENUM/VARCHAR) | **'ENTRADA'** o **'SALIDA'**. |
| | **cantidad** | (DECIMAL) | Cantidad movida. |
| | **usuario\_id** | (FK a USUARIO) | Usuario que realizó la acción (**Auditoría**). |
| | **fecha** | (TIMESTAMP) | Fecha y hora de la transacción. |

---

## 5. Puntos Críticos de Implementación (Backend MVP)

Estos puntos son cruciales para el éxito del proyecto en el plazo de dos semanas:

1.  **Seguridad y Contexto:** Implementar **Spring Security y JWT** es la prioridad #1 para autenticar al usuario y obtener su `negocio_id` y `rol_id` en cada solicitud.
2.  **Lógica Transaccional:** La actualización del `PRODUCTO.stock_actual` debe ser envuelta en una transacción (usando `@Transactional` en el `ProductoService`) cada vez que se crea un `MOVIMIENTO` para evitar inconsistencias en el stock.
3.  **Aislamiento de Datos:** Asegurar que **todas** las consultas a `PRODUCTO`, `ALMACEN` y `MOVIMIENTO` incluyan el filtro `WHERE negocio_id = [usuario_actual.negocio_id]`.