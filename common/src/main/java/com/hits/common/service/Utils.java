package com.hits.common.service;

import com.hits.common.dto.user.PaginationDto;
import com.hits.common.dto.user.PaginationQueryDto;
import com.hits.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class Utils {
    public static String toSQLReg(String str) {
        return "%" + str + "%";
    }

    public static Pageable toPageable(PaginationQueryDto paginationQueryDto, List<Sort.Order> orders) {
        int page = paginationQueryDto.getPageNumber() - 1;
        int size = paginationQueryDto.getSize();
        if(page < 0) {
            throw new NotFoundException("index <= 0");
        }
        if(size <= 0) {
            throw new NotFoundException("size <= 0");
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

    public static PaginationDto toPagination(Page page) {
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setMaxPage(page.getTotalPages());
        paginationDto.setSize(page.getSize());
        paginationDto.setPageNumber(page.getNumber());
        return paginationDto;
    }
}
