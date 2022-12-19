package com.clone.ohouse.store;

import com.clone.ohouse.account.auth.LoginUser;
import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.OrderService;
import com.clone.ohouse.store.domain.PaymentService;
import com.clone.ohouse.store.domain.order.dto.DeliveryDto;
import com.clone.ohouse.store.domain.order.dto.OrderRequestDto;
import com.clone.ohouse.store.domain.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/order/api/v1/payment")
@RestController
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;

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

        return orderService.orderStart(sessionUser, requestDto, deliveryDto);
    }

    @GetMapping("/card/success")
    public HttpEntity requestOrderComplete(
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderApprovalCode,
            @RequestParam("amount") Long amount
    ){

    }
}
