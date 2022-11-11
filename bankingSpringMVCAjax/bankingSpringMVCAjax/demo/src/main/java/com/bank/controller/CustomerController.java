package com.bank.controller;

import com.bank.model.Customer;
import com.bank.model.Deposit;
import com.bank.model.Transfer;
import com.bank.model.Withdraw;
import com.bank.service.customer.ICustomerService;
import com.bank.service.deposit.IDepositService;
import com.bank.service.transfer.ITransferService;
import com.bank.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private ITransferService transferService;

    @GetMapping("/customers")
    public ModelAndView showListPage(){
        Iterable<Customer> customers = customerService.findAllByDeletedIsFalse();
        ModelAndView modelAndView = new ModelAndView("customer/list");
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }
    @GetMapping("/create-customer")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("customer/create");
        modelAndView.addObject("customer",new Customer());
        return modelAndView;
    }
    @PostMapping("/create-customer")
    public ModelAndView saveCustomer(@ModelAttribute("customer") Customer customer){
        customer.setBalance(new BigDecimal(0L));
        customer.setDeleted(false);
        customerService.save(customer);
        ModelAndView modelAndView =new ModelAndView("customer/create");
        modelAndView.addObject("customer",new Customer());
        modelAndView.addObject("message", "New customer created successfully");
        return modelAndView;
    }
    @GetMapping("/edit-customer/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()){
            ModelAndView modelAndView = new ModelAndView("customer/edit");
            modelAndView.addObject("customer", customer.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/edit-customer/{id}")
    public ModelAndView updateCustomer(@ModelAttribute("customer") Customer customer){
        customer.setDeleted(false);
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("customer/edit");
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", "Customer updated successfully");
        return modelAndView;
    }
    @GetMapping("/suspended-customer/{id}")
    public ModelAndView showSuspendedForm(@PathVariable Long id){
        Optional<Customer> customer = customerService.findById(id);
        if(customer.isPresent()){
            ModelAndView modelAndView = new ModelAndView("customer/suspended");
            modelAndView.addObject("customer", customer.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/suspended-customer/{id}")
    public ModelAndView suspendedCustomer(@ModelAttribute("customer") Customer customer){
        customer.setDeleted(true);
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("customer/suspended");
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", "Suspended successfully");
        return modelAndView;
    }
    @GetMapping("/deposit-customer/{id}")
    public ModelAndView showDepositForm(@PathVariable Long id){
        Optional<Customer> customer = customerService.findById(id);
        if(customer.isPresent()){
            ModelAndView modelAndView = new ModelAndView("customer/deposit");
            modelAndView.addObject("deposit", new Deposit());
            modelAndView.addObject("customer", customer.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/deposit-customer/{id}")
    public ModelAndView addDeposit(@PathVariable Long id,
                                   @ModelAttribute("deposit") Deposit deposit){
        customerService.incrementBalance(deposit.getTransactionAmount(),id);
        Optional<Customer> customerOld = customerService.findById(id);
        Customer customer = customerOld.get();
        deposit.setCustomer(customer);
        deposit.setId(0L);
        depositService.save(deposit);
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("customer/deposit");
        modelAndView.addObject("deposit", new Deposit());
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", " Deposit successfully");
        return modelAndView;
    }
    @GetMapping("/withdraw-customer/{id}")
    public ModelAndView showWithdrawForm(@PathVariable Long id){
        Optional<Customer> customer = customerService.findById(id);
        if(customer.isPresent()){
            ModelAndView modelAndView = new ModelAndView("customer/withdraw");
            modelAndView.addObject("withdraw", new Withdraw());
            modelAndView.addObject("customer", customer.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/withdraw-customer/{id}")
    public ModelAndView saveWithdraw(@PathVariable Long id,
                                   @ModelAttribute("withdraw") Withdraw withdraw){
        customerService.reduceBalance(withdraw.getTransactionAmount(), id);
        Optional<Customer> customerOld = customerService.findById(id);
        Customer customer = customerOld.get();
        withdraw.setCustomer(customer);
        withdraw.setId(0L);
        withdrawService.save(withdraw);
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("customer/withdraw");
        modelAndView.addObject("withdraw", new Withdraw());
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", " Withdraw successfully");
        return modelAndView;
    }
    @GetMapping("/transfer-customer/{id}")
    public ModelAndView showTransferForm(@PathVariable Long id){
        Optional<Customer> customer = customerService.findById(id);
        Iterable<Customer> customerRecipient = customerService.findAllByDeletedIsFalseAndByIdWithOutSender(id);
      if(customer.isPresent()){
            ModelAndView modelAndView = new ModelAndView("customer/transfer");
            Transfer transfer = new Transfer();
            transfer.setFees(10);
            modelAndView.addObject("customerRecipient", customerRecipient);
            modelAndView.addObject("transfer", transfer);
            modelAndView.addObject("customer", customer.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @Transactional
    @PostMapping("/transfer-customer/{id}")
    public ModelAndView saveTransfer(@PathVariable Long id,
                                     @ModelAttribute("transfer") Transfer transfer){
        Optional<Customer> customerSenderOld = customerService.findById(id);
        Optional<Customer> customerRecipientOld = customerService.findById(transfer.getRecipient().getId());
        Customer customerSender = customerSenderOld.get();
        Customer customerRecipient = customerRecipientOld.get();

        BigDecimal transferAmount = transfer.getTransferAmount();
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        customerService.reduceBalance(transactionAmount,customerSender.getId());
        customerService.save(customerSender);

        customerService.incrementBalance(transferAmount,customerRecipient.getId());
        customerService.save(customerRecipient);

        transfer.setId(0L);
        transfer.setTransactionAmount(transactionAmount);
        transfer.setSender(customerSender);
        transfer.setFeesAmount(feesAmount);
        transferService.save(transfer);

        Iterable<Customer> customerRecipientList = customerService.findAllByDeletedIsFalseAndByIdWithOutSender(id);
        Optional<Customer> customer = customerService.findById(customerSender.getId());

        ModelAndView modelAndView = new ModelAndView("customer/transfer");
        modelAndView.addObject("customerRecipient", customerRecipientList);
        modelAndView.addObject("transfer", transfer);
        modelAndView.addObject("customer", customer.get());
        modelAndView.addObject("message", " Transfer successfully");
        return modelAndView;
    }

    @GetMapping("/transferInformation")
    public ModelAndView showListTransferInformation(){
        Iterable<Transfer> transfers = transferService.findfAll();
        BigDecimal totalFeeAmount = transferService.sumFeesAmount();
        ModelAndView modelAndView = new ModelAndView("customer/transferInformation");
        modelAndView.addObject("transfers", transfers);
        modelAndView.addObject("totalFeeAmount", totalFeeAmount);
        return modelAndView;
    }
}
