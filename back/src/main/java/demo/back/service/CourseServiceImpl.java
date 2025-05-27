package demo.back.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.mapper.SearchcourseMapper;
import demo.back.mapper.SearchstudentMapper;
import demo.back.pojo.Course;
import demo.back.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class CourseServiceImpl {
    @Autowired
    private SearchcourseMapper searchcourseMapper;

    //    获取页数
    public List<Course> getTotals(Course course) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(course.getCno()!="", Course::getCno, course.getCno())
                .like(course.getCname()!="", Course::getCname, course.getCname())
                .eq(course.getCpon()!="", Course::getCpon, course.getCpon());

        // 调用 selectPage() 方法实现分页查询
        return searchcourseMapper.selectList(queryWrapper);
    }

    //    查找操作
    public Page<Course> seachcourse(Course course, Page page) {
        System.out.println("service层中的page:"+page.getCurrent());
        System.out.println("service层中的Course:"+course.getCname());
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(course.getCno()!="", Course::getCno, course.getCno())
                .like(course.getCname()!="", Course::getCname, course.getCname())
                .eq(course.getCpon()!="", Course::getCpon, course.getCpon());

        // 调用 selectPage() 方法实现分页查询
        return searchcourseMapper.selectPage(page, queryWrapper);
    }

    //    删除操作
    public void deleteByCnoList(List<String> cnoList) {
        searchcourseMapper.deleteByCnoList(cnoList);
    }

    //    修改操作
    public void reviseCourse(Course course,@RequestParam("oldcno") String oldcno){
        searchcourseMapper.reviseCourse(course,oldcno);
    }

    //    添加操作
    public String addCourse(Course course){
        try{
            searchcourseMapper.addCourse(course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "添加成功";
    }


}
