package demo.back.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("sc")
public class Sc extends Model<Sc> {
    @TableField("sno")
    private String sno;

    @TableField("cno")
    private String cno;
    
    @TableField("grade")
    private String  grade;

    public Sc(String sno, String cno, String grade) {
        this.sno = sno;
        this.cno = cno;
        this.grade = grade;
    }

    public Sc() {

    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    
}
