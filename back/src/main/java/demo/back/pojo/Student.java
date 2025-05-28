package demo.back.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("Student")
public class Student extends Model<Student> {
    @TableId(value = "sno", type = IdType.AUTO)
    private String sno;
    private String sname;
    private String ssex;
    private String sage;
    private String sdept;

    public Student(String sno, String sname, String ssex, String sage, String sdept) {
        this.sno = sno;
        this.sname = sname;
        this.ssex = ssex;
        this.sage = sage;
        this.sdept = sdept;
    }

    public String getAll() {
        return this.sno + this.sname + this.ssex + this.sage + this.sdept;
    }

    public Student() {
        
    }

    public String getSno() {
        return sno;
    }

    public String getSname() {
        return sname;
    }

    public String getSsex() {
        return ssex;
    }

    public String getSage() {
        return sage;
    }

    public String getSdept() {
        return sdept;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setSage(String sage) {
        this.sage = sage;
    }

    public void setSsex(String ssex) {
        this.ssex = ssex;
    }

    public void setSdept(String sdept) {
        this.sdept = sdept;
    }
}
