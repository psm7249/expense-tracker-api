package com.springboot.restapi.appilaction.expensetrackerapi.services;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.User;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtAuthException;

public interface UserService {
    
    public User validateUser(String email, String password) throws EtAuthException;
    public User createUser(String firstName, String lastName, String email, String password) throws EtAuthException;
}
