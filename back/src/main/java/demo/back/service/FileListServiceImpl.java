package demo.back.service;


import demo.back.mapper.FileListMapper;
import demo.back.pojo.MyFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FileListServiceImpl {
    @Autowired
    private FileListMapper fileListMapper;

    public void insertFile(MyFile file) { // 更名并返回void
        fileListMapper.insert(file);
    }

    public ResponseEntity<MyFile[]> searchFiles() {
        MyFile[] files = fileListMapper.selectAlll();
        return ResponseEntity.ok(files);
    }
}
