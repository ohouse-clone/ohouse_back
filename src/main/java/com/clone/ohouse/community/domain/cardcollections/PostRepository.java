package com.clone.ohouse.community.domain.cardcollections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    public Optional<Post> findByTitle(String title);
    public void deleteByTitle(String title);


}
