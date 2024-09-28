package hello.concertreservation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.concertreservation.dto.request.PostConcertReservationLimitRequestDto;
import hello.concertreservation.dto.response.PostConcertReservationLimitResponseDto;
import hello.concertreservation.entity.concert.ConcertEntity;
import hello.concertreservation.entity.user.ReservationEntity;
import hello.concertreservation.management.Session.SessionManager;
import hello.concertreservation.management.Session.SessionManagerImpl;
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
import hello.concertreservation.repository.ConcertRepository;
import hello.concertreservation.repository.ReservationRepository;
import hello.concertreservation.repository.UserRepository;
import hello.concertreservation.service.ConcertService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertServiceImpl implements ConcertService {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final SessionManager sessionManager;
    private final CookieManager cookieManager;
    private final RedisManager redisManager;
    private final ReservationRepository reservationRepository;

    // 콘서트 등록
    @Override
    public ResponseEntity<? super PostConcertAddResponseDto> add(PostConcertAddRequestDto dto, HttpServletRequest request) {
        // 0. 콘서트 명, 콘서트 가수, 콘서트 오픈 날짜, 콘서트 마감 날짜, 콘서트 입장객수, 결제 비용,
        // 1. 리퀘스트의 userEntity를 찾아서 만든다음에 거기에 서 concert 정보 추가
        log.info("concert::add 메서드 실행");
        Cookie[] cookies = request.getCookies();
        try {
            log.info("ConcertServiceImpl::add:saveConcertInfo 메서드 실행");
            ConcertEntity concertEntity = saveConcertInfo(dto, cookies);
            log.info("ConcertServiceImpl::add:concertEntityId={}",concertEntity.getId());
            redisManager.createReservationKey(concertEntity.getId());

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PostConcertAddResponseDto.succss();
    }

    // 콘서트 예약 시작 시 좌석 잠금 메서드
    @Override
    public ResponseEntity<? super GetReservationSeatLockResponseDto> reservationSeatLock(Long concertId, Integer seatId, HttpServletRequest request) {
        log.info("ConcertServiceImpl::reservationSeatLock:start");
        UserEntity user;
        try {
            // 쿠키 검증
            Cookie cookie = cookieManager.findCookie(request, CookieConst.LOGIN_COOKIE_NAME);
            if (cookie == null){
                return ResponseDto.NotExistedCookie();
            }
            // 2. 세션에서 쿠키 값 검증
            user = sessionManager.findUser(cookie);
            if (user ==  null){
                return ResponseDto.NotExistedCookie();
            }
            // 좌석 임시 배정 여부
            boolean isSuccess = redisManager.seatLock(concertId, seatId, user.getUserId());
            if (!isSuccess) {
                log.info("ConcertServiceImpl::reservationSeatLock:이미 배정된 좌석 입니다.");
                return GetReservationSeatLockResponseDto.duplicatedSeat("이미 배정된 좌석 입니다.");
            }
        }catch (Exception e){
            log.info("ConcertServiceImpl::reservationSeatLock:databaseError");
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        log.info("ConcertServiceImpl::reservationSeatLock:success");
        // 5. success
        return GetReservationSeatLockResponseDto.success();
    }


    // 콘서트 예약 기능
    @Override
    public ResponseEntity<? super PostConcertReservationResponseDto> reservation(PostConcertReservationRequestDto dto, HttpServletRequest request) {
        log.info("ConcertServiceImpl::reservation:start");
        // 1. 쿠키 값 가져와서 user entity 확보
        Cookie cookie = cookieManager.findCookie(request, CookieConst.LOGIN_COOKIE_NAME);
        // 2. dto 에서 콘서트 id 와 좌석 번호 가져오고
        // 3. redis에 해당 좌석이 예매 되어 있는지 확인 -> 동일한 유저가 예약한 거라면 expire 없애기

        // 4. 예매 되어 있다면 or rock 걸려 있다면 -> error 전송
        // 5. 예매 되어 있지 않다면 -> redis 에 저장 하고 DB에도 저장
        // 6. success


        log.info("ConcertServiceImpl::reservation:success");
        return PostConcertReservationResponseDto.success();
    }


    // 콘서트 선착순 예매 기능
    @Override
    @Transactional
    public ResponseEntity<? super PostConcertReservationLimitResponseDto> reservationLimit(PostConcertReservationLimitRequestDto dto, HttpServletRequest request) {
        log.info("ConcertServiceImpl::reservationLimit:start");
        ConcertEntity concertEntity ;
        UserEntity user;

        try {
            // 0. 사용자 검증
            Cookie cookie = cookieManager.findCookie(request, CookieConst.LOGIN_COOKIE_NAME);
            user = sessionManager.findUser(cookie);
            if (user == null){
                return ResponseDto.NotExistedCookie();
            }
            // 0. concert 검증
            Optional<ConcertEntity> concertId = concertRepository.findById(dto.getConcertId());
            if (concertId.isEmpty()){
                return ResponseDto.NotFound("찾을 수 없는 콘서트 입니다.");
            }
            concertEntity = concertId.get();

            // 남은 티켓 검증
            Long quantity = redisManager.checkQuantity(concertEntity.getId());
            if (quantity == -1){
                return ResponseDto.databaseError();
            } else if (quantity >= concertEntity.getEnterNumber() || quantity + dto.getQuantity() > concertEntity.getEnterNumber()) {
                return PostConcertReservationLimitResponseDto.NotExistedTicket();
            }

            // 1. 레디스로 예매 요청 전송
            // 2. 레디스 incr 증가
            Long count = redisManager.reservationLimit(concertEntity.getId(), dto.getQuantity());
            // 3. 수량 검증
            if (count > concertEntity.getEnterNumber()){
                return PostConcertReservationLimitResponseDto.NotExistedTicket();
            }
            // 성공

            log.info("ConcertServiceImpl::reservationLimit: concertEntity={} user={} dto={}", concertEntity.toString(), user.toString(),dto.toString());
            //user.addReservation(new ReservationEntity(concertEntity, user, dto.getQuantity()));
            ReservationEntity reservationEntity = new ReservationEntity(concertEntity, user, dto.getQuantity());
            reservationRepository.save(reservationEntity);



        }catch (Exception e){
            e.printStackTrace();
            log.info("ConcertServiceImpl::reservationLimit:databaseError");
            return ResponseDto.databaseError();
        }
        // 4. success
        log.info("ConcertServiceImpl::reservationLimit:success");
        return PostConcertReservationLimitResponseDto.success();
    }

    // 콘서트 info 저장 메서드
    @Transactional
    public ConcertEntity saveConcertInfo(PostConcertAddRequestDto dto, Cookie[] cookies) {
        log.info("ConcertServiceImpl::saveConcertInfo:start");
        String cookieUUID;
        UserEntity userEntity;
        ConcertEntity concertEntity = new ConcertEntity();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CookieConst.LOGIN_COOKIE_NAME)){
                cookieUUID = cookie.getValue();
                String redisKey = "session::"+cookieUUID;
                log.info("total : {} , {}, {}",cookieUUID, redisKey, redisTemplate.opsForValue().get(redisKey));
                Object target =  redisTemplate.opsForValue().get(redisKey);
                userEntity = objectMapper.convertValue(target, UserEntity.class);
                log.info("concert::add:userEntity ={}",userEntity);
                concertEntity = new ConcertEntity(dto, userEntity);
                concertRepository.save(concertEntity);
            }
        }
        log.info("ConcertServiceImpl::saveConcertInfo:success");
        return concertEntity;
    }

}
