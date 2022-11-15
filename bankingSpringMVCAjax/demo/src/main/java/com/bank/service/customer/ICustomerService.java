package com.bank.service.customer;

import com.bank.model.Customer;
import com.bank.model.Deposit;
import com.bank.model.Transfer;
import com.bank.model.Withdraw;
import com.bank.model.dto.RecipientDTO;
import com.bank.service.IGeneralService;
import lombok.With;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer> {
Iterable<Customer>findAllByDeletedIsFalse();

Customer deposit(Customer customer, Deposit deposit);
Customer withdraw (Customer customer, Withdraw withdraw);

Customer transfer(Transfer transfer);

void incrementBalance(BigDecimal balance, Long id);

void reduceBalance (BigDecimal balance, Long id);

Iterable<Customer> findAllByDeletedIsFalseAndByIdWithOutSender(Long id);
void softDelete(@Param("customerId") Long customerId);
List<RecipientDTO> getAllRecipientDTO(Long id);
}
