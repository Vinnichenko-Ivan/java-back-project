package com.hits.friends.model;

import com.hits.common.service.Utils;
import com.hits.friends.dto.QueryRelationFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class FriendshipSpecification implements Specification<Friendship> {

    private final QueryRelationFilter queryRelationFilter;
    private final UUID mainId;

    public FriendshipSpecification(QueryRelationFilter queryRelationFilter, UUID mainId) {
        this.queryRelationFilter = queryRelationFilter;
        this.mainId = mainId;
    }

    @Override
    public Predicate toPredicate(Root<Friendship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and(criteriaBuilder.isNull(root.get(Friendship_.dateEnd)),
                criteriaBuilder.equal(root.get(Friendship_.mainUser), mainId));
        if(queryRelationFilter.getPatronymic() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                    root.get(Friendship_.patronymicTarget), Utils.toSQLReg(queryRelationFilter.getPatronymic())
            ));
        }
        if(queryRelationFilter.getSurname() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                    root.get(Friendship_.surnameTarget), Utils.toSQLReg(queryRelationFilter.getSurname())
            ));
        }
        if(queryRelationFilter.getName() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                    root.get(Friendship_.nameTarget), Utils.toSQLReg(queryRelationFilter.getName())
            ));
        }
        if(queryRelationFilter.getStartDate() != null && queryRelationFilter.getEndDate() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(
                    root.get(Friendship_.dateStart),
                    queryRelationFilter.getStartDate(),
                    queryRelationFilter.getEndDate()
                    )
            );
        }
        return predicate;
    }
}
