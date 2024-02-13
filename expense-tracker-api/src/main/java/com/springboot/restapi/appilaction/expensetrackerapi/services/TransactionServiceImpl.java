package com.springboot.restapi.appilaction.expensetrackerapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Transaction;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtBadRequestException;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtResourceNotFoundException;
import com.springboot.restapi.appilaction.expensetrackerapi.repository.TransactionRepository;

@Service
@Transactional
public class TransactionServiceImpl  implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
        return transactionRepository.findAllTransactions(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        return transactionRepository.findTransactionById(userId, categoryId, transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note,
            Long transactionDate) throws EtBadRequestException {
                Integer transactionId = transactionRepository.createTransaction(userId, categoryId, amount, note, transactionDate);
                return  transactionRepository.findTransactionById(userId, categoryId, transactionId);
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws EtBadRequestException {
         transactionRepository.updateTransaction(userId, categoryId, transactionId, transaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        transactionRepository.removeTransaction(userId, categoryId, transactionId);
    }
    
}
