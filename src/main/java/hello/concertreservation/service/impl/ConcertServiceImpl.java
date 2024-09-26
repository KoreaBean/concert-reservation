package hello.concertreservation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.concertreservation.management.Session.SessionManager;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.management.cookie.CookieConst;
import hello.concertreservation.management.cookie.CookieManager;
import hello.concertreservation.dto.request.PostConcertAddRequestDto;
import hello.concertreservation.dto.request.PostConcertReservationRequestDto;
import hello.concertreservation.dto.response.GetReservationSeatLockResponseDto;
import hello.concertreservation.dto.response.PostConcertAddResponseDto;
import hello.concertreservation.dto.response.PostConcertReservationResponseDto;
import hello.concertreservation.entity.user.UserEntity;
import hello.concertreservation.management.redis.RedisManager;
import hello.concertreservation.repository.UserRepository;
import hello.concertreservation.service.ConcertService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertServiceImpl implements ConcertService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final SessionManager sessionManager;
    private final RedisManager redisManager;
    private final CookieManager cookieManager;

    // 콘서트 등록
    @Override
    public ResponseEntity<? super PostConcertAddResponseDto> add(PostConcertAddRequestDto dto, HttpServletRequest request) {
        // 0. 콘서트 명, 콘서트 가수, 콘서트 오픈 날짜, 콘서트 마감 날짜, 콘서트 입장객수, 결제 비용,
        // 1. 리퀘스트의 userEntity를 찾아서 만든다음에 거기에 서 concert 정보 추가
        log.info("concert::add 메서드 실행");
        Cookie[] cookies = request.getCookies();
        try {
            log.info("ConcertServiceImpl::add:saveConcertInfo 메서드 실행");
            saveConcertInfo(dto, cookies);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PostConcertAddResponseDto.succss();
    }

    // 콘서트 예약 시작 시 좌석 잠금 메서드
    @Override
    public ResponseEntity<? super GetReservationSeatLockResponseDto> reservationSeatLock(Integer concertId, Integer seatId, HttpServletRequest request) {
        log.info("ConcertServiceImpl::reservationSeatLock:reservationSeatLock Method start");
        // 쿠키 검증
        Cookie cookie = cookieManager.findCookie(request, CookieConst.LOGIN_COOKIE_NAME);
        if (cookie == null){
            return ResponseDto.NotExistedCookie();
        }
        // 2. 세션에서 쿠키 값 검증
        UserEntity user = sessionManager.findUser(cookie);
        if (user == null){
            return ResponseDto.NotExistedCookie();
        }
        // 좌석 임시 배정 여부
        boolean isSuccess = redisManager.seatLock(concertId, seatId, user.getUserId());
        if (!isSuccess) {
            return GetReservationSeatLockResponseDto.duplicatedSeat("이미 배정된 좌석 입니다.");
        }

        // 5. success
        return GetReservationSeatLockResponseDto.success();
    }


    // 콘서트 예약 기능
    @Override
    public ResponseEntity<? super PostConcertReservationResponseDto> reservation(PostConcertReservationRequestDto dto, HttpServletRequest request) {

        // 1. 쿠키 값 가져와서 user entity 확보
        Cookie cookie = cookieManager.findCookie(request, CookieConst.LOGIN_COOKIE_NAME);
        UserEntity user = sessionManager.findUser(cookie);
        // 2. dto 에서 콘서트 id 와 좌석 번호 가져오고
        // 3. redis에 해당 좌석이 예매 되어 있는지 확인 -> 동일한 유저가 예약한 거라면 expire 없애기

        // 4. 예매 되어 있다면 or rock 걸려 있다면 -> error 전송
        // 5. 예매 되어 있지 않다면 -> redis 에 저장 하고 DB에도 저장
        // 6. success



        return PostConcertReservationResponseDto.success();
    }



    // 콘서트 info 저장 메서드
    private void saveConcertInfo(PostConcertAddRequestDto dto, Cookie[] cookies) {
        String cookieUUID;
        UserEntity userEntity;
        log.info("foreach 실행");
        for (Cookie cookie : cookies) {
            log.info("if 실행");
            if (cookie.getName().equals(CookieConst.LOGIN_COOKIE_NAME)){
                cookieUUID = cookie.getValue();
                String redisKey = "session::"+cookieUUID;
                log.info("total : {} , {}, {}",cookieUUID, redisKey, redisTemplate.opsForValue().get(redisKey));
                Object target =  redisTemplate.opsForValue().get(redisKey);
                userEntity = objectMapper.convertValue(target, UserEntity.class);
                log.info("concert::add:userEntity ={}",userEntity);
                userEntity.addConcert(dto);
                userRepository.save(userEntity);
            }
        }
    }

}
