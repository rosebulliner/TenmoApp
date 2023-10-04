package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransfersDao implements TransferDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcTransfersDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;

    };


    //ADMIN METHOD REMOVE OR PROTECT
//    @Override
//    public List<Transfer> listTransfers() {
//        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT transfer_id, amount, datetime_requested, datetime_executed, status, sender_id, recipient_id " +
//                "FROM transfer;";
//        try {
//            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
//            while (result.next()){
//                transfers.add(mapToTransfer(result));
//            }
//
//        } catch (CannotGetJdbcConnectionException e){
//            throw new DaoException("Could not connect", e);
//        } catch (BadSqlGrammarException e){
//            throw new DaoException("SQL Grammar issue", e);
//        }
//
//        return transfers;
//    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, amount, datetime_requested, datetime_executed, status, sender_id, recipient_id, sender_username, recipient_username " +
                "FROM transfer WHERE transfer_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
            if (result.next()){
                transfer = mapToTransfer(result);
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }

        return transfer;
    }

    @Override
    public Transfer getTransferById(int transferId, String username) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ? AND (sender_username = ? OR recipient_username = ?);";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId, username, username);
            if (result.next()){
                transfer = mapToTransfer(result);
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }

        return transfer;
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, amount, datetime_requested, datetime_executed, status, sender_id, recipient_id, sender_username, recipient_username " +
        "FROM transfer WHERE sender_id = ? OR recipient_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            while (result.next()){
                transfers.add(mapToTransfer(result));
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, amount, datetime_requested, datetime_executed, status, sender_id, recipient_id, sender_username, recipient_username " +
                "FROM transfer WHERE sender_username = (SELECT username FROM tenmo_user WHERE user_id = ?) " +
                "OR recipient_username = (SELECT username FROM tenmo_user WHERE user_id = ?);";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (result.next()){
                transfers.add(mapToTransfer(result));
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }

        return transfers;
    }


    @Override
    public Transfer sendTransfer(Transfer transfer) {

        Transfer createdTransfer = null;
        String sql = "INSERT INTO transfer (amount, sender_id, recipient_id, datetime_executed, sender_username, recipient_username) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?) RETURNING transfer_id;";

        try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getAmount(), transfer.getSenderId(), transfer.getRecipientId(), transfer.getSenderUsername(), transfer.getRecipientUsername());
            createdTransfer = getTransferById(transferId);
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }


        return createdTransfer;
    }

    @Override
    public Transfer requestTransfer(Transfer transfer) {

        Transfer createdTransfer = null;
        String sql = "INSERT INTO transfer (amount, status, sender_username, recipient_username) " +
                "VALUES (?, 'Pending', ?, ?) RETURNING transfer_id;";

        try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getAmount(), transfer.getSenderUsername(), transfer.getRecipientUsername());
            createdTransfer = getTransferById(transferId);
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }


        return createdTransfer;
    }

    @Override
    public Transfer acceptTransfer(Transfer transfer) {
        Transfer acceptedTransfer = null;

        //make sure input transfer is pending...
        if (!isPending(transfer)){
            return null;
        }

        String sql = "UPDATE transfer SET status = 'Approved', datetime_executed = CURRENT_TIMESTAMP WHERE transfer_id = ?;";

        try {
            jdbcTemplate.update(sql, transfer.getTransferId());
            acceptedTransfer = getTransferById(transfer.getTransferId());
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }


        return acceptedTransfer;
    }

    @Override
    public Transfer denyTransfer(Transfer transfer) {

        if (!isPending(transfer)){
            return null;
        }
        Transfer deniedTransfer = null;
        String sql = "UPDATE transfer SET status = 'Rejected' WHERE transfer_id =?;";


        try {
            jdbcTemplate.update(sql, transfer.getTransferId());
            getTransferById(transfer.getTransferId()).setStatus("Rejected");
            deniedTransfer = getTransferById(transfer.getTransferId());
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }


        return deniedTransfer;
    }

    @Override
    public List<Transfer> listPendingTransfersForLoggedInUser(String username) {
        List<Transfer>pendingTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, amount, datetime_requested, datetime_executed, status, sender_id, recipient_id, sender_username, recipient_username " +
                "FROM transfer WHERE status = 'Pending' AND " +
                "(sender_username = ? OR recipient_username = ?);";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username, username);
            while (result.next()){
                pendingTransfers.add(mapToTransfer(result));
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }



        return pendingTransfers;
    }

    private Transfer mapToTransfer(SqlRowSet result){
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setAmount(result.getDouble("amount"));
        if (result.getTimestamp("datetime_requested") != null){
            transfer.setDateTimeRequested(result.getTimestamp("datetime_requested").toLocalDateTime());
        }
        if (result.getTimestamp("datetime_executed") != null){
            transfer.setDateTimeExecuted(result.getTimestamp("datetime_executed").toLocalDateTime());
        }
        transfer.setStatus(result.getString("status"));
        transfer.setSenderId(result.getInt("sender_id"));
        transfer.setRecipientId(result.getInt("recipient_id"));
        transfer.setSenderUsername(result.getString("sender_username"));
        transfer.setRecipientUsername(result.getString("recipient_username"));


        return transfer;
    }

    private boolean isPending(Transfer transfer){
        boolean isPending = false;
        String sql = "SELECT status FROM transfer WHERE transfer_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transfer.getTransferId());
            if (result.next()){
                String status = result.getString("status");
                if (status.equals("Pending")){
                    isPending = true;
                }
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }

        return isPending;
    }

    //might come back and delete this.
    private boolean isPending(int transferId){
        boolean isPending = false;
        String sql = "SELECT status FROM transfer WHERE transfer_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
            if (result.next()){
                String status = result.getString("status");
                if (status.equals("Pending")){
                    isPending = true;
                }
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Could not connect", e);
        } catch (BadSqlGrammarException e){
            throw new DaoException("SQL Grammar issue", e);
        }

        return isPending;
    }

}
