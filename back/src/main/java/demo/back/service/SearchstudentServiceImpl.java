package demo.back.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.mapper.SearchstudentMapper;
import demo.back.pojo.Student;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Service
public class SearchstudentServiceImpl{
    // 实现类中无需额外实现getBaseMapper()

    @Autowired
    private SearchstudentMapper studentMapper;

//    获取页数
    public List<Student> getTotals(Student student) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(student.getSno()!="", Student::getSno, student.getSno())
                .like(student.getSname()!="", Student::getSname, student.getSname())
                .eq(student.getSsex()!="", Student::getSsex, student.getSsex())
                .eq(student.getSage() != "", Student::getSage, student.getSage())  // 年龄用 Integer 判空
                .eq(student.getSdept()!="", Student::getSdept, student.getSdept());

        // 调用 selectPage() 方法实现分页查询
        return studentMapper.selectList(queryWrapper);
    }

//    查找操作
    public Page<Student> seachstudent(Student student,Page page) {
        System.out.println("service层中的page:"+page.getCurrent());
        System.out.println("service层中的student:"+student.getSno());
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(student.getSno()!="", Student::getSno, student.getSno())
                .like(student.getSname()!="", Student::getSname, student.getSname())
                .eq(student.getSsex()!="", Student::getSsex, student.getSsex())
                .eq(student.getSage() != "", Student::getSage, student.getSage())  // 年龄用 Integer 判空
                .eq(student.getSdept()!="", Student::getSdept, student.getSdept());

        // 调用 selectPage() 方法实现分页查询
        return studentMapper.selectPage(page, queryWrapper);
    }

//    删除操作
    public void deleteBySnoList(List<String> snoList) {
        studentMapper.deleteBySnoList(snoList);
    }

//    修改操作
    public void reviseStudent(Student student,@RequestParam("oldsno") String oldsno){
        studentMapper.reviseStudent(student,oldsno);
    }

//    添加操作
    public String addStudent(Student student){
        try{
            studentMapper.addStudent(student);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "添加成功";
    }
}
