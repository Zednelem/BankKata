package com.melendez.kata.service.mapper;


import com.melendez.kata.domain.*;
import com.melendez.kata.service.dto.BankStatementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankStatement} and its DTO {@link BankStatementDTO}.
 */
@Mapper(componentModel = "spring", uses = {BankAccountMapper.class})
public interface BankStatementMapper extends EntityMapper<BankStatementDTO, BankStatement> {

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.name", target = "accountName")
    BankStatementDTO toDto(BankStatement bankStatement);

    @Mapping(source = "accountId", target = "account")
    BankStatement toEntity(BankStatementDTO bankStatementDTO);

    default BankStatement fromId(Long id) {
        if (id == null) {
            return null;
        }
        BankStatement bankStatement = new BankStatement();
        bankStatement.setId(id);
        return bankStatement;
    }
}
