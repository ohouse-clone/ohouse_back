package com.clone.ohouse.utility.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private AmazonS3 s3Client;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final LocalFileService localFileService;

    public boolean isRunning(){
        return s3Client != null;
    }

    public S3File upload(File uploadFile, String dirName) throws IOException{
        String fileNameWithPath = dirName + "/" + uploadFile.getName(); //fireNameWithPath is the key
        log.debug("uploadFile Name = " + uploadFile.getName());
        log.debug("fileNameWithPath = " + fileNameWithPath);
        if (s3Client == null) {
            log.debug("Not Set for aws");
            return new S3File(null, fileNameWithPath);
        }

        String uploadImageUrl = putS3(uploadFile, fileNameWithPath);
        localFileService.deleteFile(uploadFile);
        return new S3File(fileNameWithPath, uploadImageUrl);
    }
    public void delete(String key){
        if(s3Client == null) throw new RuntimeException("running state is the local");

        s3Client.deleteObject(bucket, key);
    }


    @PostConstruct
    private void initConfig() {
        if(StringUtils.hasText(secretKey) && StringUtils.hasText(accessKey)) s3Client = setAmazonS3Client();
        else log.info("running without aws key");
    }

    private AmazonS3Client setAmazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();
    }



    private String putS3(File uploadFile, String fileNameWithPath) {
        //fileNameWithPath is the key
        s3Client.putObject(new PutObjectRequest(bucket, fileNameWithPath, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3Client.getUrl(bucket, fileNameWithPath).toString();
    }









}
