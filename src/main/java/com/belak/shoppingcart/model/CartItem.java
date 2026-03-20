package com.belak.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE )
    private  Long id ;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product ;

    private  int quantity ;
    private BigDecimal unitPrice  ;
    private BigDecimal totalPrice ;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart ;

    public void setTotalPrice()
    {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }

}
