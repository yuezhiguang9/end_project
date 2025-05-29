package demo.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.back.pojo.MyFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface FileListMapper extends BaseMapper<MyFile> {
    MyFile[] selectAlll();
}
