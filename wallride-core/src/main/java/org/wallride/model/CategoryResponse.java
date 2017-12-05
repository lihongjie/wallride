package org.wallride.model;

public class CategoryResponse {

    private Long id;

    private String name;

    private Long num;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, Long num) {
        this.id = id;
        this.name = name;
        this.num = num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
