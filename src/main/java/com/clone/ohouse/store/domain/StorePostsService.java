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
import com.clone.ohouse.utility.s3.LocalFileService;
import com.clone.ohouse.utility.s3.S3File;
import com.clone.ohouse.utility.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
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
    private final LocalFileService localFileService;
    @Transactional
    public Long save(StorePostsSaveRequestDto saveRequestDto) throws IOException {
        StorePosts storePost = new StorePosts(saveRequestDto.getTitle(), null, saveRequestDto.getAuthor(), null, null);
        String contentUrl = null;
        String previewUrl = null;

        if(saveRequestDto.getContentImageId() != null && saveRequestDto.getPreviewImageId() != null) {
            List<S3File> s3files = saveInS3(saveRequestDto.getContentImageId(), saveRequestDto.getPreviewImageId(), storePost);
            contentUrl = s3files.get(0).getUrl();
            previewUrl = s3files.get(1).getUrl();
        }


        storePost.update(
                true,
                null,
                contentUrl,
                null,
                false,
                previewUrl);
        return storePostsRepository.save(storePost).getId();
    }

    private List<S3File> saveInS3(Long previewImageId, Long contentImageId, StorePosts storePost) throws IOException {
        List<S3File> s3files = new ArrayList<>();
        List<StorePostPictures> pictures = storePostPicturesRepository.findAllById(new ArrayList<>(List.of(previewImageId, contentImageId)));
        for (StorePostPictures picture : pictures) {
            File image = new File(picture.getLocalFilePath());
            log.debug("storepost save with image : " + picture.getLocalFilePath());

            picture.registerStorePost(storePost);

            if(s3Service.isRunning()){
                S3File keyValue = s3Service.upload(image, "storepost");

                if(keyValue == null) {
                    log.debug("No setup to s3 connect");
                    throw new RuntimeException("No setting in aws");
                }

                picture.registerKey(keyValue);
                s3files.add(keyValue);
            }
            localFileService.deleteFile(image);
        }
        return s3files;
    }

    @Transactional
    public Long delete(Long id){
        StorePosts post = storePostsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        deleteExistingStorePostPictures(post);
        //TODO: 연관된 PRODUCT 해제할 것

        storePostsRepository.delete(post);

        return id;
    }

    @Transactional
    public Long update(Long id, StorePostsUpdateRequestDto dto) throws IOException{
        String contentUrl = null;
        String previewUrl = null;
        StorePosts entity = storePostsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        deleteExistingStorePostPictures(entity);



        if(dto.getContentImageId() != null && dto.getPreviewImageId() != null) {
            List<S3File> s3files = saveInS3(dto.getContentImageId(), dto.getContentImageId(), entity);
            contentUrl = s3files.get(0).getUrl();
            previewUrl = s3files.get(1).getUrl();
        }

        entity.update(
                true,
                dto.getTitle(),
                contentUrl,
                dto.getModifiedUser(),
                false,
                previewUrl);

        return storePostsRepository.save(entity).getId();
    }

    private void deleteExistingStorePostPictures(StorePosts entity) {
        List<StorePostPictures> previousPictures = storePostPicturesRepository.findByStorePostsId(entity.getId());
        if(previousPictures.size() != 0){
            for (StorePostPictures previousPicture : previousPictures) {
                if(s3Service.isRunning())
                    s3Service.delete(previousPicture.getKeyName());
                storePostPicturesRepository.delete(previousPicture);
            }
        }
    }

    @Transactional
    public StorePostsResponseDto findById(Long id){
        StorePosts entity = storePostsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없음"));
        return new StorePostsResponseDto(entity);
    }

    // TODO: controller 필요
    @Transactional
    public StorePostWithProductsDto findByIdWithProduct(Long id){
        StorePosts post = storePostsRepository.findByIdWithFetchJoinProduct(id);
        if(post == null) throw new NoSuchElementException("잘못된 Storepost id : " + id);

        return new StorePostWithProductsDto(post,
                post.getProductList()
                        .stream()
                        .map((p) -> new ProductDetailDto(p))
                        .collect(Collectors.toCollection(ArrayList<ProductDetailDto>::new)));
    }
}
