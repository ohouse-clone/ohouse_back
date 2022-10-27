package com.clone.ohouse.store.store.domain;

import com.clone.ohouse.store.domain.StorePostsService;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsResponseDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsUpdateRequestDto;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StorePostsServiceTest {
    @Autowired
    private StorePostsService boardService;

    @Autowired
    private StorePostsRepository storePostsRepository;

    @AfterEach
    void clean() {
        storePostsRepository.deleteAll();
    }

    @Test
    void save() {
        //given
        StorePostsSaveRequestDto dto = createProductBoardSaveRequest();

        //when
        Long savedId = boardService.save(dto);

        //then
        List<StorePosts> all = storePostsRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
        Assertions.assertThat(all.get(0).getId()).isEqualTo(savedId);
    }

    @Test
    void update() {
        //given
        String title = "불행한 마을";
        String contentUrl = "내용 없음";
        String modifiedUser = "쿼터";

        StorePostsSaveRequestDto dto = createProductBoardSaveRequest();
        Long savedId = boardService.save(dto);
        StorePostsUpdateRequestDto updateDto = StorePostsUpdateRequestDto.builder()
                .title(title)
                .contentUrl(contentUrl)
                .modifiedUser(modifiedUser)
                .isActive(true)
                .isDeleted(false)
                .build();

        //when
        boardService.update(savedId, updateDto);

        //then
        StorePosts storePosts = storePostsRepository.findById(savedId).orElseThrow(() -> new NoSuchElementException("찾으려는 판매글이 없습니다."));

        Assertions.assertThat(storePosts.getTitle()).isEqualTo(title);
        Assertions.assertThat(storePosts.getContentUrl()).isEqualTo(contentUrl);
        Assertions.assertThat(storePosts.getModifiedUser()).isEqualTo(modifiedUser);
    }

    @Test
    void delete() {
        //given
        StorePostsSaveRequestDto dto = createProductBoardSaveRequest();
        Long savedId = boardService.save(dto);

        //when
        boardService.delete(savedId);

        //then
        long count = storePostsRepository.count();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    void findById() {
        //given
        StorePostsSaveRequestDto dto = createProductBoardSaveRequest();
        Long savedId = boardService.save(dto);

        //when
        StorePostsResponseDto responseDto = boardService.findById(savedId);


        //then
        Assertions.assertThat(responseDto.getTitle()).isEqualTo("행복한 마을");
    }

    private StorePostsSaveRequestDto createProductBoardSaveRequest() {
        String title = "행복한 마을";
        String contentUrl = "url";
        String author = "고순무";

        return new StorePostsSaveRequestDto(title, contentUrl, null, author);
    }



}
