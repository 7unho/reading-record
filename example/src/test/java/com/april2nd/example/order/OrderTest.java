package com.april2nd.example.order;

import com.april2nd.example.product.Product;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {
    private ShippingInfo createShippingInfo(String receiver, String address) {
        return ShippingInfo.builder()
                .receiverName(receiver)
                .shippingAddress1(address)
                .build();
    }

    private List<OrderLine> createOrderLines() {
        return List.of(
                OrderLine.builder()
                        .product(new Product(100L))
                        .price(1000)
                        .quantity(42)
                        .build()
        );
    }
    @Test
    public void 주문_대기_중_PAYMENT_WAITING_이라면_주문_변경이_가능하다() throws Exception {
        // given
        ShippingInfo before = createShippingInfo("before", "SEOUL");
        ShippingInfo after = createShippingInfo("AFTER SHIPPING", "INCHEON");

        Order order = Order.builder()
                .state(OrderStatus.PAYMENT_WAITING)
                .shippingInfo(before)
                .orderLines(createOrderLines())
                .build();
        // when
        order.changeShippingInfo(after);
        // then
        assertAll(
                () -> assertThat(order.getShippingInfo().getShippingAddress1()).isEqualTo("INCHEON"),
                () -> assertThat(order.getShippingInfo().getReceiverName()).isEqualTo("AFTER SHIPPING")
        );
    }

    @Test
    public void 상품_준비_중_PREPARING_이라면_주문_변경이_가능하다() throws Exception {
        // given
        ShippingInfo before = createShippingInfo("before", "SEOUL");
        ShippingInfo after = createShippingInfo("AFTER SHIPPING", "INCHEON");

        Order order = Order.builder()
                .state(OrderStatus.PREPARING)
                .shippingInfo(before)
                .orderLines(createOrderLines())
                .build();
        // when
        order.changeShippingInfo(after);
        // then
        assertAll(
                () -> assertThat(order.getShippingInfo().getShippingAddress1()).isEqualTo("INCHEON"),
                () -> assertThat(order.getShippingInfo().getReceiverName()).isEqualTo("AFTER SHIPPING")
        );
    }

    @Test
    public void 출고되었다면_SHIPPED_주문_변경이_불가능하다() throws Exception {
        // given
        ShippingInfo before = createShippingInfo("before", "SEOUL");
        ShippingInfo after = createShippingInfo("AFTER SHIPPING", "INCHEON");

        Order order = Order.builder()
                .state(OrderStatus.SHIPPED)
                .shippingInfo(before)
                .orderLines(createOrderLines())
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
        ShippingInfo before = createShippingInfo("before", "SEOUL");
        ShippingInfo after = createShippingInfo("AFTER SHIPPING", "INCHEON");

        Order order = Order.builder()
                .state(OrderStatus.DELIVERING)
                .shippingInfo(before)
                .orderLines(createOrderLines())
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
        ShippingInfo before = createShippingInfo("before", "SEOUL");
        ShippingInfo after = createShippingInfo("AFTER SHIPPING", "INCHEON");

        Order order = Order.builder()
                .state(OrderStatus.DELIVERY_COMPLETED)
                .shippingInfo(before)
                .orderLines(createOrderLines())
                .build();
        // when & then
        assertThatThrownBy(() -> {
            order.changeShippingInfo(after);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 상품목록이_없는_주문이라면_exception_을_반환한다() throws Exception {
        // given
        ShippingInfo before = createShippingInfo("before", "SEOUL");

        // when & then
        assertThatThrownBy(() -> {
            Order order = Order.builder()
                    .state(OrderStatus.PREPARING)
                    .shippingInfo(before)
                    .build();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문정보가_없는_주문이라면_exception_을_반환한다() throws Exception {
        // given
        // when & then
        assertThatThrownBy(() -> {
            Order order = Order.builder()
                    .state(OrderStatus.PREPARING)
                    .orderLines(createOrderLines())
                    .build();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}