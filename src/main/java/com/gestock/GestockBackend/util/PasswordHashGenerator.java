package com.gestock.GestockBackend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt de contraseñas
 * Útil para crear contraseñas hasheadas manualmente para scripts SQL
 */
public class PasswordHashGenerator {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Genera un hash BCrypt de la contraseña proporcionada
     * @param plainPassword Contraseña en texto plano
     * @return Hash BCrypt de la contraseña
     */
    public static String generateHash(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash
     * @param plainPassword Contraseña en texto plano
     * @param hashedPassword Hash BCrypt almacenado
     * @return true si coinciden, false en caso contrario
     */
    public static boolean matches(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    /**
     * Método main para generar hashes desde la línea de comandos
     * Uso: java PasswordHashGenerator <contraseña>
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("=".repeat(60));
            System.out.println("  PASSWORD HASH GENERATOR - BCrypt");
            System.out.println("=".repeat(60));
            System.out.println();

            // Ejemplos por defecto
            String[] defaultPasswords = {"admin123", "password123", "test1234"};

            System.out.println("Generando hashes de ejemplo:");
            System.out.println();

            for (String password : defaultPasswords) {
                String hash = generateHash(password);
                System.out.println("Contraseña: " + password);
                System.out.println("Hash BCrypt: " + hash);
                System.out.println("-".repeat(60));
            }

            System.out.println();
            System.out.println("Uso: java PasswordHashGenerator <tu_contraseña>");
            System.out.println("Ejemplo: java PasswordHashGenerator miContraseña123");
        } else {
            String password = args[0];
            String hash = generateHash(password);

            System.out.println("=".repeat(60));
            System.out.println("  PASSWORD HASH GENERATOR - BCrypt");
            System.out.println("=".repeat(60));
            System.out.println();
            System.out.println("Contraseña original: " + password);
            System.out.println("Hash BCrypt generado:");
            System.out.println(hash);
            System.out.println();
            System.out.println("Copia este hash para usar en tu base de datos o script SQL");
            System.out.println("=".repeat(60));
        }
    }
}
