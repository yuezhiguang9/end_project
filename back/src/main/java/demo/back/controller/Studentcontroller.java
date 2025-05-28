package demo.back.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.pojo.PageReg;
import demo.back.pojo.Student;
import demo.back.service.SearchstudentServiceImpl;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class Studentcontroller {

    @Autowired
    private SearchstudentServiceImpl searchstudentServiceImpl;

    @GetMapping("/gettotals")
    public List<Student> gettotals(String sno, String sname, String ssex, String sage, String sdept) {
        Student student = new Student(sno, sname, ssex, sage, sdept);
        System.out.println("!!!!" + student.getSdept());
        return searchstudentServiceImpl.getTotals(student);
    }

    @GetMapping("/searchstudent")
    public Page<Student> servicesearch(
            String sno, String sname, String ssex, String sage, String sdept,
            Integer current, Integer size,
            @RequestParam(required = false) String sortField, // 新增排序字段参数
            @RequestParam(required = false) String sortOrder // 新增排序顺序参数
    ) {
        Student student = new Student(sno, sname, ssex, sage, sdept);
        PageReg pageReg = new PageReg(current, size);

        Page<Student> page = new Page<>(pageReg.getCurrent(), pageReg.getSize());
        return searchstudentServiceImpl.seachstudent(student, page, sortField, sortOrder); // 传递排序参数
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
    public ResponseEntity<String> reviseStudent(@RequestBody Student student, @RequestParam("oldsno") String oldsno) {
        if (student.getSno() == null) {
            return ResponseEntity.badRequest().body("sno不能为空");
        }
        try {
            System.out.println("controller中：老sno：" + oldsno);
            searchstudentServiceImpl.reviseStudent(student, oldsno);
            return ResponseEntity.ok("修改成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("修改失败: " + e.getMessage());
        }
    }

    @PutMapping("/addStudent")
    public String addStudent(@RequestBody Student student) {
        return searchstudentServiceImpl.addStudent(student);
    }

}
