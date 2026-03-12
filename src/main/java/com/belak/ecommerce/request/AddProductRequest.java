package com.belak.ecommerce.request;

import com.belak.ecommerce.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddProductRequest {

    private String name ;
    private  String brand ;
    private BigDecimal price ;
    private int inventory ;
    private  String description ;
    private Category category ;
}
