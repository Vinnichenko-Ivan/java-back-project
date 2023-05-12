package com.hits.chat.model;

import com.hits.common.service.Utils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class ChatSpecification implements Specification<Chat> {

    private final String find;
    private final UUID userId;
    public ChatSpecification(String find, UUID userId) {
        this.find = find;
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Chat> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(
                criteriaBuilder.isMember(userId, root.get(Chat_.users)),
                criteriaBuilder.like(root.get(Chat_.name), Utils.toSQLReg(find))
        );
    }
}
