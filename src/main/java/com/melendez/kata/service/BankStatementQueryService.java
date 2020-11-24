package com.melendez.kata.service;

import com.melendez.kata.domain.BankAccount_;
import com.melendez.kata.domain.BankStatement;
import com.melendez.kata.domain.BankStatement_;
import com.melendez.kata.repository.BankStatementRepository;
import com.melendez.kata.service.dto.BankStatementCriteria;
import com.melendez.kata.service.dto.BankStatementDTO;
import com.melendez.kata.service.mapper.BankStatementMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link BankStatement} entities in the database.
 * The main input is a {@link BankStatementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BankStatementDTO} or a {@link Page} of {@link BankStatementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankStatementQueryService extends QueryService<BankStatement> {

    private final Logger log = LoggerFactory.getLogger(BankStatementQueryService.class);

    private final BankStatementRepository bankStatementRepository;

    private final BankStatementMapper bankStatementMapper;

    public BankStatementQueryService(BankStatementRepository bankStatementRepository, BankStatementMapper bankStatementMapper) {
        this.bankStatementRepository = bankStatementRepository;
        this.bankStatementMapper = bankStatementMapper;
    }

    /**
     * Return a {@link List} of {@link BankStatementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BankStatementDTO> findByCriteria(BankStatementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BankStatement> specification = createSpecification(criteria);
        return bankStatementMapper.toDto(bankStatementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BankStatementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankStatementDTO> findByCriteria(BankStatementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BankStatement> specification = createSpecification(criteria);
        return bankStatementRepository.findAll(specification, page)
            .map(bankStatementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankStatementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BankStatement> specification = createSpecification(criteria);
        return bankStatementRepository.count(specification);
    }

    /**
     * Function to convert {@link BankStatementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BankStatement> createSpecification(BankStatementCriteria criteria) {
        Specification<BankStatement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BankStatement_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), BankStatement_.amount));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), BankStatement_.label));
            }
            if (criteria.getValidatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidatedDate(), BankStatement_.validatedDate));
            }
            if (criteria.getStatementType() != null) {
                specification = specification.and(buildSpecification(criteria.getStatementType(), BankStatement_.statementType));
            }
            if (criteria.getAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getAccountId(),
                    root -> root.join(BankStatement_.account, JoinType.LEFT).get(BankAccount_.id)));
            }
        }
        return specification;
    }
}
