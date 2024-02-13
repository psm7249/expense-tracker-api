package com.springboot.restapi.appilaction.expensetrackerapi.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Category;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtBadRequestException;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtResourceNotFoundException;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository{

    @Autowired 
    private JdbcTemplate jdbcTemplate;

    public static final String SQL_FIND_BY_ID = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, " +
    "COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
    "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " +
    "WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? GROUP BY C.CATEGORY_ID";
    public static final String SQL_FIND_ALL = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, " +
    "COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
    "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " +
    "WHERE C.USER_ID = ? GROUP BY C.CATEGORY_ID";
    public static final String SQL_CREATE = "INSERT INTO ET_CATEGORIES (CATEGORY_ID, USER_ID, TITLE, DESCRIPTION) VALUES(NEXTVAL('ET_CATEGORIES_SEQ'), ?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE ET_CATEGORIES SET TITLE = ?, DESCRIPTION = ? WHERE USER_ID = ? AND CATEGORY_ID = ?";
    public static final String SQL_DEL_CATEGORY = "DELETE FROM ET_CATEGORIES WHERE USER_ID = ? AND CATEGORY_ID = ?";
    public static final String SQL_DEL_ALL_TRANSACTION = "DELETE FROM ET_TRANSACTIONS WHERE CATEGORY_ID = ?";

    @Override
    public List<Category> findAll(Integer userId) throws EtResourceNotFoundException {
       try{
        return jdbcTemplate.query(SQL_FIND_ALL, userRowMapper, userId);
       } catch(Exception e){
        throw new EtResourceNotFoundException("User ID not found");
       }
    }

    @Override
    public Category findCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, userId, categoryId);
        } catch(Exception e){
            e.printStackTrace();
            throw new EtResourceNotFoundException("Category Id not found");
        }
    }

    @Override
    public Integer createCategory(Integer userId, String title, String description) throws EtBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            },keyHolder);
            return (Integer) keyHolder.getKeys().get("CATEGORY_ID");
        } catch(Exception e) {
            e.printStackTrace();
            throw new EtBadRequestException("Invalid Request");
        }
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
        try{
            jdbcTemplate.update(SQL_UPDATE, category.getTitle(), category.getDescription(), userId, categoryId);
        } catch(Exception e){
            throw new EtBadRequestException("Invalid Request");
        }
    }

    @Override
    public void removeCategory(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        this.removeTransactionsForCategory(categoryId);
        jdbcTemplate.update(SQL_DEL_CATEGORY, userId, categoryId);
    }

    public void removeTransactionsForCategory(Integer categoryId){
        jdbcTemplate.update(SQL_DEL_ALL_TRANSACTION, categoryId);
    }

    private RowMapper<Category> userRowMapper = (
        (rs, rowNum) -> new Category(rs.getInt("CATEGORY_ID"),
        rs.getInt("USER_ID"),
        rs.getString("TITLE"),
        rs.getString("DESCRIPTION"),
        rs.getDouble("TOTAL_EXPENSE")
        )
    );
    
}
