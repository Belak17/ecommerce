package com.belak.ecommerce.repository;

import com.belak.ecommerce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
