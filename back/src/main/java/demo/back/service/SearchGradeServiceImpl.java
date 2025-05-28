package demo.back.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.mapper.SearchGradeMapper;
import demo.back.pojo.Grade;
import demo.back.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
public class SearchGradeServiceImpl {

    @Autowired
    private SearchGradeMapper searchGradeMapper;

    public List<Grade> GradegetTotals(Grade grade) {
        LambdaQueryWrapper<Grade> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(grade.getSno() != "", Grade::getSno, grade.getSno())
                .like(grade.getSname() != "", Grade::getSname, grade.getSname())
                .eq(grade.getSdept() != "", Grade::getSdept, grade.getSdept())
                .eq(grade.getCname() != "", Grade::getCname, grade.getCname())
                .eq(grade.getSdept() != "", Grade::getSdept, grade.getSdept());

        return searchGradeMapper.selectList(queryWrapper);
    }

    public Page<Grade> seachGrade(Grade grade, Page page) {
        LambdaQueryWrapper<Grade> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(grade.getSno() != "", Grade::getSno, grade.getSno())
                .like(grade.getSname() != "", Grade::getSname, grade.getSname())
                .eq(grade.getSdept() != "", Grade::getSdept, grade.getSdept())
                .eq(grade.getCname() != "", Grade::getCname, grade.getCname());
        return searchGradeMapper.selectPage(page, queryWrapper);
    }

    public void updateGrade(Grade grade) {
        System.out.println("grade:" + grade.getCname() + " " + grade.getSno() + " " + grade.getGrade());
        searchGradeMapper.updateGrade(grade.getSno(), grade.getCname(), grade.getGrade());
    }
}
