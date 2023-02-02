package com.clone.ohouse.utility.s3;

import lombok.Getter;

@Getter
public class S3File {
    private String key;
    private String url;

    public S3File(String key, String url) {
        this.key = key;
        this.url = url;
    }
}
