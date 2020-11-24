package com.melendez.kata.service.dto;

import com.melendez.kata.domain.enumeration.StatementType;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.melendez.kata.domain.BankStatement} entity. This class is used
 * in {@link com.melendez.kata.web.rest.BankStatementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bank-statements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankStatementCriteria implements Serializable, Criteria {
    /**
     * Class for filtering StatementType
     */
    public static class StatementTypeFilter extends Filter<StatementType> {

        public StatementTypeFilter() {
        }

        public StatementTypeFilter(StatementTypeFilter filter) {
            super(filter);
        }

        @Override
        public StatementTypeFilter copy() {
            return new StatementTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter amount;

    private StringFilter label;

    private InstantFilter validatedDate;

    private StatementTypeFilter statementType;

    private LongFilter accountId;

    public BankStatementCriteria() {
    }

    public BankStatementCriteria(BankStatementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.validatedDate = other.validatedDate == null ? null : other.validatedDate.copy();
        this.statementType = other.statementType == null ? null : other.statementType.copy();
        this.accountId = other.accountId == null ? null : other.accountId.copy();
    }

    @Override
    public BankStatementCriteria copy() {
        return new BankStatementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public InstantFilter getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(InstantFilter validatedDate) {
        this.validatedDate = validatedDate;
    }

    public StatementTypeFilter getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementTypeFilter statementType) {
        this.statementType = statementType;
    }

    public LongFilter getAccountId() {
        return accountId;
    }

    public void setAccountId(LongFilter accountId) {
        this.accountId = accountId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BankStatementCriteria that = (BankStatementCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(label, that.label) &&
            Objects.equals(validatedDate, that.validatedDate) &&
            Objects.equals(statementType, that.statementType) &&
            Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        amount,
        label,
        validatedDate,
        statementType,
        accountId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankStatementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (validatedDate != null ? "validatedDate=" + validatedDate + ", " : "") +
                (statementType != null ? "statementType=" + statementType + ", " : "") +
                (accountId != null ? "accountId=" + accountId + ", " : "") +
            "}";
    }

}
