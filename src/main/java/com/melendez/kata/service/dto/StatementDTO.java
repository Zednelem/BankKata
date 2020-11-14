package com.melendez.kata.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;

public class StatementDTO {

    private Long id;
    @Size(min = 4,max = 25)
    @Pattern(regexp = "^[A-Za-z ]*$")
    private String createdBy;
    private Instant createdDate;
    private Instant validatedDate;
    @Pattern(regexp = "^[A-Za-z ]*$")
    @Size(min = 4,max = 25)
    private String label;
    private StatementType type;
    @Min(10)
    private double amount;

    public StatementDTO() {
    }

    public StatementDTO(long id,
                        String createdBy,
                        Instant createdDate,
                        Instant validatedDate,
                        String label,
                        StatementType type,
                        long amount) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.validatedDate = validatedDate;
        this.label = label;
        this.type = type;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(Instant validatedDate) {
        this.validatedDate = validatedDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public StatementType getType() {
        return type;
    }

    public void setType(StatementType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public enum StatementType {
        DEPOSIT,WITHDRAW;

        @Override
        public String toString() {
            return this.name();
        }
    }
}
