package aliyun.oss.controller;

import aliyun.oss.service.OssService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aliyun/oss")
@AllArgsConstructor
public class OssController {
    private final OssService ossService;

    @GetMapping("/presignedUrl")
    public ResponseEntity<?> presignedUrl(@RequestParam(value = "key") String key) {
        String presignedUrl = ossService.getPresignedUrl(key);
        return ResponseEntity.ok(presignedUrl);
    }
}
