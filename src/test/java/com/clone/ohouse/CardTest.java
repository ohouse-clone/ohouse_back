//package com.clone.ohouse;
//
//import com.clone.ohouse.community.domain.cardcollections.Card;
//import com.clone.ohouse.community.domain.cardcollections.CardRepository;
//import com.clone.ohouse.account.domain.user.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//@SpringBootTest
//@Transactional
//
//public class CardTest {
//    @Autowired
//    CardRepository cardRepository;
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    public void createPost() throws Exception{
//        Card card = Card.builder()
//                .author("user1")
//                .title("newpost")
//                .content("thisisnew")
//                .build();
//        Card card2 = Card.builder()
//                .author("uwer2")
//                .title("post2")
//                .content("newone")
//                .build();
//
//        Card new2 = cardRepository.save(card2);
//        //저장
//        Card newCard = cardRepository.save(card);
//        //조회
//        Card result = cardRepository.findById(card.getId()).get();
//        Optional<Card> findByIdPost  = cardRepository.findById(card.getId());
//        Optional<Card> findByTitlePost = cardRepository.findByTitle(card.getTitle());
//
//        assertThat(findByIdPost.get().getId()).isEqualTo(card.getId());
//        assertThat(findByIdPost.get()).isEqualTo(card);
//        assertThat(findByTitlePost.get().getId()).isEqualTo(card.getId());
//        assertThat(findByTitlePost.get()).isEqualTo(card);
//    }
//
//    @Test
//    public void CRUD() throws Exception{
//        Card card1 = Card.builder()
//                .author("author1")
//                .title("post1")
//                .content("newpost1")
//                .build();
//
//        Card card2 = Card.builder()
//                .author("author2")
//                .title("post2")
//                .content("newpost2")
//                .build();
//
//        cardRepository.save(card1);
//        cardRepository.save(card2);
//        Card findCard1 = cardRepository.findById(card1.getId()).get();
//        Card findCard2 = cardRepository.findById(card2.getId()).get();
//        assertThat(findCard1).isEqualTo(card1);
//        assertThat(findCard2).isEqualTo(card2);
//
//        List<Card> cards = cardRepository.findAll();
//        long count = cardRepository.count();
//
//        assertThat(cards.size()).isEqualTo(count);
//        assertThat(cards.size()).isEqualTo(2);
//
//        cardRepository.delete(card1);
//        cardRepository.delete(card2);
//        long deletedCount = cardRepository.count();
//        assertThat(deletedCount).isEqualTo(0);
//
//    }
//}
