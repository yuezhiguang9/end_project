package demo.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.back.pojo.Sc;
import demo.back.pojo.ScList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface SearchScMapper extends BaseMapper<Sc> {
    int deleteBySnoCnoList(@Param("scList") ScList scList);
    int reviseSc(@Param("sc") Sc sc, @RequestParam("oldsno") String oldsno, @RequestParam("oldcno") String oldcno);
    int addSc(@Param("sc") Sc sc);
}
