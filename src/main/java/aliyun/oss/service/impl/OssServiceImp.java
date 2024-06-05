package aliyun.oss.service.impl;

import aliyun.oss.config.OssProperties;
import aliyun.oss.service.OssService;
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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;

@Service
@AllArgsConstructor
public class OssServiceImp implements OssService {
    private final OSS ossClient;

    private final OssProperties ossProperties;

    private static final long EXPIRE_TIME = 3600 * 1000L;

    private static final long FILE_LIMIT = 10 * 1024 * 1024;

    @Override
    public String getPresignedUrl(String key) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + EXPIRE_TIME);

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

    @Override
    public String getPolicy() {
        JSONObject response = new JSONObject();
        try {
            Date expiration = new Date(System.currentTimeMillis() + EXPIRE_TIME);

            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, FILE_LIMIT);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);

            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            response.put("ossAccessKeyId", ossProperties.getOssAccessKeyId());
            response.put("policy", encodedPolicy);
            response.put("signature", postSignature);
            response.put("host",
                    "http://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            // 假设此方法存在
            System.out.println("HTTP Status Code: " + oe.getRawResponseError());
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } finally {
            return response.toString();
        }
    }
}
