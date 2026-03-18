package com.belak.shoppingcart.dto;

import com.belak.shoppingcart.model.Category;
import com.belak.shoppingcart.model.Image;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private Long id ;
    private String name ;
    private  String brand ;
    private BigDecimal price ;
    private int inventory ;
    private  String description ;

    private Category category ;
    private List<ImageDto> images ;
}
