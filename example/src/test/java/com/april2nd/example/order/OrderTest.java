package com.april2nd.example.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {
    @Test
    public void 주문_대기_중_PAYMENT_WAITING_이라면_주문_변경이_가능하다() throws Exception {
        // given
        ShippingInfo before = ShippingInfo.builder()
                .message("BEFORE SHIPPING")
                .address("SEOUL")
                .build();
        ShippingInfo after = ShippingInfo.builder()
                .message("AFTER SHIPPING")
                .address("INCHEON")
                .build();

        Order order = Order.builder()
                .state(OrderStatus.PAYMENT_WAITING)
                .shippingInfo(before)
                .build();
        // when
        order.changeShippingInfo(after);
        // then
        assertAll(
                () -> assertThat(order.getShippingInfo().getAddress()).isEqualTo("INCHEON"),
                () -> assertThat(order.getShippingInfo().getMessage()).isEqualTo("AFTER SHIPPING")
        );
    }

    @Test
    public void 상품_준비_중_PREPARING_이라면_주문_변경이_가능하다() throws Exception {
        // given
        ShippingInfo before = ShippingInfo.builder()
                .message("BEFORE SHIPPING")
                .address("SEOUL")
                .build();
        ShippingInfo after = ShippingInfo.builder()
                .message("AFTER SHIPPING")
                .address("INCHEON")
                .build();

        Order order = Order.builder()
                .state(OrderStatus.PREPARING)
                .shippingInfo(before)
                .build();
        // when
        order.changeShippingInfo(after);
        // then
        assertAll(
                () -> assertThat(order.getShippingInfo().getAddress()).isEqualTo("INCHEON"),
                () -> assertThat(order.getShippingInfo().getMessage()).isEqualTo("AFTER SHIPPING")
        );
    }

    @Test
    public void 출고되었다면_SHIPPED_주문_변경이_불가능하다() throws Exception {
        // given
        ShippingInfo before = ShippingInfo.builder()
                .message("BEFORE SHIPPING")
                .address("SEOUL")
                .build();
        ShippingInfo after = ShippingInfo.builder()
                .message("AFTER SHIPPING")
                .address("INCHEON")
                .build();

        Order order = Order.builder()
                .state(OrderStatus.SHIPPED)
                .shippingInfo(before)
                .build();
        // when
        // then
        assertThatThrownBy(() -> {
            order.changeShippingInfo(after);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 배송중이라면_DELIVERING_주문_변경이_불가능하다() throws Exception {
        // given
        ShippingInfo before = ShippingInfo.builder()
                .message("BEFORE SHIPPING")
                .address("SEOUL")
                .build();
        ShippingInfo after = ShippingInfo.builder()
                .message("AFTER SHIPPING")
                .address("INCHEON")
                .build();

        Order order = Order.builder()
                .state(OrderStatus.DELIVERING)
                .shippingInfo(before)
                .build();
        // when
        // then
        assertThatThrownBy(() -> {
            order.changeShippingInfo(after);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 배송이_완료되었다면_DELIVERY_COMPLETED_주문_변경이_불가능하다() throws Exception {
        // given
        ShippingInfo before = ShippingInfo.builder()
                .message("BEFORE SHIPPING")
                .address("SEOUL")
                .build();
        ShippingInfo after = ShippingInfo.builder()
                .message("AFTER SHIPPING")
                .address("INCHEON")
                .build();

        Order order = Order.builder()
                .state(OrderStatus.DELIVERY_COMPLETED)
                .shippingInfo(before)
                .build();
        // when
        // then
        assertThatThrownBy(() -> {
            order.changeShippingInfo(after);
        }).isInstanceOf(IllegalStateException.class);
    }
}