package org.wallride.mapper;

import org.apache.ibatis.annotations.Param;
import org.wallride.domain.Tag;

import java.util.List;
import java.util.Map;

public interface TagMapper extends BaseMapper<Tag, String> {

    Tag findOneByIdAndLanguage(Map<String, Object> params);

    Tag findOneByNameAndLanguage(Map<String, Object> params);

    List<Tag> findAllByLanguage(@Param("language") String language);

    List<Tag> findTagsByArticleId(@Param("id") String id);

}
