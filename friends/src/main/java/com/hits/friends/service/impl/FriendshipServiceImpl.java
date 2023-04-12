package com.hits.friends.service.impl;

import com.hits.common.service.JwtProvider;
import com.hits.friends.dto.AddRelationDto;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.mapper.FriendshipMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Friendship;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.repository.FriendsRepository;
import com.hits.friends.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendsRepository friendsRepository;

    private final FriendshipMapper friendshipMapper;

    private final CommonMapper commonMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void addFriend(AddRelationDto addRelationDto) {
        UUID targetId = addRelationDto.getTargetId();
        UUID mainId = jwtProvider.getId();

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
}
