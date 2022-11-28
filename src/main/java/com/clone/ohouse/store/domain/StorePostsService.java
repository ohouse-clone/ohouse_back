package com.clone.ohouse.store.domain;


import com.clone.ohouse.store.domain.product.dto.ProductDetailDto;
import com.clone.ohouse.store.domain.storeposts.StorePostPictures;
import com.clone.ohouse.store.domain.storeposts.StorePostPicturesRepository;
import com.clone.ohouse.store.domain.storeposts.StorePostsRepository;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostWithProductsDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsResponseDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.store.domain.storeposts.dto.StorePostsUpdateRequestDto;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StorePostsService {
    private final StorePostsRepository storePostsRepository;
    private final StorePostPicturesRepository storePostPicturesRepository;
    @Transactional
    public Long save(StorePostsSaveRequestDto saveRequestDto){
        StorePosts storePost = new StorePosts(saveRequestDto.getTitle(), null, saveRequestDto.getAuthor(), null, null);

        List<StorePostPictures> pictures = storePostPicturesRepository.findAllById(new ArrayList<>(List.of(saveRequestDto.getPreviewImageId(), saveRequestDto.getContentImageId())));
        for (StorePostPictures picture : pictures) {


            picture.registerStorePost(storePost);
        }
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

    // TODO: controller 필요
    @Transactional
    public StorePostWithProductsDto findByIdWithProduct(Long id) throws Exception{
        StorePosts post = storePostsRepository.findByIdWithFetchJoinProduct(id);
        if(post == null) throw new NoSuchElementException("잘못된 Storepost id : " + id);

        return new StorePostWithProductsDto(post,
                post.getProductList()
                        .stream()
                        .map((p) -> new ProductDetailDto(p))
                        .collect(Collectors.toCollection(ArrayList<ProductDetailDto>::new)));
    }
}
