package com.springboot.restapi.appilaction.expensetrackerapi.repository;

import java.util.List;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Transaction;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtBadRequestException;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtResourceNotFoundException;

public interface TransactionRepository {
    
    public List<Transaction> findAllTransactions(Integer userId, Integer categoryId);
    public Transaction findTransactionById (Integer userID, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;
    public Integer createTransaction(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException;
    public void updateTransaction(Integer userID, Integer categoryId, Integer transactionId, Transaction transaction) throws EtBadRequestException;
    public void removeTransaction(Integer userID, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;

}
