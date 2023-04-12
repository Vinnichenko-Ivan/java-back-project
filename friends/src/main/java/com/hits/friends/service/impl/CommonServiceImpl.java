package com.hits.friends.service.impl;

import com.hits.common.dto.user.NameSyncDto;
import com.hits.common.service.JwtProvider;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.mapper.FriendshipMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.repository.FriendsRepository;
import com.hits.friends.service.CommonService;
import lombok.RequiredArgsConstructor;
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
}


