package service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImageStorageService {

	// Thư mục lưu ảnh: uploads/ nằm ngang hàng với src/
	private final Path storagePath = Paths.get("uploads");

	// Đường dẫn ảnh qua HTTP để Angular truy cập
	private final String imageBaseUrl = "http://localhost:8080/images/";

	/**
	 * Lưu file ảnh được upload từ người dùng
	 */
	public String store(MultipartFile file) {
		try {
			createDirectoryIfNotExists();

			String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
			Path targetPath = storagePath.resolve(filename);

			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

			return imageBaseUrl + filename; // ✅ Trả về link HTTP cho frontend
		} catch (IOException e) {
			throw new RuntimeException("Failed to store image", e);
		}
	}

	/**
	 * Tải ảnh từ URL và lưu về thư mục uploads/
	 */
	public String downloadImageFromUrl(String imageUrl) {
		try {
			URI uri = new URI(imageUrl);
			URL url = uri.toURL();

			try (InputStream in = url.openStream()) {
				createDirectoryIfNotExists();

				String filename = UUID.randomUUID() + "_" + getFileNameFromUrl(imageUrl);
				Path targetPath = storagePath.resolve(filename);

				Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);

				return imageBaseUrl + filename;
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to download and store image", e);
		}
	}

	/**
	 * Kiểm tra xem file upload có phải ảnh không
	 */
	public boolean isValidImageFile(MultipartFile file) {
		if (file == null) return false;

		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			return false;
		}

		String originalFilename = file.getOriginalFilename();
		return originalFilename != null &&
			originalFilename.matches("(?i).+\\.(jpg|jpeg|png|gif|bmp|webp)$");
	}

	/**
	 * Kiểm tra xem URL có trỏ đến ảnh không
	 */
	public boolean isValidImageUrl(String url) {
		try {
			URI uri = new URI(url);
			URL realUrl = uri.toURL();

			// Check extension
			String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
			boolean hasValidExtension = false;
			for (String ext : validExtensions) {
				if (url.toLowerCase().endsWith(ext)) {
					hasValidExtension = true;
					break;
				}
			}
			if (!hasValidExtension) return false;

			// Check content-type
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestMethod("HEAD");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);

			String contentType = conn.getContentType();
			return contentType != null && contentType.startsWith("image/");

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Tạo thư mục nếu chưa tồn tại
	 */
	private void createDirectoryIfNotExists() throws IOException {
		if (!Files.exists(storagePath)) {
			Files.createDirectories(storagePath);
		}
	}

	/**
	 * Lấy tên file từ URL
	 */
	private String getFileNameFromUrl(String url) {
		return url.substring(url.lastIndexOf('/') + 1);
	}
}
