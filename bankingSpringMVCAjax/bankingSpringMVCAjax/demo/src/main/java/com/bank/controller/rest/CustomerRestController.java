package com.bank.controller.rest;

import com.bank.exception.DataInputException;
import com.bank.model.Customer;
import com.bank.model.Deposit;
import com.bank.model.Transfer;
import com.bank.model.Withdraw;
import com.bank.model.dto.*;
import com.bank.service.customer.ICustomerService;
import com.bank.service.deposit.IDepositService;
import com.bank.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
    @Autowired
    private AppUtil appUtil;
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;
    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAll(){
        return new ResponseEntity<>((customerService.findAllByDeletedIsFalse()), HttpStatus.OK);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getById(@PathVariable Long customerId) {
        Optional<Customer> customerOptional = customerService.findById(customerId);
        if(!customerOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Customer customer = customerOptional.get();
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }

    @GetMapping("/get-all-recipients-without-sender/{senderId}")
    public ResponseEntity<?> getAllRecipientsWithoutSender(@PathVariable Long senderId){
        Optional<Customer> senderOptional = customerService.findById(senderId);
        if(!senderOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<RecipientDTO> recipientDTOS = customerService.getAllRecipientDTO(senderId);
        return new ResponseEntity<>(recipientDTOS, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<CustomerCreateDTO> create(@RequestBody CustomerCreateDTO customerDTO){
        customerDTO.setId(0L);
        Customer customer = customerDTO.toCustomer();
        Customer newCustomer = customerService.save(customer);
        customerDTO.setId(newCustomer.getId());
        customerDTO.setBalance("0");
        return new ResponseEntity<>(newCustomer.toCustomerCreateDTO(),HttpStatus.CREATED);
    }
    @PostMapping("/edit")
    public ResponseEntity<CustomerEditDTO> update (@RequestBody CustomerEditDTO customerDTO){
            Optional<Customer> customerOptional = customerService.findById(customerDTO.getId());
            if(!customerOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Customer customer = customerDTO.toCustomer();
            Customer newCustomer = customerService.save(customer);
            return new ResponseEntity<>(newCustomer.toCustomerEditDTO(), HttpStatus.ACCEPTED);
    }

    @PostMapping("/deposit")
    public ResponseEntity<CustomerDTO> depositAmount(@RequestBody DepositDTO depositDTO) {
        Long customerId = depositDTO.getCustomerId();
        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Deposit deposit = new Deposit();
        Customer customer = customerOptional.get();
        BigDecimal transactionAmount = new BigDecimal(depositDTO.getTransactionAmount());
        deposit.setTransactionAmount(transactionAmount);
        deposit.setCustomer(customer);
        Customer newCustomer = customerService.deposit(customer, deposit);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<CustomerDTO> withdrawAmount(@RequestBody WithdrawDTO withdrawDTO){
        Long customerId = withdrawDTO.getCustomerId();
        Optional<Customer> customerOptional = customerService.findById(customerId);
        if(!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal tracsactionAmount = new BigDecimal(withdrawDTO.getTransactionAmount());
        if(customerOptional.get().getBalance().compareTo(tracsactionAmount) < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Withdraw withdraw = new Withdraw();
        withdraw.setTransactionAmount(tracsactionAmount);
        withdraw.setCustomer(customerOptional.get());

        Customer newCustomer = customerService.withdraw(customerOptional.get(), withdraw);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<CustomerDTO> delete(@PathVariable Long customerId ){
        Optional<Customer> customerOptional = customerService.findById(customerId);
        if(!customerOptional.isPresent()){
            throw new DataInputException("Thông tin khách hàng không tồn tại");
        }
        try {
            customerService.softDelete(customerId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e){
            e.printStackTrace();
            throw new DataInputException("Vui lòng liên hệ Admin");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferDTO transferDTO, BindingResult bindingResult) {
       new TransferDTO().validate(transferDTO,bindingResult);
       if(bindingResult.hasFieldErrors()){
           return appUtil.mapErrorToResponse(bindingResult);
       }

       Optional<Customer> senderOptional = customerService.findById(transferDTO.getSenderId());
       if(!senderOptional.isPresent()){
           throw new DataInputException("Thông tin người gửi không hợp lệ");
       }

       Optional<Customer> recipientOptional = customerService.findById(transferDTO.getRecipientId());
       if(!recipientOptional.isPresent()){
           throw new DataInputException("Thông tin người nhận không hợp lệ");
       }

       Customer sender = senderOptional.get();

       BigDecimal currentBalanceSender = sender.getBalance();

       String transferAmountStr = transferDTO.getTransferAmount();
       BigDecimal transferAmount = new BigDecimal(transferAmountStr);
       int fees = 10;
       BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100L));
       BigDecimal transactionAmount = transferAmount.add(feesAmount);

       if(currentBalanceSender.compareTo(transactionAmount)<0){
           throw new DataInputException("Số dư người gửi không đủ thực hiện giao dịch");
       }

       try {
           Transfer transfer = new Transfer();
           transfer.setId(0L);
           transfer.setSender(sender);
           transfer.setRecipient(recipientOptional.get());
           transfer.setTransferAmount(transferAmount);
           transfer.setFees(fees);
           transfer.setFeesAmount(feesAmount);
           transfer.setTransactionAmount(transactionAmount);

           Customer newSender = customerService.transfer(transfer);

           Optional<Customer> newRecipient = customerService.findById(transferDTO.getRecipientId());

           Map<String, CustomerDTO> results = new HashMap<>();
           results.put("sender", newSender.toCustomerDTO());
           results.put("recipient", newRecipient.get().toCustomerDTO());

           return new ResponseEntity<>(results, HttpStatus.CREATED);
       } catch (Exception e){
           throw new DataInputException("VUi lòng liên hệ Administrator");
       }

    }

}
