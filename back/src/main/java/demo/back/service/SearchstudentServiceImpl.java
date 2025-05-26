package demo.back.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.mapper.SearchstudentMapper;
import demo.back.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SearchstudentServiceImpl{
    // 实现类中无需额外实现getBaseMapper()

    @Autowired
    private SearchstudentMapper studentMapper;

//    查找操作
    public List<Student> seachstudent(Student student) {
//        System.out.println("service层中的page:"+page.getCurrent());
//        System.out.println("service层中的student:"+student.getSno());
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(student.getSno()!="", Student::getSno, student.getSno())       // 学号精确匹配
                .like(student.getSname()!="", Student::getSname, student.getSname()) // 姓名模糊查询
                .eq(student.getSsex()!="", Student::getSsex, student.getSsex())     // 性别精确匹配
                .eq(student.getSage()!="", Student::getSage, student.getSage())     // 年龄精确匹配
                .eq(student.getSdept()!="", Student::getSdept, student.getSdept()); // 系别精确匹配

        return studentMapper.selectList(queryWrapper);//使用Mapper CRUD接⼝
    }

//    删除操作
    public void deleteBySnoList(List<String> snoList) {
        studentMapper.deleteBySnoList(snoList);
    }

//    修改操作
    public void reviseStudent(Student student,Integer oldsno){
        studentMapper.reviseStudent(student,oldsno);
    }
}
