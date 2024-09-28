package hello.concertreservation.service.impl;

import hello.concertreservation.common.Sex;
import hello.concertreservation.dto.request.PostConcertReservationLimitRequestDto;
import hello.concertreservation.dto.request.PostJoinRequestDto;
import hello.concertreservation.dto.request.PostLoginRequestDto;
import hello.concertreservation.dto.response.PostConcertReservationLimitResponseDto;
import hello.concertreservation.dto.response.PostLoginResponseDto;
import hello.concertreservation.entity.user.UserEntity;
import hello.concertreservation.management.cookie.CookieConst;
import hello.concertreservation.service.ConcertService;
import hello.concertreservation.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ConcertServiceImplTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private UserService userService;

    @Test
    public void 티켓선착순예매_한명이구매(){

    }

    @Test
    public void 티켓선착순예매_동시성테스트() throws InterruptedException {
        // given
        int threadCount = 300;
        int maxSeats = 100;
        int seatsPerRequest = 1;
        Long concertId = 1L;
        String redisReservationKey = "reservation::" + concertId;
        String cookieValue = "loginTest";
        String redisSessionKey = "session::" +cookieValue;
        redisTemplate.delete(cookieValue);

        PostJoinRequestDto postJoinRequestDto = new PostJoinRequestDto("test","1234","kim",20,Sex.Man,"010");
        UserEntity userEntity = new UserEntity(postJoinRequestDto);
        PostConcertReservationLimitRequestDto requestDto = new PostConcertReservationLimitRequestDto(concertId, seatsPerRequest);
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie(CookieConst.LOGIN_COOKIE_NAME,cookieValue);
        cookie.setMaxAge(60);
        request.setCookies(cookie);

        redisTemplate.opsForValue().set(redisSessionKey,userEntity);

        //When
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    ResponseEntity<? super PostConcertReservationLimitResponseDto> responseEntity = concertService.reservationLimit(requestDto, request);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();



        //then


    }

}