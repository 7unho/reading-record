package com.april2nd.example.order;

import com.april2nd.example.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderLine {
    private Product product;
    private int price;
    private int quantity;
    private int amounts;

    @Builder
    public OrderLine(Product product, int price, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.amounts = calculateAmounts();
    }

    private int calculateAmounts() {
        return price * quantity;
    }

}
