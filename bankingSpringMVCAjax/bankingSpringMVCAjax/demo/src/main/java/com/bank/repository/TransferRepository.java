package com.bank.repository;

import com.bank.model.Customer;
import com.bank.model.Transfer;
import com.bank.model.dto.TransferHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("SELECT SUM(feesAmount) FROM Transfer")
    BigDecimal sumFeesAmount();
    @Query("SELECT NEW com.bank.model.dto.TransferHistoryDTO (" +
            "t.id," +
            "t.createdAt," +
            "t.sender.id," +
            "t.sender.fullName," +
            "t.recipient.id," +
            "t.recipient.fullName," +
            "t.transferAmount," +
            "t.fees," +
            "t.feesAmount" +
            ") " +
            "FROM Transfer AS t"
    )
    List<TransferHistoryDTO> getAllHistories();
}
