package com.hits.notification.model;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class NotificationNotReadDto implements Specification<Notification> {

    private final UUID userId;

    public NotificationNotReadDto(UUID userId) {
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Notification> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(
                criteriaBuilder.equal(root.get(Notification_.userId), userId),
                criteriaBuilder.equal(root.get(Notification_.notificationType), "NEW_MESSAGE"),
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get(Notification_.notificationStatus),"NOT_SEND"),
                        criteriaBuilder.equal(root.get(Notification_.notificationStatus),"SEND")
                )
        );
    }
}
