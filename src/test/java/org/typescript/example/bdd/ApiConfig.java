package org.typescript.example.bdd;

public class ApiConfig {
    public static final String BASE_URI = System.getProperty("api.baseUri", "http://localhost");
    public static final int PORT = Integer.parseInt(System.getProperty("api.port", "8089"));
}
