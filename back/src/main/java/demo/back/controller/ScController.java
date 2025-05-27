package demo.back.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.back.pojo.PageReg;
import demo.back.pojo.Sc;
import demo.back.pojo.ScList;
import demo.back.pojo.Student;
import demo.back.service.ScServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ScController {
    @Autowired
    private ScServiceImpl scService;

    @GetMapping("/ScgetTotals")
    public List<Sc> ScgetTotals(String sno, String cno, String grade){
        Sc sc = new Sc();
        sc.setSno(sno);
        sc.setCno(cno);
        sc.setGrade(grade);
        System.out.println("!!!!"+sc.getCno());
        return scService.getTotals(sc);
    }

    @GetMapping("/searchSc")
    public Page<Sc> seachsc(String sno, String cno, String grade, Integer current, Integer size){
        Sc sc = new Sc();
        sc.setSno(sno);
        sc.setCno(cno);
        sc.setGrade(grade);
        PageReg pageReg = new PageReg(current,size);
        Page<Sc> page = new Page<>(pageReg.getCurrent(),pageReg.getSize());
        return scService.seachsc(sc,page);
    }

    @GetMapping("/deleteSc")
    public ResponseEntity<String> deleteSc(List<String> snoList, List<String> cnoList) {
        if (snoList.isEmpty() || cnoList.isEmpty()) {
            return ResponseEntity.badRequest().body("sno和cno列表不能为空");
        }
        if (snoList.size() != cnoList.size()){
            return ResponseEntity.badRequest().body("sno和cno列表长度不一致");
        }
        ScList scList = new ScList(snoList.toArray(new String[0]), cnoList.toArray(new String[0]));
        scService.deleteBySnoCnoList(scList);
        return ResponseEntity.ok("删除成功");
    }

    @PutMapping("/reviseSc")
    public ResponseEntity<String> reviseSc(@RequestBody Sc sc, @RequestParam("oldsno") String oldsno, @RequestParam("oldcno") String oldcno) {
        scService.reviseSc(sc,oldsno,oldcno);
        return ResponseEntity.ok("修改成功");
    }

    // ScController.java
    @PutMapping("/addSc")
    public ResponseEntity<String> addSc(@RequestBody Sc sc) { // 使用 @RequestBody 接收 JSON
        try {
            scService.addSc(sc);
            return ResponseEntity.ok("添加成功");
        } catch (DuplicateKeyException e) {
            return ResponseEntity.badRequest().body("学号与课程号组合已存在");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("添加失败: " + e.getMessage());
        }
    }

}
