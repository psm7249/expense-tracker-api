package com.springboot.restapi.appilaction.expensetrackerapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Category;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtBadRequestException;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtResourceNotFoundException;
import com.springboot.restapi.appilaction.expensetrackerapi.repository.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchAllCategories(Integer userId) {
        return categoryRepository.findAll(userId);
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        return categoryRepository.findCategoryById(userId, categoryId);
    }

    @Override
    public Category addCategory(Integer userId, String title, String description) throws EtBadRequestException {
        Integer categoryId = categoryRepository.createCategory(userId, title, description);
        return categoryRepository.findCategoryById(userId, categoryId);
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
        categoryRepository.updateCategory(userId, categoryId, category);
    }

    @Override
    public void deleteCategoryWithAllTransactions(Integer userId, Integer categoryId)
            throws EtResourceNotFoundException {
        this.fetchCategoryById(userId, categoryId);
        categoryRepository.removeCategory(userId, categoryId);
    }
    
}
