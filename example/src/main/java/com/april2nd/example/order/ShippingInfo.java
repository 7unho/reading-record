package com.april2nd.example.order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ShippingInfo {
    private String address;
    private String message;

    @Builder
    public ShippingInfo(String address, String message) {
        this.address = address;
        this.message = message;
    }
}
