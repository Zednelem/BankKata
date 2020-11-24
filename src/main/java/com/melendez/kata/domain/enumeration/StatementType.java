package com.melendez.kata.domain.enumeration;

/**
 * The StatementType enumeration.
 */
public enum StatementType {
    DEPOSIT, WITHDRAW;

    @Override
    public String toString() {
        return this.name();
    }
}
