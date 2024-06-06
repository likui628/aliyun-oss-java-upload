package aliyun.oss.controller;

import aliyun.oss.service.OssService;
import com.aliyun.oss.HttpMethod;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/aliyun/oss")
@AllArgsConstructor
public class OssController {
    private final OssService ossService;

    @GetMapping("/presignedUrl")
    public ResponseEntity<?> presignedUrl(@RequestParam(value = "key") String key) {
        String presignedUrl = ossService.getPresignedUrl(key, HttpMethod.PUT);
        return ResponseEntity.ok(presignedUrl);
    }

    @GetMapping("/policy")
    public ResponseEntity<?> policy() {
        String policy = ossService.getPolicy();
        return ResponseEntity.ok(policy);
    }

    @GetMapping("/url")
    public ResponseEntity<?> getFileUrl(@RequestParam(value = "key") String key) {
        String presignedUrl = ossService.getPresignedUrl(key, HttpMethod.GET);
        return ResponseEntity.ok(presignedUrl);
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "path", defaultValue = "") String path) {
        if (file.isEmpty()) {
            return ResponseEntity.status(404).body("file is empty");
        }

        boolean isSuccessful = ossService.uploadFile(file, path);
        if (isSuccessful) {
            return ResponseEntity.ok("Upload successful");
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed");
        }
    }
}
