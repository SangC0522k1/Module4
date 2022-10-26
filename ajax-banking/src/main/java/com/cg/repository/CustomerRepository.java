package com.cg.repository;


import com.cg.model.Customer;
import com.cg.model.dto.RecipientDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByDeletedIsFalse();

    List<Customer> findAllByIdNot(long id);

    Boolean existsByIdEquals(long id);

    List<Customer> getAllByIdNot(long senderId);

    @Query("SELECT NEW com.cg.model.dto.RecipientDTO(" +
                "c.id, " +
                "c.fullName" +
            ") " +
            "FROM Customer AS c " +
            "WHERE c.id <> :senderId "
    )
    List<RecipientDTO> getAllRecipientDTO(@Param("senderId") long senderId);


    @Modifying
    @Query("UPDATE Customer AS c " +
            "SET c.balance = c.balance + :transactionAmount " +
            "WHERE c.id = :customerId"
    )
    void incrementBalance(@Param("transactionAmount") BigDecimal transactionAmount, @Param("customerId") long customerId);


    @Modifying
    @Query("UPDATE Customer AS c " +
            "SET c.balance = c.balance - :transactionAmount " +
            "WHERE c.id = :customerId"
    )
    void reduceBalance(@Param("transactionAmount") BigDecimal transactionAmount, @Param("customerId") long customerId);


    @Modifying
    @Query("UPDATE Customer AS c SET c.deleted = true WHERE c.id = :customerId")
    void softDelete(@Param("customerId") long customerId);
}
