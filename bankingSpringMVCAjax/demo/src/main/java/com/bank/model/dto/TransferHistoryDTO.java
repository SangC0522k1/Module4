package com.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class TransferHistoryDTO {
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
    private BigDecimal transferAmount;
    private int fees;
    private BigDecimal feesAmount;
}
