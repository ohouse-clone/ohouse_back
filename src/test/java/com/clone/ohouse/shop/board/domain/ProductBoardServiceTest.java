package com.clone.ohouse.shop.board.domain;

import com.clone.ohouse.shop.board.domain.access.ProductBoardRepository;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardResponseDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardSaveRequestDto;
import com.clone.ohouse.shop.board.domain.dto.ProductBoardUpdateRequestDto;
import com.clone.ohouse.shop.board.domain.entity.ProductBoard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductBoardServiceTest {
    @Autowired
    private ProductBoardService boardService;

    @Autowired
    private ProductBoardRepository productBoardRepository;

    @AfterEach
    void clean(){
        productBoardRepository.deleteAll();
    }

    @Test
    void save(){
        //given
        ProductBoardSaveRequestDto dto = createProductBoardSaveRequest();

        //when
        Long savedId = boardService.save(dto);

        //then
        List<ProductBoard> all = productBoardRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
        Assertions.assertThat(all.get(0).getId()).isEqualTo(savedId);
    }

    @Test
    void update(){
        //given
        String title = "불행한 마을";
        String content = "내용 없음";
        String modifiedUser = "쿼터";

        ProductBoardSaveRequestDto dto = createProductBoardSaveRequest();
        Long savedId = boardService.save(dto);
        ProductBoardUpdateRequestDto updateDto = ProductBoardUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .modifiedUser(modifiedUser)
                .isActive(true)
                .isDeleted(false)
                .build();

        //when
        boardService.update(savedId, updateDto);

        //then
        ProductBoard productBoard = productBoardRepository.findById(savedId).orElseThrow(() -> new NoSuchElementException("찾으려는 판매글이 없습니다."));

        Assertions.assertThat(productBoard.getTitle()).isEqualTo(title);
        Assertions.assertThat(new String(productBoard.getContent(), StandardCharsets.UTF_8)).isEqualTo(content);
        Assertions.assertThat(productBoard.getModifiedUser()).isEqualTo(modifiedUser);
    }

    @Test
    void delete(){
        //given
        ProductBoardSaveRequestDto dto = createProductBoardSaveRequest();
        Long savedId = boardService.save(dto);

        //when
        boardService.delete(savedId);

        //then
        long count = productBoardRepository.count();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    void findById(){
        //given
        ProductBoardSaveRequestDto dto = createProductBoardSaveRequest();
        Long savedId = boardService.save(dto);

        //when
        ProductBoardResponseDto responseDto = boardService.findById(savedId);


        //then
        Assertions.assertThat(responseDto.getTitle()).isEqualTo("행복한 마을");
    }

    private ProductBoardSaveRequestDto createProductBoardSaveRequest(){
        String title = "행복한 마을";
        String content = "One of the early announcements out of Google I/O 2017's keynote was the launch of google.ai, with which Google aims to \"bring the benefits of AI to everyone\". Google.ai is a collection of products and teams across Alphabet with a focus on AI.\n" +
                "\n" +
                "\n" +
                "\n" +
                "There are several projects under Google.ai underway already, including using neural nets to build better neural nets and a particularly exciting project that looks to bring AI detection to healthcare. Google has worked with pathologists to build neural nets that can detect spreading cancer in lymph nodes with 85% accuracy compared to 70% with previous human methods. Google confessed a higher false positive rate currently, but the net is fast-learning.\n" +
                "\n" +
                "Another biomedical application from Google.ai is improving the accuracy of DNA sequencing and helping unlock the wonders of our genetic code. Machine learning is also to predict the properties of molecules, cutting computing time by orders of magnitude, with Sundar Pichai saying \"I'm confident that AI will invent new molecules that define in predetermined ways.\"\n" +
                "\n" +
                "Not everything in Google.ai is new and potentially life-saving. Google.ai also powers a feature we've already seen and loved: Google auto-draw.\n" +
                "\n" +
                "Stay tuned to more out of Google I/O from Android Central!\n" +
                "\n";
        String author = "고순무";

        return new ProductBoardSaveRequestDto(title, content, author);
    }

}
