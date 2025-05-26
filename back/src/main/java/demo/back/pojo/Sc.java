package demo.back.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("sc")
public class Sc extends Model<Sc> {
    @TableId(value = "sno,cno",type = IdType.AUTO)
    private String sno;
    private String cno;
    private int  grade;
}
