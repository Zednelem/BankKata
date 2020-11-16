package com.melendez.kata.repository;

import com.melendez.kata.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {

    @Query("select bankAccount from BankAccount bankAccount where bankAccount.user.login = ?#{principal.username}")
    List<BankAccount> findByUserIsCurrentUser();
}
