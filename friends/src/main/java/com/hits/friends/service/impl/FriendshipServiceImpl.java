package com.hits.friends.service.impl;

import com.hits.common.dto.user.PaginationDto;
import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.common.exception.NotFoundException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.JwtProvider;
import com.hits.friends.dto.*;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.mapper.FriendshipMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.repository.FriendsRepository;
import com.hits.friends.service.CommonService;
import com.hits.friends.service.FriendshipService;
import com.hits.friends.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendsRepository friendsRepository;

    private final FriendshipMapper friendshipMapper;

    private final UserService userService;
    private final ApiKeyProvider apiKeyProvider;
    private final CommonMapper commonMapper;
    private final JwtProvider jwtProvider;

    private final CommonService commonService;
    @Override
    public void addFriend(AddRelationDto addRelationDto) {
        UUID targetId = addRelationDto.getTargetId();
        UUID mainId = jwtProvider.getId();

        commonService.checkUsers(mainId, targetId);

        Friendship friendship = friendsRepository.getByMainUserAndTargetUser(mainId, targetId);
        if(friendship == null)
        {
            friendship = new Friendship();
            friendship.setMainUser(mainId);
            friendship.setTargetUser(targetId);
        }
        else
        {
            friendship.setDateEnd(null);
        }
        commonMapper.map(friendship, addRelationDto);

        friendsRepository.save(friendship);
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
        return friendshipMapper.mapToFull(friendship);
    }

    @Override
    public RelationsDto getFriend(QueryRelationDto queryRelationDto) {
        UUID mainId = jwtProvider.getId();

        int page = queryRelationDto.getPaginationQueryDto().getPageNumber() - 1;
        int size = queryRelationDto.getPaginationQueryDto().getSize();

        if(page < 0) {
            throw new NotFoundException("index <= 0");
        }
        if(size <= 0) {
            throw new NotFoundException("size <= 0");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(commonService.genOrder(queryRelationDto.getQueryRelationSort())));

        String name = queryRelationDto.getQueryRelationFilter().getName().toLowerCase();
        String surname = queryRelationDto.getQueryRelationFilter().getSurname().toLowerCase();
        String patronymic = queryRelationDto.getQueryRelationFilter().getPatronymic().toLowerCase();

        Page<Friendship> friendships = friendsRepository.getAllByFilter(
                name,
                surname,
                patronymic,
                mainId,
                pageable
        );

        RelationsDto relationsDto = new RelationsDto();

        relationsDto.setRelations(friendships.stream().map(friendshipMapper::map).collect(Collectors.toList()));

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setMaxPage(friendships.getTotalPages());
        paginationDto.setPageNumber(queryRelationDto.getPaginationQueryDto().getPageNumber());
        paginationDto.setSize(relationsDto.getRelations().size());
        relationsDto.setPaginationDto(paginationDto);
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
}