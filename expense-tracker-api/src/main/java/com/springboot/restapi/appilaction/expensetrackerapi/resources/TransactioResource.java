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

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Transaction;
import com.springboot.restapi.appilaction.expensetrackerapi.services.TransactionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
public class TransactioResource {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("")
    public ResponseEntity<List<Transaction>> getAllTransactions(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        List<Transaction> transactionList = transactionService.fetchAllTransactions(userId, categoryId);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId){
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Transaction> addTransaction(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId, @RequestBody Map<String, Object> map){
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        Double amount = Double.valueOf(map.get("amount").toString()) ;
        String note = (String) map.get("note");
        long transactionDate = (long) map.get("transactionDate");
        Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, transactionDate);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId, @RequestBody Transaction transaction){
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
        Map<String, Boolean> map = new HashMap<>();
        map.put("Transaction Added Successfully", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
public ResponseEntity<Map<String, Boolean>> deleteTransactions(HttpServletRequest httpServletRequest, @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId){
    Integer userId = (Integer) httpServletRequest.getAttribute("userId");
    transactionService.removeTransaction(userId, categoryId, transactionId);
    Map<String, Boolean> map = new HashMap<>();
        map.put("Transaction Deleted Successfully", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
} 



}
