package aliyun.oss.config;

import com.aliyun.oss.OSS;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class OssClientShutdown implements DisposableBean {
    private final OSS ossClient;

    @Override
    public void destroy() throws Exception {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}