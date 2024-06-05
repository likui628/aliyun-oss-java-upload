package aliyun.oss.service;

public interface OssService {

    String getPresignedUrl(String key);

    String getPolicy();
}
