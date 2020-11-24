package com.melendez.kata.service.dto;

import com.melendez.kata.domain.BankStatement;
import com.melendez.kata.domain.enumeration.StatementType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link BankStatement} entity.
 */
public class BankStatementDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(10)
    private Double amount;

    @NotNull
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    private String label;

    private Instant validatedDate;

    private StatementType statementType;


    private Long accountId;

    private String accountName;
    private String createdBy;
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(Instant validatedDate) {
        this.validatedDate = validatedDate;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long bankAccountId) {
        this.accountId = bankAccountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String bankAccountName) {
        this.accountName = bankAccountName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankStatementDTO)) {
            return false;
        }

        return id != null && id.equals(((BankStatementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankStatementDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", label='" + getLabel() + "'" +
            ", validatedDate='" + getValidatedDate() + "'" +
            ", statementType='" + getStatementType() + "'" +
            ", accountId=" + getAccountId() +
            ", accountName='" + getAccountName() + "'" +
            "}";
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }
}
