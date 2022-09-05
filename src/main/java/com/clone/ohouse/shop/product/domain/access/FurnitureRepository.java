package com.clone.ohouse.shop.product.domain.access;

import com.clone.ohouse.shop.product.domain.entity.Furniture;
import com.clone.ohouse.shop.product.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
    List<Furniture> findByName(String name);

    @Query("select i from Furniture i order by i.name ASC")
    List<Furniture> findAllAsc();
}
