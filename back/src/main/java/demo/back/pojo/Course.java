package demo.back.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("Course")
public class Course extends Model<Course> {
    @TableId(value = "cno",type = IdType.AUTO)
    private String cno;
    private String cname;
    private String cpon;

    public Course(String cno, String cname, String cpon) {
        this.cno = cno;
        this.cname = cname;
        this.cpon = cpon;
    }


    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCpon() {
        return cpon;
    }

    public void setCpon(String cpon) {
        this.cpon = cpon;
    }

}
