package aliyun.oss.service;

import com.aliyun.oss.HttpMethod;

public interface OssService {

    String getPresignedUrl(String key, HttpMethod method);

    String getPolicy();
}
