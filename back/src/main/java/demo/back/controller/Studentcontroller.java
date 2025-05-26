package demo.back.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.pojo.PageReg;
import demo.back.pojo.Student;
import demo.back.service.SearchstudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class Studentcontroller {

    @Autowired
    private SearchstudentServiceImpl searchstudentServiceImpl;

    @GetMapping("/searchstudent")
    public List<Student> servicesearch(Student student){
//        这两句话用于测试是否正常获取
//        System.out.println("==="+pageReg.getCurrent());
//        System.out.println("==="+pageReg.getSize());
//        Page<Student> page = new Page<>(pageReg.getCurrent(),pageReg.getSize());
        return searchstudentServiceImpl.seachstudent(student);
    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<String> deleteStudents(@RequestBody List<String> snoList) {
        if (snoList.isEmpty()) {
            return ResponseEntity.badRequest().body("sno列表不能为空");
        }
        try {
            searchstudentServiceImpl.deleteBySnoList(snoList);
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("删除失败: " + e.getMessage());
        }
    }

    @PutMapping("/reviseStudent")
    public ResponseEntity<String> reviseStudent(@RequestBody Student student,Integer oldsno) {
        if(student.getSno()==null){
            return ResponseEntity.badRequest().body("sno不能为空");
        }
        try{
            System.out.println("controller中：老sno："+oldsno);
            searchstudentServiceImpl.reviseStudent(student,oldsno);
            return ResponseEntity.ok("修改成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("修改失败: " + e.getMessage());
        }
    }

}
