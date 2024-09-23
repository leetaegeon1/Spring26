package com.sparta.spring26.domain.menu.controller;

import com.sparta.spring26.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.spring26.domain.menu.dto.GetMenuResponseDto;
import com.sparta.spring26.domain.menu.dto.UpdateMenuRequestDto;
import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.entity.MenuStatus;
import com.sparta.spring26.domain.menu.service.MenuService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery/restaurants/{restaurantId}/menus")
public class MenuController {
    private final MenuService menuService;

    /**
     * 메뉴 등록
     */
    @PostMapping
//    public ApiResponse<Void> createMenu(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable long restaurantId, @Valid @RequestBody CreateMenuRequestDto request){
//        User user = userDetails.getUser();
    public ResponseEntity<ApiResponse<?>> createMenu(@PathVariable Long restaurantId, @Valid @RequestBody CreateMenuRequestDto request){
        User user = new User();

        menuService.createMenu(user, restaurantId, request.getName(), request.getCategory(), request.getPrice());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successWithNoContent());
    }

    /**
     * 메뉴 수정
     */
    @PatchMapping("/{id}")
//    public ApiResponse<Void> updateMenu(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody UpdateMenuRequestDto request){
//        User user = userDetails.getUser();
    public ResponseEntity<ApiResponse<?>> updateMenu(@PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody UpdateMenuRequestDto request){
        User user = new User();

        menuService.updateMenu(user, restaurantId, id, request.getName(), request.getCategory(), request.getPrice(), request.getPopularity(), MenuStatus.of(request.getStatus().toUpperCase()));

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }

    /**
     * 메뉴 다건 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getmenus(@PathVariable Long restaurantId){
        List<Menu> menuList = menuService.getMenus(restaurantId);

        List<GetMenuResponseDto> menuResponseDtoList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuResponseDtoList.add(new GetMenuResponseDto(menu));
        }

        return ResponseEntity.ok(ApiResponse.success(menuResponseDtoList));
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteMenu(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long restaurantId, @PathVariable Long id){
//        User user = userDetails.getUser();
    public ResponseEntity<ApiResponse<?>> deleteMenu(@PathVariable Long restaurantId, @PathVariable Long id){
        User user = new User();

        menuService.deleteMenu(user, restaurantId, id);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
