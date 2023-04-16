package com.hits.friends.service.impl;

import com.hits.common.dto.user.CheckDto;
import com.hits.common.dto.user.PaginationDto;
import com.hits.common.exception.ExternalServiceErrorException;
import com.hits.common.exception.NotFoundException;
import com.hits.common.service.ApiKeyProvider;
import com.hits.common.service.JwtProvider;
import com.hits.friends.dto.*;
import com.hits.friends.mapper.BlockingMapper;
import com.hits.friends.mapper.CommonMapper;
import com.hits.friends.model.Blocking;
import com.hits.friends.model.Relationship;
import com.hits.friends.repository.BlockingRepository;
import com.hits.friends.service.BlockingService;
import com.hits.friends.service.CommonService;
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
public class BlockingServiceImpl implements BlockingService {

    private final BlockingRepository blockingRepository;

    private final BlockingMapper blockingMapper;
    private final UserService userService;

    private final CommonMapper commonMapper;
    private final JwtProvider jwtProvider;

    private final ApiKeyProvider apiKeyProvider;

    private final CommonService commonService;

    @Override
    public void addBlock(AddRelationDto addRelationDto) {
        UUID targetId = addRelationDto.getTargetId();
        UUID mainId = jwtProvider.getId();

        commonService.checkUsers(mainId, targetId);

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
    public RelationsDto getBlocking(QueryRelationDto queryRelationDto) {
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

        Page<Blocking> blockingPage = blockingRepository.getAllByFilter(
                name,
                surname,
                patronymic,
                mainId,
                pageable
        );

        RelationsDto relationsDto = new RelationsDto();

        relationsDto.setRelations(blockingPage.stream().map(blockingMapper::map).collect(Collectors.toList()));

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setMaxPage(blockingPage.getTotalPages());
        paginationDto.setPageNumber(queryRelationDto.getPaginationQueryDto().getPageNumber());
        paginationDto.setSize(relationsDto.getRelations().size());
        relationsDto.setPaginationDto(paginationDto);
        return relationsDto;
    }

    @Override
    public FullRelationDto getBlocking(UUID targetId) {
        UUID mainId = jwtProvider.getId();
        Blocking blocking = blockingRepository.getByMainUserAndTargetUser(mainId, targetId);
        if(blocking == null)
        {
            throw new NotFoundException("blocking not found");
        }
        return blockingMapper.mapToFull(blocking);
    }

    @Override
    public FullRelationDto deleteBlocking(UUID targetId) {
        UUID mainId = jwtProvider.getId();
        Blocking blocking = blockingRepository.getByMainUserAndTargetUser(mainId, targetId);
        if(blocking == null)
        {
            throw new NotFoundException("blocking not found");
        }
        blocking.setDateEnd(new Date(System.currentTimeMillis()));
        blocking = blockingRepository.save(blocking);
        return blockingMapper.mapToFull(blocking);
    }

    @Override
    public void findBlocking() {

    }

    @Override
    public Boolean checkBlocking(UUID uuid) {
        UUID mainId = jwtProvider.getId();
        return commonService.checkBlocking(new CheckDto(mainId, uuid));
    }
}