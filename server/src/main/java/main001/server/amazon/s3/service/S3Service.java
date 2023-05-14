package main001.server.amazon.s3.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
@Transactional
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3Service(@Value("${cloud.aws.credentials.access-key}") String accessKey,
                     @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                     @Value("${cloud.aws.region.static}")  String region) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }


    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "." + extension;
        String folderKey;
        if (fileName != null && !fileName.isEmpty()) {
            folderKey = folderName + "/";
        } else folderKey = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        InputStream inputStream = file.getInputStream();

        s3Client.putObject(new PutObjectRequest(bucketName, folderKey + fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

//        try (InputStream inputStream = file.getInputStream()) {
//            s3Client.putObject(new PutObjectRequest(bucketName, folderKey + fileName, inputStream, objectMetadata)
//                    .withCannedAcl(CannedAccessControlList.PublicRead));
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to upload file to S3", e);
//        }

        return s3Client.getUrl(bucketName, folderKey + fileName).toString();
    }


    public String getFileUrl(String fileName) {
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    public String uploadLocal(String filePath) throws RuntimeException {
        File targetFile = new File(filePath);

        String uploadImageUrl = putS3(targetFile, targetFile.getName());
        removeOriginalFile(targetFile);

        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) throws RuntimeException {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3Client.getUrl(bucketName, fileName).toString();

    }

    private void removeOriginalFile(File targetFile) {
        if (targetFile.exists() && targetFile.delete()) {
            return;
        }
    }

    public void removeS3File(String fileName) {
        final DeleteObjectRequest deleteObjectRequest =
                new DeleteObjectRequest(bucketName, fileName);
        s3Client.deleteObject(deleteObjectRequest);
    }



    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }
    }


    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException();
        }
    }

}
