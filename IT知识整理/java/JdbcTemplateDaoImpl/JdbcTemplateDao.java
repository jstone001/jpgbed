package com.sw.dao;

import java.util.List;

/**
 * Created by Administrator on 2018/6/21 0021.
 */
public interface JdbcTemplateDao {

    public <T> List<T> queryForList(String sql, Class<T> clazz, Object... args);

    public  List<Integer> queryForIntList(String sql, Object... args);

    public int queryForInt(String sql, Object... args);

    public String queryForString(String sql, Object... args);

    public <T> T queryForObject(String sql, Class<T> clazz, Object... args);

    int insert(String sql, Object... args);

    int updateClob(String sql, String url, String content);

    int update(String sql, Object... args);

    <T> int queryClob(String sql, Class<T> clazz, String urlNum, String filePath);



}

