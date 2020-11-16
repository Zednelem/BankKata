package com.melendez.kata.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.melendez.kata.domain.BankAccount} entity. This class is used
 * in {@link com.melendez.kata.web.rest.BankAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bank-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankAccountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter balance;

    private StringFilter name;

    private LongFilter userId;

    public BankAccountCriteria() {
    }

    public BankAccountCriteria(BankAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.balance = other.balance == null ? null : other.balance.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public BankAccountCriteria copy() {
        return new BankAccountCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getBalance() {
        return balance;
    }

    public void setBalance(DoubleFilter balance) {
        this.balance = balance;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BankAccountCriteria that = (BankAccountCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(name, that.name) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        balance,
        name,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankAccountCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
