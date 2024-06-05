package aliyun.oss.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Getter
@Setter
public class OssProperties {
    private String endpoint;

    private String region;

    private String bucketName;
}
