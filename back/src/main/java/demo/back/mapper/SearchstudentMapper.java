package demo.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.back.pojo.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface SearchstudentMapper extends BaseMapper<Student> {
    int deleteBySnoList(@Param("snoList") List<String> snoList);
    int reviseStudent(@Param("student") Student student,
                      @RequestParam("oldsno") String oldsno);
}
