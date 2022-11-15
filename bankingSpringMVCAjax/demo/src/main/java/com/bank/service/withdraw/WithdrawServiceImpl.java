package com.bank.service.withdraw;

import com.bank.model.Withdraw;
import com.bank.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
@Service
@Transactional
public class WithdrawServiceImpl implements IWithdrawService{
    @Autowired
    private WithdrawRepository withdrawRepository;
    @Override
    public Iterable<Withdraw> findfAll() {
        return null;
    }

    @Override
    public Withdraw getById(Long id) {
        return null;
    }

    @Override
    public Optional<Withdraw> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Withdraw save(Withdraw withdraw) {
        return withdrawRepository.save(withdraw);
    }

    @Override
    public void remove(Long id) {

    }
}
