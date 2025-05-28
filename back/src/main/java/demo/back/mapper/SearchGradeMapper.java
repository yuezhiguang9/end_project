package demo.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.back.pojo.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SearchGradeMapper extends BaseMapper<Grade> {
    // Mapper 接口

    @Update(
            "UPDATE grade_view " +
                    "SET grade = #{grade} "
                    +
                    "WHERE sno = #{sno} AND cname = #{cname}")
    int updateGrade(@Param("sno") String sno,
                    @Param("cname") String cname,
                    @Param("grade") Integer grade);


}
