package com.clone.ohouse.store;

import com.clone.ohouse.account.auth.LoginUser;
import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.OrderService;
import com.clone.ohouse.store.domain.order.dto.DeliveryDto;
import com.clone.ohouse.store.domain.order.dto.OrderRequestDto;
import com.clone.ohouse.store.domain.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/order/api/v1/order")
@RestController
public class OrderApiController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponse order(
            @ModelAttribute OrderRequestDto requestDto,
            @ModelAttribute DeliveryDto deliveryDto
//            @LoginUser SessionUser sessionUser
            ) throws Exception{

        //TODO: Temperary users, 추후 삭제 예정
        SessionUser sessionUser = null;
        if(sessionUser == null){
            sessionUser = new SessionUser(User.builder()
                    .email("jjh@bb.com")
                    .name("jjh")
                    .phone("010-0000-0000")
                    .nickname("j3")
                    .password("0x00")
                    .build());
        }

        return orderService.order(sessionUser, requestDto, deliveryDto);
    }

    @GetMapping("/payment/success")
    public void requestOrderComplete(
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderApprovalCode,
            @RequestParam("amount") Long amount
    ){

    }
}
