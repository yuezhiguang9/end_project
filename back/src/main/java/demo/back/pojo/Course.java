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
    private String ccredit;
}
