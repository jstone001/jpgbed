package com.sw.dao.impl;

import com.sw.dao.JdbcTemplateDao;
import com.sw.mapper.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/20 0020.
 */
@Repository
public class JdbcTemplateDaoImpl implements JdbcTemplateDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public <T> List<T> queryForList(String sql, Class<T> clazz, Object... args) {

        List<T> result = jdbcTemplate.query(sql, args, new UserRowMapper(clazz));
        return result == null ? new ArrayList<T>() : result;
    }

    //queryForList中new UserRowMapper(clazz)) Integer不能用
    public  List<Integer> queryForIntList(String sql, Object... args) {

        List<Integer> result = jdbcTemplate.queryForList(sql,Integer.class,args);
        return result == null ? new ArrayList<>() : result;
    }

    public int queryForInt(String sql, Object... args) {
        Object o = jdbcTemplate.queryForObject(sql, args, Integer.class);
        if (o != null) {
            return Integer.parseInt(o.toString());
        } else {
            return 0;
        }

    }

    @Override
    public String queryForString(String sql, Object... args) {
        String s = jdbcTemplate.queryForObject(sql, args, String.class);
        return s;
    }

    public <T> T queryForObject(String sql, Class<T> clazz, Object... args) {

        T result = (T) jdbcTemplate.queryForObject(sql, args, new UserRowMapper(clazz));
        return result;
    }

    public int insert(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    @Override
    public int updateClob(String sql, String url, String content) {
        int j = 0;

        return j;
    }

    @Override
    public int update(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    @Override
    public <T> int queryClob(String sql, Class<T> clazz, String urlNum, String filePath) {

        return 0;
    }


}

