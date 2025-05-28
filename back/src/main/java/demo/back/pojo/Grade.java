package demo.back.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grade_view")
public class Grade {
    @TableId(type = IdType.INPUT)
    private String sno;
    
    private String sname;
    private String sdept;
    private String cname;
    private Integer grade;

    public Grade(String sno, String sname, String sdept, String cname, Integer grade) {
        this.sno = sno;
        this.sname = sname;
        this.sdept = sdept;
        this.cname = cname;
        this.grade = grade;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSdept() {
        return sdept;
    }

    public void setSdept(String sdept) {
        this.sdept = sdept;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
