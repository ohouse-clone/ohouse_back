//package com.clone.ohouse.community.domain;
//
//import com.clone.ohouse.community.domain.cardcollections.Card;
//import com.clone.ohouse.community.domain.cardcollections.CardRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//@Transactional
//public class PostService {
//
//    @Autowired
//    private final CardRepository cardRepository;
//
//    public List<Card> findAll() {
//        List<Card> cards = new ArrayList<>();
//        cardRepository.findAll().forEach(s -> cards.add(s));
//        return cards;
//    }
//
//    public Optional<Card> findByTitle(String title) {
//        Optional<Card> post = cardRepository.findByTitle(title);
//        return post;
//    }
//
//    public void deleteByTitle(String title) {
//        Optional<Card> deletePost = cardRepository.findByTitle(title);
//        cardRepository.delete(deletePost.orElseThrow(() ->
//                new NoSuchElementException("POST NOT FOUND")));
//    }
//
//    public void deleteById(Long id) {
//        cardRepository.deleteById(id);
//    }
//
//    public Card save(Card card) {
//        cardRepository.save(card);
//        return card;
//    }
//
//    public void updatePost(String title) {
//        Optional<Card> post = cardRepository.findByTitle(title);
//        post.ifPresent(newPost -> {
//            post.get().setTitle(newPost.getTitle());
//            post.get().setContent(newPost.getContent());
//            post.get().setAuthor(newPost.getAuthor());
//            post.get().setId(newPost.getId());
//        });
//    }
//
//
//}
