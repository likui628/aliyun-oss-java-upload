package aliyun.oss.service.impl;

import aliyun.oss.config.OssProperties;
import aliyun.oss.service.OssService;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
@AllArgsConstructor
public class OssServiceImp implements OssService {
    private final OSS ossClient;

    private final OssProperties ossProperties;

    private static final long EXPIRE_TIME = 3600 * 1000L;

    @Override
    public String getPresignedUrl(String key) {
        try {
            Date expiration =new Date(System.currentTimeMillis() + EXPIRE_TIME);

            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                    ossProperties.getBucketName(),
                    key, HttpMethod.PUT);
            request.setExpiration(expiration);

            URL signedUrl = ossClient.generatePresignedUrl(request);
            return signedUrl.toString();

        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
        return null;
    }
}
