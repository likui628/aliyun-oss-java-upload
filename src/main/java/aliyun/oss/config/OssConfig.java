package aliyun.oss.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyuncs.exceptions.ClientException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class OssConfig {
    private final OssProperties ossProperties;

    @Bean
    public OSS ossClient() {
        EnvironmentVariableCredentialsProvider credentialsProvider = null;
        try {
            credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create EnvironmentVariableCredentialsProvider", e);
        }

        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setSignatureVersion(SignVersion.V4);

        return new OSSClientBuilder().create()
                .endpoint(ossProperties.getEndpoint())
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(conf)
                .region(ossProperties.getRegion())
                .build();
    }

}
