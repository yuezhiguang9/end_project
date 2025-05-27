package demo.back.pojo;

import lombok.Data;

@Data
public class PageReg {
//    请求第几页
    private Integer Current = 1;
//    每页的记录数
    private Integer Size = 20;
    public PageReg(Integer current,Integer size){
        this.Current = current;
        this.Size = size;
    }

    public Integer getCurrent() {
        return Current;
    }

    public void setCurrent(Integer current) {
        Current = current;
    }

    public Integer getSize() {
        return Size;
    }

    public void setSize(Integer size) {
        Size = size;
    }
}
