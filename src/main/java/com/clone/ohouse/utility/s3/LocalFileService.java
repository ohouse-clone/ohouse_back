package com.clone.ohouse.utility.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileService {
    @Value("${spring.custom.environment.ec2}")
    private String environmentEc2;
    @Value("${spring.file-dir}")
    private String rootDir;
    private String fileDir;

    public File createFileBeforeUploadS3(MultipartFile file, String dirName) throws IOException {
        File uploadFile = convert(file, dirName).orElseThrow(() -> new IllegalArgumentException("Fail to convert MultipartFile"));
        return uploadFile;
    }
    public void deleteFile(File file){
        this.removeNewFile(file);
    }

    @PostConstruct
    private void initConfig(){
        if (environmentEc2.equals("false")) {
            this.fileDir = System.getProperty("user.dir") + this.rootDir;
            log.info("running environment is local");
        } else if (environmentEc2.equals("true")) {
            this.fileDir = this.rootDir;

            log.info("running environment is ec2");
        }
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
