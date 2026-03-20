package com.belak.shoppingcart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id ;
    private  String firstName ;
    private String lastName ;
    @NaturalId
    private String email ;
    private  String password ;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Cart cart ;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Order> order = new ArrayList<>();
}
