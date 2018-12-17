package me.quiz_together.root.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.exceptions.FileNameEmptyException;
import me.quiz_together.root.support.security.SecurityUtils;

@Slf4j
@Service
public class AmazonS3Service {

    private String bucket = "quiz_together.image";
    int size = 256;

    @Autowired
    private AmazonS3 amazonS3;

    public String uploadImage(MultipartFile multipartFile){
        if (StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            throw new FileNameEmptyException();
        }

        String uploadKey = createHashFileName(multipartFile.getOriginalFilename());

        // thumbnail 생성
        try (InputStream inputStream = createMultipartFileToByteArrayInputStream(multipartFile)){
            PutObjectRequest putObjectRequest = createPutObjectRequest(inputStream, uploadKey);
            PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return createProfileImageUrl(uploadKey);
    }

    public PutObjectRequest createPutObjectRequest(InputStream inputStream, String uploadKey) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        return new PutObjectRequest(bucket, uploadKey, inputStream, objectMetadata);
    }

    public String createHashFileName(String fileName) {
        return String.format("%s", SecurityUtils.encryptMD5AndSHA256(System.currentTimeMillis() + fileName));
    }

    public InputStream createMultipartFileToByteArrayInputStream(MultipartFile multipartFile) throws IOException {
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        BufferedImage thumbnailImage = createThumbnail(image);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(thumbnailImage, "png", os);

        return new ByteArrayInputStream(os.toByteArray());
    }

    public BufferedImage createThumbnail(BufferedImage originalImage) throws IOException {
        //썸네일 생성
        double getWidth = originalImage.getWidth();
        double getHeight = originalImage.getHeight();

        // 비율
        double resizeRatio = getWidth / getHeight;

        // 지정한 높이, 높이와 비율로 구한 너비
        int mediumHeight = size;
        int mediumWidth = (int) (resizeRatio * mediumHeight);

        return Thumbnails.of(originalImage).size(mediumWidth, mediumHeight).asBufferedImage();
    }

    public String createProfileImageUrl(String imageKey) {
        URL thumbnailURL = null;
        Date expiration = new Date();
        expiration.setDate(expiration.getDate() + 7);

        GeneratePresignedUrlRequest thumbnailRequest = new GeneratePresignedUrlRequest(bucket, imageKey);
        thumbnailRequest.setExpiration(expiration);
        thumbnailURL = amazonS3.generatePresignedUrl(thumbnailRequest);

        return thumbnailURL.toString();
    }
}
