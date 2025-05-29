package demo.back.service;


import demo.back.mapper.FileListMapper;
import demo.back.pojo.MyFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FileListServiceImpl {
    @Autowired
    private FileListMapper fileListMapper;

    public void insertFile(MyFile file) { // 更名并返回void
        fileListMapper.insert(file);
    }

    public ResponseEntity<MyFile[]> searchFiles(MyFile[] myFiles) {
        MyFile[] files = fileListMapper.selectAlll();//数据库的文件数据

        System.out.println("数据库文件数据：" + files.length);

        //如果实际文件夹为空，则删除所有的数据
        if (myFiles.length == 0 || myFiles == null) {
            for (int i = 0; i < files.length; i++) {
                fileListMapper.deleteById(files[i].getFileName());
            }
        }

        for (int i = 0; i < files.length; i++) {
            int k = -1;
            for (int j = 0; j < myFiles.length; j++) {
                if (Objects.equals(files[i].getFileName(), myFiles[j].getFileName())) {
                    break;
                }
                k++;
            }
            if (k == myFiles.length) {
                fileListMapper.deleteById(files[i].getFileName());
                System.out.println("删除文件：" + files[i].getFileName());
            }
        }


        MyFile[] files3 = fileListMapper.selectAlll();//数据库的文件数据
        return ResponseEntity.ok(files3);
    }
}
