package demo.back.pojo;

import lombok.Data;

@Data
public class StudentPageRequest {
    private Student student;
    private PageReg pageReg;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public PageReg getPageReg() {
        return pageReg;
    }

    public void setPageReg(PageReg pageReg) {
        this.pageReg = pageReg;
    }
}
