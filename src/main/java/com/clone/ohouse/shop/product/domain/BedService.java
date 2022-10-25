package com.clone.ohouse.shop.product.domain;

import com.clone.ohouse.shop.product.domain.access.BedRepository;
import com.clone.ohouse.shop.product.domain.dto.BedAllListResponseDto;
import com.clone.ohouse.shop.product.domain.dto.BedSaveRequestDto;
import com.clone.ohouse.shop.product.domain.dto.BedUpdateRequestDto;
import com.clone.ohouse.shop.product.domain.entity.Bed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class BedService {
    private final BedRepository bedRepository;

    @Transactional
    public Long save(BedSaveRequestDto requestDto){
        return bedRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long seq, BedUpdateRequestDto requestDto) {
        Bed posts = bedRepository.findById(seq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Item : " + seq));

        posts.update(requestDto.getName(), requestDto.getModelName(), requestDto.getBrandName(), requestDto.getSize(), requestDto.getColor());

        return seq;
    }

    @Transactional(readOnly = true)
    public List<BedAllListResponseDto> findAllAsc(){
        return bedRepository.findAllAsc().stream()
                .map(BedAllListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long seq){
        Bed item = bedRepository.findById(seq)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 Item : " + seq));

        bedRepository.delete(item);
    }
}
