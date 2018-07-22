package org.wallride.service;

import org.wallride.domain.Article;

import java.util.Map;

public interface StatisticsService {

    Long countArticles(String language);

    Long countArticlesByStatus(Article.Status status, String language);

    Map<Long, Long> countArticlesByAuthorIdGrouped(Article.Status status, String language);

    Map<Long, Long> countArticlesByCategoryIdGrouped(Article.Status status, String language);

    Map<Long, Long> countArticlesByTagIdGrouped(Article.Status status, String language);
}
