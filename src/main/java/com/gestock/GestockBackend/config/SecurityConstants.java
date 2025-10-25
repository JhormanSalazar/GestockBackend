package com.gestock.GestockBackend.config;

/**
 * Constantes de seguridad para roles y endpoints
 */
public class SecurityConstants {

    // ==================== ROLES ====================
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_BUSINESS_OWNER = "BUSINESS_OWNER";
    public static final String ROLE_COLLABORATOR = "COLLABORATOR";

    // ==================== AUTH ENDPOINTS ====================
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String AUTH_LOGIN = "/auth/login";

    // ==================== BUSINESS ENDPOINTS ====================
    public static final String BUSINESSES = "/businesses";
    public static final String BUSINESSES_ID = "/businesses/{id}";

    // ==================== WAREHOUSE ENDPOINTS ====================
    public static final String WAREHOUSES = "/warehouses";
    public static final String WAREHOUSES_ID = "/warehouses/{id}";
    public static final String WAREHOUSES_BY_BUSINESS = "/warehouses/by-business/{businessId}";

    // ==================== PRODUCT ENDPOINTS ====================
    public static final String PRODUCTS = "/products";
    public static final String PRODUCTS_ID = "/products/{id}";

    // ==================== USER ENDPOINTS ====================
    public static final String USERS = "/users";
    public static final String USERS_ID = "/users/{id}";
    public static final String USERS_BY_BUSINESS = "/users/by-business/{businessId}";

    // ==================== WAREHOUSE-PRODUCT ENDPOINTS ====================
    public static final String WAREHOUSE_PRODUCTS = "/warehouse-products";
    public static final String WAREHOUSE_PRODUCTS_BY_BUSINESS = "/warehouse-products/by-business";
    public static final String WAREHOUSE_PRODUCTS_BY_WAREHOUSE = "/warehouse-products/by-warehouse/{warehouseId}";
    public static final String WAREHOUSE_PRODUCTS_ID = "/warehouse-products/{productId}/{warehouseId}";

    // ==================== TRANSACTION ENDPOINTS ====================
    public static final String TRANSACTIONS = "/transactions";
    public static final String TRANSACTIONS_ID = "/transactions/{id}";
    public static final String TRANSACTIONS_BY_BUSINESS = "/transactions/by-business";
    public static final String TRANSACTIONS_BY_WAREHOUSE = "/transactions/by-warehouse/{warehouseId}";
    public static final String TRANSACTIONS_BY_PRODUCT = "/transactions/by-product/{productId}";

    // ==================== H2 CONSOLE (Development only) ====================
    public static final String H2_CONSOLE = "/h2-console/**";

    private SecurityConstants() {
        // Private constructor to prevent instantiation
    }
}
