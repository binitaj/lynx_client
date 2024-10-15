package com.binitajha.lynx.client.model;


import java.io.Serializable;

public class Secret implements Serializable {

    public String id;
    public String key;
    public String data;
    public String encrypted;

    public Secret() {

    }
    public Secret(Secret other) {
        this.id = other.id;
        this.data = other.data;
        this.key = other.key;
        this.encrypted = other.encrypted;
    }

}