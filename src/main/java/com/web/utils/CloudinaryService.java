package com.web.utils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinaryConfig;

    public String uploadFile(MultipartFile file) {
        try {
            File uploadedFile = convertMultiPartToFile(file);

            // Lấy tên gốc (không phần mở rộng) từ file upload
            String originalFileName = file.getOriginalFilename();
            String fileNameWithoutExtension = originalFileName != null ? originalFileName.replaceFirst("[.][^.]+$", "") : "default_file_name";

            // Upload với tên chỉ định
            Map uploadResult = cloudinaryConfig.uploader().upload(
                    uploadedFile,
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "public_id", fileNameWithoutExtension, // có thể bỏ "your_folder/" nếu không cần thư mục
                            "use_filename", true,
                            "unique_filename", false // để không thêm chuỗi ngẫu nhiên vào tên
                    )
            );
            uploadedFile.delete();
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}

