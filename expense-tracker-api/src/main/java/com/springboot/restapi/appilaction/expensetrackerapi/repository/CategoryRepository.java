package com.springboot.restapi.appilaction.expensetrackerapi.repository;

import java.util.List;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Category;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtBadRequestException;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtResourceNotFoundException;

public interface CategoryRepository {
    
    public List<Category> findAll(Integer userId) throws EtResourceNotFoundException;
    public Category findCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
    public Integer createCategory(Integer userId, String title, String description) throws EtBadRequestException;
    void updateCategory(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;
    void removeCategory(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
    
}
