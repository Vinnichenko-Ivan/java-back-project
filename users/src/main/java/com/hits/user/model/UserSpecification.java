package com.hits.user.model;

import com.hits.common.service.Utils;
import com.hits.user.dto.UserFiltersDto;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {
    private final UserFiltersDto userFiltersDto;

    public UserSpecification(UserFiltersDto userFiltersDto) {
        this.userFiltersDto = userFiltersDto;
    }

    private Predicate mainFilter(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and();
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = mainFilter(root, query, criteriaBuilder);
        if(userFiltersDto.getBirthDate() != null){
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get(User_.birthDate), userFiltersDto.getBirthDate())
            );
        }
        if(userFiltersDto.getRegistrationDate() != null){
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get(User_.registrationDate), userFiltersDto.getRegistrationDate())
            );
        }
        if(userFiltersDto.getSurname() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(User_.surname), Utils.toSQLReg(userFiltersDto.getSurname()))
            );
        }
        if(userFiltersDto.getName() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(User_.name), Utils.toSQLReg(userFiltersDto.getName()))
            );
        }
        if(userFiltersDto.getPatronymic() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(User_.patronymic), Utils.toSQLReg(userFiltersDto.getPatronymic()))
            );
        }
        if(userFiltersDto.getEmail() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(User_.email), Utils.toSQLReg(userFiltersDto.getEmail()))
            );
        }
        if(userFiltersDto.getPhone() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(User_.phone), Utils.toSQLReg(userFiltersDto.getPhone()))
            );
        }
        if(userFiltersDto.getCity() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(User_.city), Utils.toSQLReg(userFiltersDto.getCity()))
            );
        }
        if(userFiltersDto.getLogin() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(User_.login), Utils.toSQLReg(userFiltersDto.getLogin()))
            );
        }

        return predicate;
    }
}
