package com.hits.friends.service.impl;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.NameSyncDto;
import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.common.exception.NotFoundException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.JwtProvider;
import com.hits.friends.dto.QueryRelationSort;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.mapper.FriendshipMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import com.hits.friends.model.Relationship_;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.repository.FriendsRepository;
import com.hits.friends.service.CommonService;
import com.hits.friends.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final FriendsRepository friendsRepository;
    private final BlockingRepository blockingRepository;

    private final FriendshipMapper friendshipMapper;

    private final BlockingMapper blockingMapper;
    private final CommonMapper commonMapper;

    private final JwtProvider jwtProvider;

    private final ApiKeyProvider apiKeyProvider;
    private final UserService userService;

    @Override
    public void synchronise(NameSyncDto nameSyncDto) {
        List<Friendship> friendships = friendsRepository.getAllByTargetUser(nameSyncDto.getId());
        List<Friendship> newFriendships = friendships.stream()
                .peek(friendship -> commonMapper.map(friendship, nameSyncDto))
                .collect(Collectors.toList());
        friendsRepository.saveAll(newFriendships);

        List<Blocking> listBlocking = blockingRepository.getAllByTargetUser(nameSyncDto.getId());
        List<Blocking> newBlocking = listBlocking.stream()
                .peek(blocking -> commonMapper.map(blocking, nameSyncDto))
                .collect(Collectors.toList());
        blockingRepository.saveAll(newBlocking);
    }

    @Override
    public void checkUsers(UUID mainId, UUID targetId) {
        Boolean targetUserExist = false;
        Boolean mainUserExist = false;

        try {
            targetUserExist = userService.checkUser(targetId, apiKeyProvider.getKey());
            mainUserExist = userService.checkUser(mainId, apiKeyProvider.getKey());
        } catch (Exception e) {
            throw new ExternalServiceErrorException("user service error");
        }

        if(!targetUserExist){
            throw new NotFoundException("target user not found");
        }

        if(!mainUserExist){
            throw new NotFoundException("main user not found");
        }
    }

    @Override
    public Boolean checkBlocking(CheckDto checkDto) {
        return blockingRepository.existsByMainUserAndTargetUser(checkDto.getMainId(), checkDto.getTargetId());
    }

    @Override
    public List<Sort.Order> genOrder(QueryRelationSort queryRelationSort) {
        return List.of(
                new Sort.Order(queryRelationSort.getDateSD(), Relationship_.dateStart.getName()),
                new Sort.Order(queryRelationSort.getNameSD(), Relationship_.nameTarget.getName()),
                new Sort.Order(queryRelationSort.getPatronymicSD(), Relationship_.patronymicTarget.getName()),
                new Sort.Order(queryRelationSort.getSurnameSD(), Relationship_.surnameTarget.getName())
        );
    }
}


