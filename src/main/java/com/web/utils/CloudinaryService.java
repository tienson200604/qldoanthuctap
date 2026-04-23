package com.web.utils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final long DOWNLOAD_URL_EXPIRE_SECONDS = 15 * 60;

    @Autowired
    private Cloudinary cloudinaryConfig;

    public String uploadFile(MultipartFile file) {
        File uploadedFile = null;
        try {
            uploadedFile = convertMultiPartToFile(file);
            String originalFileName = getOriginalFileName(file);
            Map<String, Object> uploadOptions = buildUploadOptions(file, originalFileName);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, uploadOptions);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (uploadedFile != null && uploadedFile.exists()) {
                uploadedFile.delete();
            }
        }
    }


    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        String extension = getFileExtension(getOriginalFileName(file));
        File convFile = File.createTempFile("upload-", extension.isEmpty() ? ".tmp" : "." + extension);
        file.transferTo(convFile);
        return convFile;
    }

    private Map<String, Object> buildUploadOptions(MultipartFile file, String originalFileName) {
        boolean rawResource = isRawResource(file);
        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("resource_type", rawResource ? "raw" : "auto");
        uploadOptions.put("use_filename", true);
        uploadOptions.put("unique_filename", false);
        uploadOptions.put("overwrite", true);
        uploadOptions.put("public_id", rawResource ? originalFileName : stripExtension(originalFileName));
        return uploadOptions;
    }

    private boolean isRawResource(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || contentType.trim().isEmpty()) {
            return true;
        }
        String normalizedContentType = contentType.toLowerCase(Locale.ROOT);
        return !normalizedContentType.startsWith("image/") && !normalizedContentType.startsWith("video/");
    }

    private String getOriginalFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            return "uploaded-file";
        }
        return new File(originalFileName).getName();
    }

    private static String stripExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex <= 0) {
            return fileName;
        }
        return fileName.substring(0, extensionIndex);
    }

    private static String getFileExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex < 0 || extensionIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(extensionIndex + 1);
    }

    public String buildSignedDownloadUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            return fileUrl;
        }
        try {
            AssetInfo assetInfo = parseAssetInfo(fileUrl);
            if (assetInfo == null) {
                return fileUrl;
            }
            Map<String, Object> options = new HashMap<>();
            options.put("resource_type", assetInfo.resourceType);
            options.put("type", "upload");
            options.put("attachment", true);
            options.put("expires_at", (System.currentTimeMillis() / 1000L) + DOWNLOAD_URL_EXPIRE_SECONDS);
            String downloadPublicId = "raw".equals(assetInfo.resourceType)
                    ? assetInfo.publicPath
                    : assetInfo.publicIdWithoutExtension;
            String format = "raw".equals(assetInfo.resourceType) ? null : assetInfo.extension;

            return cloudinaryConfig.privateDownload(downloadPublicId, format, options);
        } catch (Exception e) {
            return fileUrl;
        }
    }

    public void deleteFileByUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            return;
        }
        try {
            AssetInfo assetInfo = parseAssetInfo(fileUrl);
            if (assetInfo == null) {
                return;
            }
            Map<String, Object> options = new HashMap<>();
            options.put("resource_type", assetInfo.resourceType);
            options.put("type", "upload");
            options.put("invalidate", true);
            String destroyPublicId = "raw".equals(assetInfo.resourceType)
                    ? assetInfo.publicPath
                    : assetInfo.publicIdWithoutExtension;
            Map result = cloudinaryConfig.uploader().destroy(destroyPublicId, options);
            Object destroyResult = result.get("result");
            if (!"ok".equals(destroyResult) && !"not found".equals(destroyResult)) {
                throw new RuntimeException("Cloudinary destroy result: " + destroyResult);
            }
        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa file trên Cloudinary", e);
        }
    }

    private AssetInfo parseAssetInfo(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        if (!"res.cloudinary.com".equalsIgnoreCase(url.getHost())) {
            return null;
        }
        String path = url.getPath();
        int uploadIndex = path.indexOf("/upload/");
        if (uploadIndex < 0) {
            return null;
        }
        String resourceType = extractResourceType(path);
        String encodedPublicPath = extractPublicPath(path, uploadIndex + "/upload/".length());
        if (encodedPublicPath == null || encodedPublicPath.isEmpty()) {
            return null;
        }
        String decodedPublicPath = URLDecoder.decode(encodedPublicPath, StandardCharsets.UTF_8.name());
        return new AssetInfo(resourceType, decodedPublicPath);
    }

    private String extractResourceType(String path) {
        String[] segments = path.split("/");
        for (int i = 0; i < segments.length - 2; i++) {
            if ("upload".equals(segments[i + 2])) {
                return segments[i + 1];
            }
        }
        return "raw";
    }

    private String extractPublicPath(String path, int startIndex) {
        String uploadPath = path.substring(startIndex);
        if (uploadPath.startsWith("v")) {
            int versionSeparatorIndex = uploadPath.indexOf('/');
            if (versionSeparatorIndex > 0) {
                String versionSegment = uploadPath.substring(0, versionSeparatorIndex);
                if (versionSegment.matches("v\\d+")) {
                    return uploadPath.substring(versionSeparatorIndex + 1);
                }
            }
        }
        return uploadPath;
    }

    private static class AssetInfo {
        private final String resourceType;
        private final String publicPath;
        private final String publicIdWithoutExtension;
        private final String extension;

        private AssetInfo(String resourceType, String publicPath) {
            this.resourceType = resourceType;
            this.publicPath = publicPath;
            this.publicIdWithoutExtension = stripExtension(publicPath);
            this.extension = getFileExtension(publicPath);
        }
    }
}

