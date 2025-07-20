package org.cypress.example.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllureLoggerUtils {
    private static final Logger logger = LoggerFactory.getLogger(AllureLoggerUtils.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FULL_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Metoda 1: Timestamp w nazwie kroku
    @Step("[{timestamp}] {stepName}")
    public static void logStepWithTimestamp(String stepName) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        logger.info("[{}] Wykonywanie kroku: {}", timestamp, stepName);

        Allure.addAttachment("Log z timestampem", "text/plain", String.format("[%s] %s", timestamp, stepName));
    }

    // Metoda 2: Attachment z timestampem
    public static void addTimestampedLog(String message) {
        String timestamp = LocalDateTime.now().format(FULL_TIMESTAMP);
        String logMessage = String.format("[%s] %s", timestamp, message);

        // Dodanie do logów konsoli
        logger.info(logMessage);

        // Dodanie jako Allure attachment
        Allure.addAttachment("Log z timestampem", "text/plain", logMessage);
    }

    // Metoda 3: Krok z szczegółowym attachmentem
    @Step("Krok: {stepDescription}")
    public static void createTimestampedStep(String stepDescription, String details) {
        String timestamp = LocalDateTime.now().format(FULL_TIMESTAMP);

        // Log do konsoli
        logger.info("[{}] {}: {}", timestamp, stepDescription, details);

        // Szczegółowy attachment
        String attachmentContent = String.format(
                "Timestamp: %s%nKrok: %s%nSzczegoly: %s",
                timestamp, stepDescription, details
        );

        Allure.addAttachment("Szczegoly kroku z timestampem", "text/plain", attachmentContent);
    }

    // Metoda 4: Dynamiczny krok z timestampem
    public static void dynamicTimestampedStep(String action, Object... params) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String formattedAction = String.format(action, params);

        Allure.step(String.format("[%s] %s", timestamp, formattedAction), () -> {
            logger.info("[{}] Wykonywanie: {}", timestamp, formattedAction);
        });
    }
}
