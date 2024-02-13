package com.springboot.restapi.appilaction.expensetrackerapi.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Category;
import com.springboot.restapi.appilaction.expensetrackerapi.services.CategoryService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/categories")
public class CategoryResourse {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<Category>> findAllCategories(HttpServletRequest httpServletRequest){
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        List<Category> allCategories = categoryService.fetchAllCategories(userId);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(HttpServletRequest request,
                                                    @PathVariable("categoryId") Integer categoryId){
        int userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(userId,categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Category> createCategory(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> map){
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        String tilte = (String) map.get("title");
        String description = (String) map.get("description");
        Category category = categoryService.addCategory(userId, tilte, description);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> updateCategory(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId, @RequestBody Category category){
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        categoryService.updateCategory(userId, categoryId,category);
        Map<String, Boolean> map = new HashMap<>();
        map.put("Category updated", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> updateCategory(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId){
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        categoryService.deleteCategoryWithAllTransactions(userId, categoryId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("Category Deleted Successfully", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
