package demo.back.controller;


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

    @GetMapping("/gettotals")
    public List<Student> gettotals(String sno,String sname,String ssex,String sage,String sdept){
        Student student = new Student(sno,sname,ssex,sage,sdept);
        System.out.println("!!!!"+student.getSdept());
        return searchstudentServiceImpl.getTotals(student);
    }

    @GetMapping("/searchstudent")
    public Page<Student> servicesearch(String sno,String sname,String ssex,String sage,String sdept,Integer current,Integer size){
//        这两句话用于测试是否正常获取
        Student student = new Student(sno,sname,ssex,sage,sdept);
        PageReg pageReg = new PageReg(current,size);
        System.out.println("==="+student.getSdept());
        System.out.println("==="+pageReg.getCurrent());
        System.out.println("==="+pageReg.getSize());

        Page<Student> page = new Page<>(pageReg.getCurrent(),pageReg.getSize());
        return searchstudentServiceImpl.seachstudent(student,page);
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
    public ResponseEntity<String> reviseStudent(@RequestBody Student student,@RequestParam("oldsno") String oldsno) {
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
