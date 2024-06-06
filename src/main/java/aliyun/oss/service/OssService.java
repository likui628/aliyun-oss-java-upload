package aliyun.oss.service;

import com.aliyun.oss.HttpMethod;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    String getPresignedUrl(String key, HttpMethod method);

    String getPolicy();

    boolean uploadFile(MultipartFile file, String path);
}
