package demo.back.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.pojo.Grade;
import demo.back.pojo.PageReg;
import demo.back.pojo.Student;
import demo.back.service.SearchGradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GradeController {
    @Autowired
    SearchGradeServiceImpl searchGradeService;

    @GetMapping("/GradegetTotals")
    public List<?> GradegetTotals(String sno, String sname, String sdept, String cname) {
        Grade grade = new Grade(sno, sname, sdept, cname, 0);
        return searchGradeService.GradegetTotals(grade);
    }

    @GetMapping("/searchGrade")
    public Page<Grade> searchGrade(String sno, String sname, String sdept, String cname, Integer current, Integer size) {
        Grade grade = new Grade(sno, sname, sdept, cname, 0);
        PageReg pageReg = new PageReg(current, size);
        Page<Student> page = new Page<>(pageReg.getCurrent(), pageReg.getSize());
        return searchGradeService.seachGrade(grade, page);
    }

    @PutMapping("/reviseGrade")
    public void reviseGrade(@RequestBody Grade grade) {
        System.out.println("grade:" + grade.getCname() + " " + grade.getSno() + " " + grade.getGrade());
        searchGradeService.updateGrade(grade);
    }
}
