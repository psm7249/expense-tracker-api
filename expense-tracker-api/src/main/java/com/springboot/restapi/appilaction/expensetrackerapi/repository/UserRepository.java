package com.springboot.restapi.appilaction.expensetrackerapi.repository;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.User;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtAuthException;

public interface UserRepository {

    public Integer getCountByEmail(String email) throws EtAuthException;
    public User findUserByUserId(Integer userId);
    public User findUserByEmailandPassword(String email, String password) throws EtAuthException;
    public Integer create(String firstName, String lastName, String email, String password);
}
