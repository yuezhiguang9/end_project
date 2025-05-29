package demo.back.controller;


import demo.back.pojo.MyFile;
import demo.back.service.FileListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class FileDownloadController {

    @Autowired
    FileListServiceImpl fileListService;

    @Value("${spring.servlet.multipart.location}") // 使用相同的配置
    private String UPLOAD_DIR;

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> download(@RequestParam("fileName") String fileName) {
        String filePath = UPLOAD_DIR + File.separator + fileName; // 使用注入的路径
        System.out.println(fileName);
        System.out.println(filePath);
        File file = new File(filePath);

        if (file.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            return ResponseEntity.ok().headers(headers).body(new FileSystemResource(file));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/fileList")
    public ResponseEntity<MyFile[]> fileList() {
//        File directory = new File(UPLOAD_DIR); // 使用注入的路径
//        File[] fList = directory.listFiles();//获取文件列表
//        MyFile[] myFiles = null;
//        if (fList != null) {
//            myFiles = new MyFile[fList.length];
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
//        if (fList != null) {
//            for (int i = 0; i < fList.length; i++) {
//                myFiles[i] = new MyFile();
//                myFiles[i].setFileName(fList[i].getName());
//                myFiles[i].setFileLength(fList[i].length() + "bytes");
//                long lastModified = fList[i].lastModified();
//                Date date = new Date(lastModified);
//                String lastModifiedTime = sdf.format(date);
//                myFiles[i].setLastModified(lastModifiedTime);
//                System.out.println(myFiles[i].toString());
//            }
//        }


        return fileListService.searchFiles();
    }


}

