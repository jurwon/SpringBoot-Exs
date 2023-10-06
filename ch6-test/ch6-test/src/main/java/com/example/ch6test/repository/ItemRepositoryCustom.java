package com.example.ch6test.repository;

import com.example.ch6test.dto.ItemSearchDto;
import com.example.ch6test.dto.MainItemDto;
import com.example.ch6test.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}