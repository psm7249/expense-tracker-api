package com.springboot.restapi.appilaction.expensetrackerapi.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.springboot.restapi.appilaction.expensetrackerapi.domin.Transaction;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtBadRequestException;
import com.springboot.restapi.appilaction.expensetrackerapi.exception.EtResourceNotFoundException;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String SQL_CREATE = "INSERT INTO ET_TRANSACTIONS (TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE) VALUES(NEXTVAL('ET_TRANSACTIONS_SEQ'), ?, ?, ?, ?, ?)";
    public static final String SQL_FIND_BY_ID = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";
    public static final String SQL_FIND_ALL = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ?";
    public static final String SQL_UPDATE_TRANSACTION = "UPDATE ET_TRANSACTIONS SET AMOUNT = ?, NOTE = ?, TRANSACTION_DATE = ? WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";
    public static final String SQL_DELETE_TRANSACTION = "DELETE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";

    @Override
    public List<Transaction> findAllTransactions(Integer userId, Integer categoryId) {
        return jdbcTemplate.query(SQL_FIND_ALL, transactionRowMapper, userId, categoryId);
    }

    @Override
    public Transaction findTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, transactionRowMapper, userId, categoryId, transactionId);
        } catch(Exception e){
            e.printStackTrace();
            throw new EtResourceNotFoundException("Resourse not found");
        }
    }

    @Override
    public Integer createTransaction(Integer userId, Integer categoryId, Double amount, String note,
            Long transactionDate) throws EtBadRequestException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            jdbcTemplate.update( connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, categoryId);
                ps.setInt(2, userId);
                ps.setDouble(3, amount);
                ps.setString(4, note);
                ps.setLong(5, transactionDate);
                return ps;
            },keyHolder);
            return (Integer) keyHolder.getKeys().get("TRANSACTION_ID");
        } catch(Exception e) {
            e.printStackTrace();
            throw new EtBadRequestException("Invalid Request");
    }
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws EtBadRequestException {
        try{
            System.out.println(transaction.getAmount().toString() + transaction.getNote() + transaction.getTransactionDate());
            jdbcTemplate.update(SQL_UPDATE_TRANSACTION, transaction.getAmount(), transaction.getNote(), transaction.getTransactionDate(), userId, categoryId, transactionId);
        }catch(Exception e){
            e.printStackTrace();
            throw new EtBadRequestException("Invalid Request");
        } 
    }

    @Override
    public void removeTransaction(Integer userID, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE_TRANSACTION, userID, categoryId, transactionId);
        if(count == 0)
        throw new EtResourceNotFoundException("Resource not found");
    }

    public RowMapper<Transaction> transactionRowMapper = ( (rs, rowNum) ->
    new Transaction(
        rs.getInt("TRANSACTION_ID"), 
        rs.getInt("CATEGORY_ID"),
        rs.getInt("USER_ID"),
        rs.getDouble("AMOUNT"),
        rs.getString("NOTE"),
        rs.getLong("TRANSACTION_DATE"))
    ); 
    
}
