package demo.back.pojo;

import lombok.Data;

@Data
public class ScList {
    private String[] sno;
    private String[] cno;

    public ScList(String[] sno, String[] cno) {
        this.sno = sno;
        this.cno = cno;
    }
    
    public String[] getSno() {
        return sno;
    }

    public void setSno(String[] sno) {
        this.sno = sno;
    }

    public String[] getCno() {
        return cno;
    }

    public void setCno(String[] cno) {
        this.cno = cno;
    }
}
