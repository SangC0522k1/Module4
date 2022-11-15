package com.bank.service.customer;

import com.bank.model.Customer;
import com.bank.model.Deposit;
import com.bank.model.Transfer;
import com.bank.model.Withdraw;
import com.bank.model.dto.RecipientDTO;
import com.bank.repository.CustomerRepository;
import com.bank.repository.DepositRepository;
import com.bank.repository.TransferRepository;
import com.bank.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Override
    public Iterable<Customer> findfAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.getById(id);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        customer.setDeleted(false);
        return customerRepository.save(customer);
    }

    @Override
    public void remove(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Iterable<Customer> findAllByDeletedIsFalse() {
        return customerRepository.findAllByDeletedIsFalse();
    }

    @Override
    public Customer deposit(Customer customer, Deposit deposit) {
        BigDecimal transactionAmount = deposit.getTransactionAmount();
        BigDecimal currentBalance = customer.getBalance();
        try{
            customerRepository.incrementBalance(transactionAmount, customer.getId());
            deposit.setId(0L);
            deposit.setCustomer(customer);
            depositRepository.save(deposit);
            Optional<Customer> newCustomer = customerRepository.findById(customer.getId());
            return newCustomer.get();
        } catch (Exception e) {
            e.printStackTrace();
            customer.setBalance(currentBalance);
            return customer;
        }
    }

    @Override
    public Customer withdraw(Customer customer, Withdraw withdraw) {
        BigDecimal currentBalance = customer.getBalance();
        BigDecimal transactionAmount = withdraw.getTransactionAmount();
        try {
            customerRepository.reduceBalance(transactionAmount, customer.getId());

            withdraw.setId(0L);
            withdraw.setCustomer(customer);
            withdrawRepository.save(withdraw);

            Optional<Customer> newCustomer = customerRepository.findById(customer.getId());
            return newCustomer.get();
        } catch (Exception e) {
            e.printStackTrace();
            customer.setBalance(currentBalance);
            return customer;
        }
    }

    @Override
    public Customer transfer(Transfer transfer) {
        customerRepository.reduceBalance(transfer.getTransactionAmount(), transfer.getSender().getId());
        customerRepository.incrementBalance(transfer.getTransferAmount(), transfer.getRecipient().getId());
        transferRepository.save(transfer);
        Optional<Customer> newSender = customerRepository.findById(transfer.getSender().getId());

        return newSender.get();
    }

    @Override
    public void incrementBalance(BigDecimal balance, Long id) {
       customerRepository.incrementBalance(balance, id);
    }

    @Override
    public void reduceBalance(BigDecimal balance, Long id) {
        customerRepository.reduceBalance(balance, id);
    }

    @Override
    public Iterable<Customer> findAllByDeletedIsFalseAndByIdWithOutSender(Long id) {
        return customerRepository.findAllByDeletedIsFalseAndByIdWithOutSender(id);
    }

    @Override
    public void softDelete(Long customerId) {
        customerRepository.softDelete(customerId);
    }

    @Override
    public List<RecipientDTO> getAllRecipientDTO(Long id) {
        return customerRepository.getAllRecipientDTO(id);
    }
}
