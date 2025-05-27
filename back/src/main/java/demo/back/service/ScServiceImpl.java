package demo.back.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.mapper.SearchScMapper;
import demo.back.pojo.Course;
import demo.back.pojo.Sc;
import demo.back.pojo.ScList;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class ScServiceImpl {

    @Autowired
    private SearchScMapper searchScMapper;


    public List<Sc> getTotals(Sc sc){
        LambdaQueryWrapper<Sc> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(sc.getSno())) {
            queryWrapper.eq(Sc::getSno, sc.getSno());
        }
        if (StringUtils.isNotEmpty(sc.getCno())) {
            queryWrapper.eq(Sc::getCno, sc.getCno());
        }
        if (StringUtils.isNotEmpty(sc.getGrade())) {
            queryWrapper.eq(Sc::getGrade, sc.getGrade());
        }
        return searchScMapper.selectList(queryWrapper);
    }

    public Page<Sc> seachsc(Sc sc, Page page){
        LambdaQueryWrapper<Sc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(sc.getSno()!="", Sc::getSno, sc.getSno())
                .eq(sc.getCno()!="", Sc::getCno, sc.getCno())
                .eq(sc.getGrade()!="", Sc::getGrade, sc.getGrade());
        return searchScMapper.selectPage(page, queryWrapper);
    }

    public void deleteBySnoCnoList(ScList scList){
        searchScMapper.deleteBySnoCnoList(scList);
    }

    public void reviseSc(Sc sc,String oldsno,String oldcno){
        searchScMapper.reviseSc(sc,oldsno,oldcno);
    }

    public String addSc(Sc sc){
        try {
            if (searchScMapper.exists(new LambdaQueryWrapper<Sc>()
                    .eq(Sc::getSno, sc.getSno())
                    .eq(Sc::getCno, sc.getCno()))) {
                return "学号与课程号组合已存在";
            }
            searchScMapper.insert(sc);
            return "添加成功";
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("主键冲突");
        }
    }


}
