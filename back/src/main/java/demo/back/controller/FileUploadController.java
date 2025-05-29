package demo.back.controller;

import demo.back.pojo.MyFile;
import demo.back.service.FileListServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@RestController
public class FileUploadController {

    // 删除类级别的 MyFile 实例变量

    @Value("${spring.servlet.multipart.location}")
    private String UPLOAD_DIR;

    @Autowired
    private FileListServiceImpl fileListServiceImpl;

    @PostConstruct
    public void init() {
        try {
            System.out.println("初始化上传目录: " + UPLOAD_DIR);
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("目录创建成功");
            }
        } catch (Exception e) {
            System.err.println("无法创建上传目录: " + e.getMessage());
            throw new RuntimeException("无法创建上传目录: " + UPLOAD_DIR, e);
        }
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam String author) {
        System.out.println("收到上传请求 - author: " + author);

        if (file.isEmpty()) {
            System.out.println("文件为空，无法上传");
            return "文件为空";
        }

        try {
            // 获取原始文件名并生成唯一文件名
            String filename = file.getOriginalFilename();
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                originalFilename = System.currentTimeMillis() + "_" + originalFilename;
            }

            Path filePath = Paths.get(UPLOAD_DIR, originalFilename);

            System.out.println("准备保存文件到: " + filePath);
            file.transferTo(filePath.toFile());
            System.out.println("文件保存成功");

            // 自动检测文件类型
            String detectedFileType;
            if (fileExtension.equalsIgnoreCase(".pdf")) {
                detectedFileType = "pdf";
            } else if (fileExtension.equalsIgnoreCase(".doc")) {
                detectedFileType = "doc";
            } else if (fileExtension.equalsIgnoreCase(".docx")) {
                detectedFileType = "docx";
            } else if (fileExtension.equalsIgnoreCase(".xls")) {
                detectedFileType = "xls";
            } else if (fileExtension.equalsIgnoreCase(".xlsx")) {
                detectedFileType = "xlsx";
            } else if (fileExtension.equalsIgnoreCase(".ppt")) {
                detectedFileType = "ppt";
            } else if (fileExtension.equalsIgnoreCase(".pptx")) {
                detectedFileType = "pptx";
            } else if (fileExtension.equalsIgnoreCase(".txt")) {
                detectedFileType = "txt";
            } else if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg")) {
                detectedFileType = "jpg"; // 统一处理为 jpg
            } else if (fileExtension.equalsIgnoreCase(".png")) {
                detectedFileType = "png";
            } else if (fileExtension.equalsIgnoreCase(".gif")) {
                detectedFileType = "gif";
            } else {
                // 其他格式直接返回扩展名（不带点）
                detectedFileType = fileExtension.replaceFirst("^\\.", "").toLowerCase();
            }
            // 在方法内部创建新实例
            MyFile myFile = new MyFile();

            myFile.setFileName(filename);//文件名

            myFile.setFileType(detectedFileType);//文件类型

            myFile.setAuthor(author);

            myFile.setFileLength(String.valueOf(file.getSize()));

            myFile.setFilePath(filePath.toString());

            myFile.setLastModified(new Date().toString());//操作时间

            myFile.setFileOnelyname(originalFilename);//唯一文件名
            fileListServiceImpl.insertFile(myFile);
            System.out.println("文件信息已保存到数据库");

            return "上传成功";
        } catch (Exception e) {
            System.err.println("上传过程中出错: " + e.getMessage());
            e.printStackTrace();
            return "上传失败: " + e.getMessage();
        }
    }
}