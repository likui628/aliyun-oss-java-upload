package aliyun.oss.service.impl;

import aliyun.oss.config.OssProperties;
import aliyun.oss.service.OssService;
import aliyun.oss.utils.ExceptionHandler;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PolicyConditions;
import lombok.AllArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@AllArgsConstructor
public class OssServiceImp implements OssService {
    private final OSS ossClient;

    private final OssProperties ossProperties;

    private static final long EXPIRE_TIME = 3600 * 1000L;

    private static final long FILE_LIMIT = 10 * 1024 * 1024;

    @Override
    public String getPresignedUrl(String key, HttpMethod method) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + EXPIRE_TIME);

            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                    ossProperties.getBucketName(),
                    key, method);
            request.setExpiration(expiration);

            URL signedUrl = ossClient.generatePresignedUrl(request);
            return signedUrl.toString();

        } catch (ClientException ce) {
            ExceptionHandler.handleClientException(ce);
        }
        return null;
    }

    @Override
    public String getPolicy() {
        JSONObject response = new JSONObject();
        try {
            Date expiration = new Date(System.currentTimeMillis() + EXPIRE_TIME);

            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, FILE_LIMIT);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);

            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            response.put("ossAccessKeyId", ossProperties.getOssAccessKeyId());
            response.put("policy", encodedPolicy);
            response.put("signature", postSignature);
            response.put("host",
                    "http://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint());
        } catch (OSSException oe) {
            ExceptionHandler.handleOSSException(oe);
        } catch (ClientException ce) {
            ExceptionHandler.handleClientException(ce);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return response.toString();
    }

    @Override
    public boolean uploadFile(MultipartFile file, String path) {
        boolean result = false;
        try {
            String objectName = path + "/" + file.getOriginalFilename();
            ossClient.putObject(ossProperties.getBucketName(), objectName, new ByteArrayInputStream(file.getBytes()));
            result = true;
        } catch (OSSException oe) {
            ExceptionHandler.handleOSSException(oe);
        } catch (ClientException ce) {
            ExceptionHandler.handleClientException(ce);
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
        return result;
    }
}
