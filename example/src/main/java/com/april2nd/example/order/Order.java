package com.april2nd.example.order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Order {
    private OrderStatus state;
    private ShippingInfo shippingInfo;

    @Builder
    public Order(OrderStatus state, ShippingInfo shippingInfo) {
        this.state = state;
        this.shippingInfo = shippingInfo;
    }

    public void changeShippingInfo(ShippingInfo newShippingInfo) {
        //
        if (!state.isShippingChangeable()) {
            throw new IllegalStateException("can't change shipping in " + state);
        }

        this.shippingInfo = newShippingInfo;
    }
}
