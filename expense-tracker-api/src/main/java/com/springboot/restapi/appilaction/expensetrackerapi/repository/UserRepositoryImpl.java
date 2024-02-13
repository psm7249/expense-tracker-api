package com.springboot.restapi.appilaction.expensetrackerapi.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.mindrot.jbcrypt.BCrypt;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.User;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtAuthException;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String SQL_CREATE = "INSERT INTO ET_USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES(NEXTVAL('ET_USERS_SEQ'), ?, ?, ?, ?)";
    public static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?";
    public static final String SQL_FIND_BY_USER_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM ET_USERS WHERE USER_ID = ?"; 
    public static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM ET_USERS WHERE EMAIL = ?";

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, new Object[]{email});
    }

    @Override
    public User findUserByUserId(Integer userId){
        return jdbcTemplate.queryForObject(SQL_FIND_BY_USER_ID, userRowMapper, new Object[]{userId});
    }

    @Override
    public User findUserByEmailandPassword(String email, String password) throws EtAuthException {
        try{User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, email);
        if(!BCrypt.checkpw(password, user.password)){
            throw new EtAuthException("Invaild password");
        }
        return user;
    } catch(EmptyResultDataAccessException e){
        e.printStackTrace();
            throw new EtAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer create(String firstName, String lastName, String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, hashedPassword);
            return ps;
        }, keyHolder);
        return (Integer) keyHolder.getKeys().get("USER_ID");
    } catch (Exception e){
            throw new EtAuthException("Invalid details. Failed to create account");
        }
    }

    private RowMapper<User> userRowMapper = ((rs,rowNum) -> 
         new User(rs.getInt("USER_ID"),
        rs.getString("FIRST_NAME"),
        rs.getString("LAST_NAME"),
        rs.getString("EMAIL"),
        rs.getString("PASSWORD"))
    ); 
}
