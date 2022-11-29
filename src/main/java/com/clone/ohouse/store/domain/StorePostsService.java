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
import com.clone.ohouse.utility.s3.S3File;
import com.clone.ohouse.utility.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StorePostsService {
    private final StorePostsRepository storePostsRepository;
    private final StorePostPicturesRepository storePostPicturesRepository;
    private final S3Service s3Service;
    @Transactional
    public Long save(StorePostsSaveRequestDto saveRequestDto) throws IOException {
        StorePosts storePost = new StorePosts(saveRequestDto.getTitle(), null, saveRequestDto.getAuthor(), null, null);

        List<S3File> s3files = new ArrayList<>();
        List<StorePostPictures> pictures = storePostPicturesRepository.findAllById(new ArrayList<>(List.of(saveRequestDto.getPreviewImageId(), saveRequestDto.getContentImageId())));
        for (StorePostPictures picture : pictures) {
            S3File keyValue = null;
            File image = new File(picture.getLocalPath());
            log.debug("storepost save with image : " + picture.getLocalPath());

            picture.registerStorePost(storePost);

            if(s3Service.isRunning()) keyValue = s3Service.upload(image, "storepost");
            if(keyValue == null) log.debug("No setup to s3 connect");

            picture.registerKey(keyValue);
            s3files.add(keyValue);
        }

        storePost.update(
                true,
                null,
                s3files.get(0).getUrl(),
                null,
                false,
                s3files.get(1).getUrl());
        return storePostsRepository.save(storePost).getId();
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
