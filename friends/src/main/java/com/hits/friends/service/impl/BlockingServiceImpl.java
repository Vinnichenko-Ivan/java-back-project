package com.hits.friends.service.impl;

import com.hits.common.service.JwtProvider;
import com.hits.friends.dto.AddRelationDto;
import com.hits.friends.dto.FullRelationDto;
import com.hits.friends.dto.RelationDto;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.service.BlockingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public List<RelationDto> getBlocking() {
        UUID mainId = jwtProvider.getId();
        return null;
    }

    @Override
    public FullRelationDto getBlocking(UUID uuid) {
        return null;
    }

    @Override
    public FullRelationDto deleteBlocking(UUID uuid) {
        return null;
    }

    @Override
    public void findBlocking() {

    }

    @Override
    public void checkBlocking() {

    }
}
