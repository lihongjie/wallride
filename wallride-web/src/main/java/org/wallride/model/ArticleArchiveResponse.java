package org.wallride.model;

public class ArticleArchiveResponse {

    private String id;

    private Integer year;

    private Integer month;

    private Long num;

    public ArticleArchiveResponse(String id, Integer year, Integer month, Long num) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
