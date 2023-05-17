package com.hits.friends.service.impl;

import com.hits.common.dto.user.FullNameDto;
import com.hits.common.dto.user.PaginationDto;
import com.hits.common.exception.AlreadyExistException;
import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.common.exception.NotFoundException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.JwtProvider;
import com.hits.common.service.Utils;
import com.hits.friends.dto.*;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.mapper.FriendshipMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import com.hits.friends.model.FriendshipSpecification;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.repository.FriendsRepository;
import com.hits.friends.service.CommonService;
import com.hits.friends.service.FriendshipService;
import com.hits.friends.service.NotificationRabbitProducer;
import com.hits.friends.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendsRepository friendsRepository;

    private final FriendshipMapper friendshipMapper;
    private final NotificationRabbitProducer notificationRabbitProducer;
    private final UserService userService;
    private final ApiKeyProvider apiKeyProvider;
    private final CommonMapper commonMapper;
    private final JwtProvider jwtProvider;

    private final CommonService commonService;
    @Override
    @Transactional
    public void addFriend(AddRelationDto addRelationDto) {
        UUID targetId = addRelationDto.getTargetId();
        UUID mainId = jwtProvider.getId();

        commonService.checkUsers(mainId, targetId);

        addFriendship(mainId, targetId, addRelationDto.getFullName());
        FullNameDto fullNameDto = userService.getUserName(mainId, apiKeyProvider.getKey());
        addFriendship(targetId, mainId, fullNameDto);

        notificationRabbitProducer.sendNewUserFriendNotify(targetId, fullNameDto);
    }

    @Override
    public FullRelationDto deleteFriend(UUID targetId) {
        UUID mainId = jwtProvider.getId();
        Friendship friendship = friendsRepository.getByMainUserAndTargetUser(mainId, targetId);
        if(friendship == null)
        {
            throw new NotFoundException("friendship not found");
        }
        friendship.setDateEnd(new Date(System.currentTimeMillis()));
        friendship = friendsRepository.save(friendship);

        Friendship friendship2 = friendsRepository.getByMainUserAndTargetUser(targetId, mainId);
        if(friendship2 == null)
        {
            throw new NotFoundException("friendship not found");
        }
        friendship2.setDateEnd(new Date(System.currentTimeMillis()));
        friendship2 = friendsRepository.save(friendship2);

        FullNameDto fullNameDto = userService.getUserName(mainId, apiKeyProvider.getKey());
        notificationRabbitProducer.sendNewUserFriendNotify(targetId, fullNameDto);
        return friendshipMapper.mapToFull(friendship);
    }

    @Override
    public RelationsDto getFriend(QueryRelationDto queryRelationDto) {
        Pageable pageable = Utils.toPageable(queryRelationDto.getPaginationQueryDto(), commonService.genOrder(queryRelationDto.getQueryRelationSort()));

        Page<Friendship> friendships = friendsRepository.findAll(new FriendshipSpecification(queryRelationDto.getQueryRelationFilter()), pageable);

        RelationsDto relationsDto = new RelationsDto();

        relationsDto.setRelations(friendships.stream().map(friendshipMapper::map).collect(Collectors.toList()));
        relationsDto.setPaginationDto(Utils.toPagination(friendships));
        return relationsDto;
    }

    @Override
    public FullRelationDto getFriend(UUID targetId) {
        UUID mainId = jwtProvider.getId();
        Friendship friendship = friendsRepository.getByMainUserAndTargetUser(mainId, targetId);
        if(friendship == null)
        {
            throw new NotFoundException("friendship not found");
        }
        return friendshipMapper.mapToFull(friendship);
    }

    private void addFriendship(UUID mainId, UUID targetId, FullNameDto fullNameDto) {
        Friendship friendship = friendsRepository.getByMainUserAndTargetUser(mainId, targetId);
        if(friendship == null)
        {
            friendship = new Friendship();
            friendship.setMainUser(mainId);
            friendship.setTargetUser(targetId);
        }
        else
        {
            if(friendship.getDateEnd() == null) {
                throw new AlreadyExistException("already in friend");
            }
            friendship.setDateEnd(null);
        }
        commonMapper.map(friendship, fullNameDto);
        friendsRepository.save(friendship);
    }
}
