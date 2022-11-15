package com.bank.service.deposit;

import com.bank.model.Deposit;
import com.bank.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
@Service
@Transactional
public class DepositServiceImpl implements IDepositService{
    @Autowired
    private DepositRepository depositRepository;
    @Override
    public Iterable<Deposit> findfAll() {
        return null;
    }

    @Override
    public Deposit getById(Long id) {
        return null;
    }

    @Override
    public Optional<Deposit> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Deposit save(Deposit deposit) {
        deposit.setDeleted(false);
        return depositRepository.save(deposit);
    }

    @Override
    public void remove(Long id) {

    }
}
