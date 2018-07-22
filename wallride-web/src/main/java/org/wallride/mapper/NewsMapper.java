package org.wallride.mapper;

import org.wallride.domain.News;
import org.wallride.model.NewsExample;

import java.util.List;

public interface NewsMapper extends BaseMapper<News, String>{

    List<News> selectByExample(NewsExample example);
}
