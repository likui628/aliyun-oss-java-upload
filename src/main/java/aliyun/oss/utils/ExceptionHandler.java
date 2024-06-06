package aliyun.oss.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;

public class ExceptionHandler {
    public static void handleOSSException(OSSException oe) {
        System.out.println("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.");
        System.out.println("Error Message:" + oe.getErrorMessage());
        System.out.println("Error Code:" + oe.getErrorCode());
        System.out.println("Request ID:" + oe.getRequestId());
        System.out.println("Host ID:" + oe.getHostId());
    }

    public static void handleClientException(ClientException ce) {
        System.out.println("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.");
        System.out.println("Error Message: " + ce.getMessage());
    }

    public static void handleException(Exception ex) {
        System.out.println("Error Message: " + ex.getMessage());
    }
}
