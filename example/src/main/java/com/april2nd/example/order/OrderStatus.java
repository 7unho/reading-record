package com.april2nd.example.order;

public enum OrderStatus {
    PAYMENT_WAITING {
        public boolean isShippingChangeable() {
            return true;
        }
    },
    PREPARING {
        public boolean isShippingChangeable() {
            return true;
        }
    },
    SHIPPED, DELIVERING, DELIVERY_COMPLETED;

    /*
    1. 주문 취소는 배송 전에만 할 수 있다.
    2. 출고 전에 배송지를 변경할 수 있다.
     */
    public boolean isShippingChangeable() {
        return false;
    }
}
