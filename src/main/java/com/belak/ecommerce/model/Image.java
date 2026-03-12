package com.belak.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE )//, generator = "user_seq")
    //@SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ",
      //    allocationSize = 1)
    private  Long id  ;
    private String fileName ;
    private  String fileType ;
    @Lob
    private Blob blob ;
    private String downloadUrl ;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product ;

}
