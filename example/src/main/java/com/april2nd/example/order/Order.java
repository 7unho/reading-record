package com.april2nd.example.order;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Order {
    private OrderStatus state;
    private ShippingInfo shippingInfo;
    private List<OrderLine> orderLines;
    private Money totalAmounts;
    @Builder
    public Order(OrderStatus state, List<OrderLine> orderLines, ShippingInfo shippingInfo) {
        this.state = state;
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        if (shippingInfo == null) {
            throw new IllegalArgumentException("no shipping info");
        }
        this.shippingInfo = shippingInfo;
    }

    private void setOrderLines(List<OrderLine> orderLines) {
        verifyAtLeastOneOrMoreOrderLines(orderLines);
        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    /*
        총 주문 금액 계산
     */
    private void calculateTotalAmounts() {
        int sum = orderLines.stream()
                .mapToInt(x -> x.getAmounts())
                .sum();
        this.totalAmounts = new Money(sum);
    }

    /*
        요구사항. 최소 한 종류 이상의 상품을 주문해야 한다.
     */
    private void verifyAtLeastOneOrMoreOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("no OrderLine");
        }
    }

    public void changeShippingInfo(ShippingInfo newShippingInfo) {
        verifyNotYetShipped();
        setShippingInfo(newShippingInfo);
    }

    private void verifyNotYetShipped() {
        if (state.isShippingChangeable()) {
            throw new IllegalStateException("already shipped");
        }
    }

    public void changeShipped() {

    }

    public void cancel() {

    }

    public void completePayment() {

    }
}
