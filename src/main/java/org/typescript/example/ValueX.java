package org.typescript.example;

public class ValueX {
    private String variableName;

    private int value;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ValueX(String variableName, int value) {
        this.variableName = variableName;
        this.value = value;
    }

}
