package com.hits.notification.model;


import com.hits.common.enums.NotificationType;
import com.hits.common.service.Utils;
import com.hits.notification.dto.NotificationFilterDto;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;
import java.util.UUID;

public class NotificationSpecification implements Specification<Notification> {

    private final NotificationFilterDto notificationFilterDto;

    private final UUID userId;

    public NotificationSpecification(NotificationFilterDto notificationFilterDto, UUID userId) {
        this.notificationFilterDto = notificationFilterDto;
        this.userId = userId;
    }

    private Predicate loadFromSet(Set<NotificationType> notificationTypes, Root<Notification> root, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.or();
        for(NotificationType type : notificationTypes) {
            predicate = criteriaBuilder.or(
                    predicate,
                    criteriaBuilder.equal(root.get(Notification_.notificationType), type)
            );
        }
        return predicate;
    }

    @Override
    public Predicate toPredicate(Root<Notification> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(notificationFilterDto.getStart() == null) {
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get(Notification_.userId), userId),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(Notification_.text)), Utils.toSQLReg(notificationFilterDto.getFilter())),
                    loadFromSet(notificationFilterDto.getFilterType(), root, criteriaBuilder)
            );
        }
        return criteriaBuilder.and(
                criteriaBuilder.equal(root.get(Notification_.userId), userId),
                criteriaBuilder.between(root.get(Notification_.readDate), notificationFilterDto.getStart(), notificationFilterDto.getEnd()),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(Notification_.text)), Utils.toSQLReg(notificationFilterDto.getFilter())),
                loadFromSet(notificationFilterDto.getFilterType(), root, criteriaBuilder)
            );
    }
}
