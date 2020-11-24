package com.melendez.kata.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.melendez.kata.domain.enumeration.StatementType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A BankStatement.
 */
@Entity
@Table(name = "bank_statement")
public class BankStatement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    @Column(name = "label", length = 20, nullable = false)
    private String label;

    @Column(name = "validated_date")
    private Instant validatedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statement_type", nullable = false)
    private StatementType statementType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "bankStatements", allowSetters = true)
    private BankAccount account;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public BankStatement amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public BankStatement label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getValidatedDate() {
        return validatedDate;
    }

    public BankStatement validatedDate(Instant validatedDate) {
        this.validatedDate = validatedDate;
        return this;
    }

    public void setValidatedDate(Instant validatedDate) {
        this.validatedDate = validatedDate;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public BankStatement statementType(StatementType statementType) {
        this.statementType = statementType;
        return this;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    public BankAccount getAccount() {
        return account;
    }

    public BankStatement account(BankAccount bankAccount) {
        this.account = bankAccount;
        return this;
    }

    public void setAccount(BankAccount bankAccount) {
        this.account = bankAccount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankStatement)) {
            return false;
        }
        return id != null && id.equals(((BankStatement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankStatement{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", label='" + getLabel() + "'" +
            ", validatedDate='" + getValidatedDate() + "'" +
            ", statementType='" + getStatementType() + "'" +
            "}";
    }
}
