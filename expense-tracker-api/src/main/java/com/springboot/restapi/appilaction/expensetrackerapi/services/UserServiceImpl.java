package com.springboot.restapi.appilaction.expensetrackerapi.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.User;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtAuthException;
import com.springboot.restapi.appilaction.expensetrackerapi.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        if(email != null)
        email = email.toLowerCase();
        User user = userRepository.findUserByEmailandPassword(email, password);
        return user;
    }

    @Override
    public User createUser(String firstName, String lastName, String email, String password) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(!pattern.matcher(email).matches()){
            throw new EtAuthException("Invalid email entered");
        }
        email = email.toLowerCase();
        Integer count = userRepository.getCountByEmail(email);
        if(count>0){
            throw new EtAuthException("Email already exists");
        }
        Integer userId = userRepository.create(firstName,lastName,email,password);
        User user = userRepository.findUserByUserId(userId);
        return user;
    }
    
}
