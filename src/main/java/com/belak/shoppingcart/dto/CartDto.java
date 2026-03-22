package com.belak.shoppingcart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CartDto {
    private Long cartId ;
    private BigDecimal totalAmount ;
    private List<CartItemDto> items ;
}
