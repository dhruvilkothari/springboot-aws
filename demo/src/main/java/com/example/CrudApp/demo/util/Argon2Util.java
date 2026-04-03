package com.example.CrudApp.demo.util;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.SecureRandom;
import java.util.Base64;

public class Argon2Util {

    private static final SecureRandom random = new SecureRandom();

    public static String hash(String password) {
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withIterations(3)      // number of iterations
                .withMemoryAsKB(65536)  // 64 MB
                .withParallelism(1)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);

        byte[] hash = new byte[32];
        generator.generateBytes(password.toCharArray(), hash);

        return Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verify(String password, String stored) {
        String[] parts = stored.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] originalHash = Base64.getDecoder().decode(parts[1]);

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withIterations(3)
                .withMemoryAsKB(65536)
                .withParallelism(1)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);

        byte[] newHash = new byte[32];
        generator.generateBytes(password.toCharArray(), newHash);

        return java.util.Arrays.equals(originalHash, newHash);
    }
}