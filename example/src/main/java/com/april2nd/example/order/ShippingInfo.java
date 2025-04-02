package com.april2nd.example.order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ShippingInfo {
    private String receiverName;
    private String receiverPhoneNumber;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingZipcode;

    @Builder

    public ShippingInfo(
            String receiverName,
            String receiverPhoneNumber,
            String shippingAddress1,
            String shippingAddress2,
            String shippingZipcode) {
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.shippingAddress1 = shippingAddress1;
        this.shippingAddress2 = shippingAddress2;
        this.shippingZipcode = shippingZipcode;
    }
}
