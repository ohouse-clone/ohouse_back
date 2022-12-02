package com.clone.ohouse.community.domain.cardcollections;

import com.clone.ohouse.community.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
}
