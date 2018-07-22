package org.wallride.mapper;

import org.wallride.domain.Article;
import org.wallride.model.ArticleArchiveResponse;

import java.util.List;
import java.util.Map;

public interface ArticleMapper extends BaseMapper<Article, String> {

    Article findOneByIdAndLanguage(Map<String, Object> params);

    Article findOneByCodeAndLanguage(Map<String, Object> params);



    //next article
    Article findTopByIdIsAfterOrderByIdAsc(Map<String, Object> params);

    //prev article
    Article findTopByIdIsBeforeOrderByIdDesc(Map<String, Object> params);

    List<ArticleArchiveResponse> archiveArticles(Map<String, Object> params);

}
