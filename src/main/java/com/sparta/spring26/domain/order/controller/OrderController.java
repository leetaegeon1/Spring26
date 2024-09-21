package com.sparta.spring26.domain.order.controller;

import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.repository.OrderRepository;
import com.sparta.spring26.domain.order.service.OrderService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery/order")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;


    // 주문 등록
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails, // 인증된 사용자 정보
            @Valid @RequestBody OrderCreateRequestDto orderCreateRequestDto) {

        // 인증된 사용자 정보 가져오기
        User user = userDetails.getUser();

        // 권한 체크
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("USER"))){
            throw new IllegalArgumentException("주문 등록은 일반 사용자만 가능합니다.");
        }
        // 주문 생성 서비스 호출
        OrderResponseDto responseDto = orderService.createOrder(user, orderCreateRequestDto);

        return ResponseEntity.ok(responseDto);
    }

    // 주문 상태 업데이트
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto statusUpdateRequestDto) {
        User user = userDetails.getUser();
        OrderResponseDto responseDto = orderService.updateOrderStatus(user, orderId, statusUpdateRequestDto.getNewStatus());
        return ResponseEntity.ok(responseDto);
    }

    // 주문 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    // 주문 리스트 조회
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrderList() {
        List<OrderResponseDto> orderList = orderService.getOrderList();
        return ResponseEntity.ok(orderList);
    }

    // 주문 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
