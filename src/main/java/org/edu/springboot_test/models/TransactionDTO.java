package org.edu.springboot_test.models;

import java.math.BigDecimal;

public class TransactionDTO {

    private Long originAccountDTO;
    private Long destinyAccountDTO;
    private BigDecimal amount;

    private Long bankId;

    public Long getOriginAccountDTO() {
        return originAccountDTO;
    }

    public void setOriginAccountDTO(Long originAccountDTO) {
        this.originAccountDTO = originAccountDTO;
    }

    public Long getDestinyAccountDTO() {
        return destinyAccountDTO;
    }

    public void setDestinyAccountDTO(Long destinyAccountDTO) {
        this.destinyAccountDTO = destinyAccountDTO;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }
}
