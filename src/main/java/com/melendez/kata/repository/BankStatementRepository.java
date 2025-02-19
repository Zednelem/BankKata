package com.melendez.kata.repository;

import com.melendez.kata.domain.BankAccount;
import com.melendez.kata.domain.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the BankStatement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long>, JpaSpecificationExecutor<BankStatement> {
    List<BankStatement> findBankStatementsByAccount(BankAccount bankAccount);
}
