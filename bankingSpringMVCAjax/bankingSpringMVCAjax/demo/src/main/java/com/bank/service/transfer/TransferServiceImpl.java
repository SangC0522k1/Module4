package com.bank.service.transfer;

import com.bank.model.Transfer;
import com.bank.model.dto.TransferHistoryDTO;
import com.bank.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class TransferServiceImpl implements ITransferService{
    @Autowired
    private TransferRepository transferRepository;
    @Override
    public Iterable<Transfer> findfAll() {
        return transferRepository.findAll();
    }

    @Override
    public Transfer getById(Long id) {
        return null;
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Transfer save(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Override
    public void remove(Long id) {

    }
    @Override
    public BigDecimal sumFeesAmount() {
        return transferRepository.sumFeesAmount();
    }

    @Override
    public List<TransferHistoryDTO> getAllHistories() {
        return transferRepository.getAllHistories();
    }
}
