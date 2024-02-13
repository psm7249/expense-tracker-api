package com.springboot.restapi.appilaction.expensetrackerapi.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Category;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtBadRequestException;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtResourceNotFoundException;

public interface CategoryService {
    
    public List<Category> fetchAllCategories(Integer userId);
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
    public Category addCategory(Integer userId, String title, String description) throws EtBadRequestException;
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;
    public void deleteCategoryWithAllTransactions(Integer userId, Integer categoryId) throws EtResourceNotFoundException;


}
