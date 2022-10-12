package com.codegym.service.Transfer;

import com.codegym.model.Customer;
import com.codegym.model.Transfer;
import com.codegym.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface TransferService extends IGeneralService<Transfer> {
    String doTransfer(Transfer transfer);
    BigDecimal getSumFeesAmount();
}
