package com.clone.ohouse.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Toss payment와 연동 테스트를 위해 생성
 * 배포 때는 사용하지 않을 예정
 */
@RequestMapping("/payment/api/v1/test/request")
@Controller
public class PaymentTestController {
    @GetMapping
    public String accessTest(){
        return "testpayment/send_request";
    }
}
