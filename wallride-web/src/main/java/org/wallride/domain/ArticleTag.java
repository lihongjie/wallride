package org.wallride.domain;

public class ArticleTag {

    private String id;

    private String articleId;

    private String tagId;


    public ArticleTag() {

    }

    public ArticleTag(String id, String articleId, String tagId) {
        this.id = id;
        this.articleId = articleId;
        this.tagId = tagId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
