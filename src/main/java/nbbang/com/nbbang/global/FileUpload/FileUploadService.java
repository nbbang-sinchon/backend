package nbbang.com.nbbang.global.FileUpload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Uploader s3Uploader;

    public String deleteAndUpload(String deleteUrl, MultipartFile imgFile, String uploadDirName) throws IOException {
        if (deleteUrl != null) {
            String oldFileName = deleteUrl.substring(deleteUrl.lastIndexOf("/") + 1);
            s3Uploader.delete(uploadDirName, oldFileName);
        }
        return upload(imgFile, uploadDirName);
    }

    public String upload(MultipartFile imgFile, String uploadDirName) throws IOException {
        String savedFileName = UUID.randomUUID().toString();
        String uploadUrl = s3Uploader.upload(imgFile, uploadDirName, savedFileName);
        return uploadUrl;
    }
}
