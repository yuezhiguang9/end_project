package demo.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.back.pojo.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchcourseMapper extends BaseMapper<Course> {
    int deleteByCnoList(List<String> cnoList);

    int reviseCourse(Course course,String oldcno);

    int addCourse(Course course);
}
