package com.clone.ohouse.store.domain.item;


import io.swagger.annotations.ApiModel;

@ApiModel(
        description = "Item API(GET /store/api/v1/item/items) Response의 하위 필드이자 공통  Request/Response<br>" +
                "이 항목은 BedRequestDto 또는 StorageBedRequestDto와 같이 Item 하부 항목에 맞는 Dto로 동적으로 변경됩니다."
)
public abstract class ItemRequestDto {
    public abstract Item toEntity();
}
