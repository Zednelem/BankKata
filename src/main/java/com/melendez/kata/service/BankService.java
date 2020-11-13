package com.melendez.kata.service;

/**
 * This service is responsible to call the database operation of the connected user
 */
public interface BankService {

    String depositMoney(double amount);

    String withdraw(double amount);
}
