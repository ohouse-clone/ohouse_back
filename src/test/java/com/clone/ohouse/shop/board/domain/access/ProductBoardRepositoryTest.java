package com.clone.ohouse.shop.board.domain.access;

import com.clone.ohouse.shop.board.domain.access.ProductBoardRepository;
import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Product;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductBoardRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductBoardRepository productBoardRepository;

    @BeforeEach
    public void previouslySetup(){
        productRepository.save(new Product());
    }

    @AfterEach
    public void clean(){
        productBoardRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void save(){
        //given
        String title = "제목";
        String content = "내용";
        String author = "나";

        ProductBoard productBoard = new ProductBoard(title, content.getBytes(StandardCharsets.UTF_8), author, null);

        //when
        productBoardRepository.save(productBoard);

        //then
        List<ProductBoard> all = productBoardRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(productBoard.getTitle());
        Assertions.assertThat(all.get(0).getAuthor()).isEqualTo(productBoard.getAuthor());
    }


    @Test
    void delete(){
        //given
        String title = "제목";
        String content = "내용";
        String author = "나";

        ProductBoard productBoard = new ProductBoard(title, content.getBytes(StandardCharsets.UTF_8), author, null);
        ProductBoard saved = productBoardRepository.save(productBoard);
        //when
        List<ProductBoard> all = productBoardRepository.findAll();

        productBoardRepository.delete(all.get(0));

        Assertions.assertThat(productBoardRepository.count()).isEqualTo(0);
    }
}
