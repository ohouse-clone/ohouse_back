package com.clone.ohouse.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "Response의 Error Object <br>" +
                "Error code와 메시지로 이루어져 있습니다."
)
@Getter
@NoArgsConstructor
public class ErrorResponse {
    @ApiModelProperty(
            value = "Error Code, 코드 내용은 Error Class 를 참조"
    )
    private String code;

    @ApiModelProperty(
            value = "Error 의 내용"
    )
    private String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }


}
