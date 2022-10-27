package com.clone.ohouse.store.domain;


import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsResponseDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsUpdateRequestDto;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class StorePostsService {
    private final StorePostsRepository storePostsRepository;

    @Transactional
    public Long save(StorePostsSaveRequestDto saveRequestDto){
        return storePostsRepository.save(saveRequestDto.toEntity()).getId();
    }

    @Transactional
    public Long delete(Long id){
        storePostsRepository.deleteById(id);

        return id;
    }

    @Transactional
    public Long update(Long id, StorePostsUpdateRequestDto dto){
        StorePosts entity = storePostsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        entity.update(dto.isActive(), dto.getTitle(), dto.getPreviewImageUrl(), dto.getContentUrl(), dto.getModifiedUser(), dto.isDeleted());

        return storePostsRepository.save(entity).getId();
    }

    @Transactional
    public StorePostsResponseDto findById(Long id){
        StorePosts entity = storePostsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        return new StorePostsResponseDto(entity);
    }


}
