package com.clone.ohouse.utility.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3UploaderController {
    private final S3Service s3Service;

    @PostMapping("/api/v1/files")
    public String imageUpload(
            @RequestParam("data")MultipartFile multipartFile) throws Exception{
        return s3Service.upload(multipartFile, "test");
    }

}
