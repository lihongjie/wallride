package org.wallride.mapper;

import org.apache.ibatis.annotations.Param;
import org.wallride.model.ArticleArchiveResponse;

import java.util.List;
import java.util.Map;

public interface StatisticsMapper {

    long countByStatusAndLanguage(Map<String, Object> params);

    long countByLanguage(@Param("language") String language);

    List<Map<String, Object>> countByAuthorAndStatusGrouped(Map<String, Object> params);

    List<Map<String, Object>> countByTopicIdGrouped(Map<String, Object> params);

    List<Map<String, Object>> countByTagIdGrouped(Map<String, Object> params);

    List<ArticleArchiveResponse> countByStatusGrouped(Map<String, Object> params);
}
