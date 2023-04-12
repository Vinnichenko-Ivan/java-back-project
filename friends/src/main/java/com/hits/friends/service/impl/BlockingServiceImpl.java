package com.hits.friends.service.impl;

import com.hits.common.service.JwtProvider;
import com.hits.friends.dto.AddRelationDto;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.mapper.FriendshipMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.repository.FriendsRepository;
import com.hits.friends.service.BlockingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlockingServiceImpl implements BlockingService {

    private final BlockingRepository blockingRepository;

    private final BlockingMapper blockingMapper;

    private final CommonMapper commonMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void addBlock(AddRelationDto addRelationDto) {
        UUID targetId = addRelationDto.getTargetId();
        UUID mainId = jwtProvider.getId();

        Blocking blocking = blockingRepository.getByMainUserAndTargetUser(mainId, targetId);
        if(blocking == null)
        {
            blocking = new Blocking();
            blocking.setMainUser(mainId);
            blocking.setTargetUser(targetId);
        }
        else
        {
            blocking.setDateEnd(null);
        }
        commonMapper.map(blocking, addRelationDto);

        blockingRepository.save(blocking);
    }
}
