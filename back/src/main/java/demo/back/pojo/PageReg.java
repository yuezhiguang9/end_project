package demo.back.pojo;

import lombok.Data;

@Data
public class PageReg {
//    请求第几页
    private Integer current = 1;
//    每页的记录数
    private Integer size = 5;

    public Integer getCurrent() {
        return current;
    }

    public Integer getSize() {
        return size;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
