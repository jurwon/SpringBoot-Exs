package com.example.ch6test.controller;

import com.example.ch6test.dto.ItemSearchDto;
import com.example.ch6test.dto.MainItemDto;
import com.example.ch6test.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        //검색 조건과 페이징을 같이 처리해서 데이터를 가지고 온 목록
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        //뷰에 전달
        model.addAttribute("items", items); // 검색 결과 item
        model.addAttribute("itemSearchDto", itemSearchDto); //검색 조건
        model.addAttribute("maxPage", 5);

        return "main";
    }

}