package com.cartobucket.auth.data.domain;

public class Identifier {
    private String system;
    private String value;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
