package com.hits.notification.service.impl;

import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.dto.user.PaginationQueryDto;
import com.hits.common.exception.BadRequestException;
import com.hits.common.exception.NotFoundException;
import com.hits.common.exception.NotImplementedException;
import com.hits.notification.model.Notification_;
import com.hits.common.service.JwtProvider;
import com.hits.common.service.Utils;
import com.hits.notification.dto.NotificationsDto;
import com.hits.notification.dto.NotificationsQueryDto;
import com.hits.notification.dto.ReadDto;
import com.hits.notification.mapper.NotificationMapper;
import com.hits.notification.model.Notification;
import com.hits.notification.model.NotificationNotReadDto;
import com.hits.notification.model.NotificationSpecification;
import com.hits.notification.model.NotificationStatus;
import com.hits.notification.repository.NotificationRepository;
import com.hits.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final JwtProvider jwtProvider;

    @Override
    public void saveNotification(CreateNotificationDto createNotificationDto) {
        Notification notification = notificationMapper.map(createNotificationDto);
        notificationRepository.save(notification);
    }

    @Override
    public NotificationsDto getNotifications(NotificationsQueryDto notificationsQueryDto) {
        if(notificationsQueryDto.getPaginationQueryDto() == null)
        {
            PaginationQueryDto paginationQueryDto = new PaginationQueryDto();
            paginationQueryDto.setPageNumber(1);
            paginationQueryDto.setSize(10);
            notificationsQueryDto.setPaginationQueryDto(paginationQueryDto);
        }
        if((notificationsQueryDto.getNotificationFilterDto().getStart() != null) != (notificationsQueryDto.getNotificationFilterDto().getEnd() != null)) {
            throw new BadRequestException("time error");
        }
        List<Sort.Order> orders = List.of(
                new Sort.Order(Sort.Direction.ASC, Notification_.createdDate.getName())
        );

        Pageable pageable = Utils.toPageable(notificationsQueryDto.getPaginationQueryDto(), orders);
        Page<Notification> page = notificationRepository.findAll(new NotificationSpecification(
                notificationsQueryDto.getNotificationFilterDto(),
                jwtProvider.getId()), pageable);
        NotificationsDto dto = new NotificationsDto();
        dto.setPaginationDto(Utils.toPagination(page));
        dto.setNotifications(
                page.stream().map(notificationMapper::map).collect(Collectors.toList())
        );
        return dto;
    }

    @Override
    public Long notRead() {
        return notificationRepository.count(new NotificationNotReadDto(jwtProvider.getId()));
    }

    @Override
    @Transactional
    public Long read(ReadDto readDto) {
        List<Notification> notifications = new ArrayList<>();
        for(UUID id : readDto.getIds()) {
            Notification notification = notificationRepository.findById(id).orElseThrow(NotFoundException::new);
            notification.setNotificationStatus(readDto.getStatus());
            if(readDto.getStatus() == NotificationStatus.READ) {
                notification.setReadDate(new Date(System.currentTimeMillis()));
            }
            notifications.add(notification);
        }
        log.info("Notification status updated:" + notifications.toString());
        notificationRepository.saveAll(notifications);

        return notRead();
    }
}
