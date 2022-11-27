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

    @Value("${spring.custom.environment.ec2}")
    private String environmentEc2;
    @Value("${spring.file-dir}")
    private String rootDir;
    private String fileDir;

    private AmazonS3 s3Client;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String dirName) throws IOException {
        File uploadFile = convert(file, dirName).orElseThrow(() -> new IllegalArgumentException("Fail to convert MultipartFile"));

        return upload(uploadFile, dirName);
    }


    @PostConstruct
    private void initConfig() {

        if (environmentEc2.equals("false")) {
            this.fileDir = System.getProperty("user.dir") + this.rootDir;
            log.info("running environment is local");
        } else if (environmentEc2.equals("true")) {
            this.fileDir = this.rootDir;

            log.info("running environment is ec2");
        }

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

    private String upload(File uploadFile, String dirName) {
        String fileNameWithPath = dirName + "/" + UUID.randomUUID() + "_" + uploadFile.getName();
        log.debug("uploadFile Name = " + uploadFile.getName());
        log.debug("fileNameWithPath = " + fileNameWithPath);
        if (s3Client == null) {
            log.debug("Not Set for aws");
            return fileNameWithPath;
        }

        String uploadImageUrl = putS3(uploadFile, fileNameWithPath);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileNameWithPath) {

        s3Client.putObject(new PutObjectRequest(bucket, fileNameWithPath, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileNameWithPath).toString();
    }

    private Optional<File> convert(MultipartFile multipartFile, String dirName) throws IOException {
        if (multipartFile.isEmpty()) return Optional.empty();

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        String nameWithPath = fileDir + "/" + dirName + "/" + storeFileName;

        log.debug("file : " + nameWithPath);

        File file = new File(nameWithPath);
        multipartFile.transferTo(file);

        return Optional.of(file);
    }

    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    private void removeNewFile(File file) {
        if (file.delete()) {
            log.info("Success to delete file");
            return;
        }
        log.info("Fail to delete file");
    }

}
