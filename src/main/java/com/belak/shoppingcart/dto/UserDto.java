package com.belak.shoppingcart.dto;

import com.belak.shoppingcart.model.Cart;
import com.belak.shoppingcart.model.Order;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private  Long id ;
    private String firstName ;
    private String lastName ;
    private  String email ;
    private List<OrderDto> order = new ArrayList<>() ;
    private CartDto cart ;
}
