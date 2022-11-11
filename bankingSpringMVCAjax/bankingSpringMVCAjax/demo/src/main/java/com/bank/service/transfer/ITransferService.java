package com.bank.service.transfer;

import com.bank.model.Transfer;
import com.bank.model.dto.TransferHistoryDTO;
import com.bank.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ITransferService extends IGeneralService<Transfer> {
    BigDecimal sumFeesAmount();
    List<TransferHistoryDTO> getAllHistories();
}
