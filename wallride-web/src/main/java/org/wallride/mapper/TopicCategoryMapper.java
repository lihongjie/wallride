package org.wallride.mapper;

import org.wallride.domain.TopicCategory;

import java.util.List;
import java.util.Map;

public interface TopicCategoryMapper extends BaseMapper<TopicCategory, String> {

    List<TopicCategory> findTopicCategoriesByParentId(String id);

    void deleteTopicCategoryById(Map<String, Object> params);

    TopicCategory findOne(Map<String, Object> params);

    TopicCategory findOneByCode(Map<String, Object> params);

    List<TopicCategory> findCategoriesByParentId(Map<String, Object> params);
}
