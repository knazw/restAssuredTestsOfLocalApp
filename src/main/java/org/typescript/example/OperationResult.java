package org.typescript.example;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class OperationResult {
    public int valueSet;
    public String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public LocalDateTime time;

    public OperationResult() {

    }

    public OperationResult(int valueSet, String message, LocalDateTime time) {
        this.valueSet = valueSet;
        this.message = message;
        this.time = time;


    }

    public int getValueSet() {
        return valueSet;
    }

    public void setValueSet(int valueSet) {
        this.valueSet = valueSet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "OperationResult{" +
                "valueSet='" + valueSet + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
