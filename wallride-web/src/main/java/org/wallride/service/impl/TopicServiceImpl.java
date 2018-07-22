package org.wallride.service.impl;

import com.github.lihongjie.core.base.util.SequenceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallride.domain.Article;
import org.wallride.domain.News;
import org.wallride.domain.Topic;
import org.wallride.mapper.TopicMapper;
import org.wallride.model.TopicExample;
import org.wallride.model.TopicRequest;
import org.wallride.service.TopicService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public PageInfo<Topic> getTopics(TopicExample example) {

        PageHelper.startPage(example.getPageNum(), example.getPageSize());
        List<Topic> topics = topicMapper.selectByExample(example);
        return new PageInfo<>(topics);
    }

    @Override
    public List<News> getNewsByTopicIdAndLanguage(String id, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        return topicMapper.findNewsByTopicIdAndLanguage(params);
    }

    @Override
    public List<Article> getArticlesByTopicIdAndLanuage(String id, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        return topicMapper.findArticlesByTopicIdAndLanuage(params);
    }

    @Override
    public void createTopic(TopicRequest topicRequest) {

        Topic topic = topicRequest.buildTopic();
        topic.setId(SequenceUtils.nexSeqId());
        topicMapper.insertSelective(topic);
    }

    @Override
    public void updateTopic(TopicRequest topicRequest, String id) {

        Topic topic = topicRequest.buildTopic();
        topic.setId(id);
        topicMapper.updateByPrimaryKey(topic);
    }

    @Override
    public void deleteTopic(String id, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        topicMapper.deleteTopic(params);
    }

    @Override
    public Topic findOne(String id, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        return topicMapper.findOne(params);
    }

    @Override
    public List<Topic> findTopicsByTopicCategoryId(String topicCategoryId, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("topicCategoryId", topicCategoryId);
        params.put("language", language);
        return topicMapper.findTopicsByTopicCategoryId(params);
    }

    @Override
    public Topic findOneByCode(String code, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("language", language);
        return topicMapper.findOneByCode(params);
    }
}
