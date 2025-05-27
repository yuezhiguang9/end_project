package demo.back.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.mapper.SearchcourseMapper;
import demo.back.pojo.Course;
import demo.back.pojo.PageReg;
import demo.back.pojo.Student;
import demo.back.service.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @GetMapping("/getTotals")
    public List<Course> gettotals(String cno, String cname, String cpon){
        Course course = new Course(cno,cname,cpon);
        System.out.println("!!!!"+course.getCname());
        return courseService.getTotals(course);
    }

    @GetMapping("/searchcourse")
    public Page<Course> seachcourse(String cno, String cname, String cpon, Integer current, Integer size){
//        这两句话用于测试是否正常获取
        Course course = new Course(cno,cname,cpon);
        PageReg pageReg = new PageReg(current,size);
        System.out.println("==="+course.getCname());
        System.out.println("==="+pageReg.getCurrent());
        System.out.println("==="+pageReg.getSize());

        Page<Course> page = new Page<>(pageReg.getCurrent(),pageReg.getSize());
        return courseService.seachcourse(course,page);
    }

    @DeleteMapping("/deleteCourse")
    public ResponseEntity<String> deleteCourse(@RequestBody List<String> cnoList) {
        if (cnoList.isEmpty()) {
            return ResponseEntity.badRequest().body("sno列表不能为空");
        }
        try {
            courseService.deleteByCnoList(cnoList);
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("删除失败: " + e.getMessage());
        }
    }

    @PutMapping("/reviseCourse")
    public ResponseEntity<String> reviseCourse(@RequestBody Course course,@RequestParam("oldcno") String oldcno) {
        if(course.getCno()==null){
            return ResponseEntity.badRequest().body("sno不能为空");
        }
        try{
            System.out.println("controller中：老sno："+oldcno);
            courseService.reviseCourse(course,oldcno);
            return ResponseEntity.ok("修改成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("修改失败: " + e.getMessage());
        }
    }

    @PutMapping("/addCourse")
    public String addCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }
}
