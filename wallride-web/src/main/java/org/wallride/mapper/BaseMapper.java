package org.wallride.mapper;

import org.wallride.model.Example;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T, ID extends Serializable> {

    List<T> selectByExample(Example example);

    T selectByPrimaryKey(ID id);

    int deleteByPrimaryKey(ID id);

    int insertSelective(T record);

    int insertInBatch(Iterable<T> entities);

    int updateByPrimaryKey(T record);

    List<T> findAll();

    void deleteInBatch(Iterable<ID> ids);

    void deleteAllInBatch();
}
