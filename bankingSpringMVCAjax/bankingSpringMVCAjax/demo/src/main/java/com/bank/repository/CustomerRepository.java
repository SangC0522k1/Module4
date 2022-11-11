package com.bank.repository;

import com.bank.model.Customer;
import com.bank.model.dto.RecipientDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Iterable<Customer> findAllByDeletedIsFalse();

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Customer c SET c.balance = c.balance + :transferAmount WHERE c.id = :id")
    void incrementBalance(@Param("transferAmount")BigDecimal transferAmount, @Param("id") Long id );

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Customer c SET c.balance = c.balance - :transferAmount WHERE c.id = :id")
    void reduceBalance(@Param("transferAmount") BigDecimal transferAmount, @Param("id") Long id);

    @Query("SELECT c FROM Customer c WHERE c.deleted = false AND c.id <> :id")
    Iterable<Customer> findAllByDeletedIsFalseAndByIdWithOutSender(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Customer AS c SET c.deleted = true WHERE c.id = :customerId")
    void softDelete(@Param("customerId") Long customerId);
    @Query("SELECT NEW com.bank.model.dto.RecipientDTO(c.id, c.fullName) FROM Customer c WHERE c.deleted = false AND c.id <> :id")
    List<RecipientDTO> getAllRecipientDTO(@Param("id") Long id);


}
