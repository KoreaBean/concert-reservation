package hello.concertreservation.management.redis;

import hello.concertreservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisManagerImpl implements RedisManager{
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    // 좌석 임시 배정
    @Transactional
    public boolean seatLock(Long concertId, Integer seatId, Long userId){

        // 3. redis에 좌석 미배정인지 검증 -> 미배정이면 배정 해주고 3분간 rock
        String redisSeatKey = "reservation::" + concertId +":" + seatId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(redisSeatKey, userId, 3, TimeUnit.MINUTES));
    }

    // 좌석 예약하기 ( 미완성 )
    @Transactional
    public boolean reservation(Long concertId, Integer seatId, Long userId){
        log.info("RedisManager::reservation:start");
        String redisSeatKey = "reservation::" + concertId + ":" + seatId;
        boolean checkUser = redisTemplate.opsForValue().get(redisSeatKey).equals(userId);
        if (!checkUser){
            return false;
        }
        // ttl 제거
        redisTemplate.persist(redisSeatKey);

        return true;
    }

    // 선착순 좌석 예약하기
    @Transactional
    public Long reservationLimit(Long concertId, Integer quantity){
        String redisReservationLimitKey = "reservationLimit::" + concertId;
        return redisTemplate.opsForValue().increment(redisReservationLimitKey, quantity);
    }

    // 남은 티켓 수량 반환
    @Override
    public Long checkQuantity(Long concertId) {
        String redisReservationLimitKey = "reservationLimit::" + concertId;
        Object target = redisTemplate.opsForValue().get(redisReservationLimitKey);
        if (target == null){
            return -1L;
        }
        return Long.valueOf(target.toString());
    }

    // 콘서트 예매 key 생성
    @Override
    public void createReservationKey(Long concertId) {
        String redisKey = "reservationLimit::" + concertId;
        redisTemplate.opsForValue().set(redisKey, 0L);

    }

    @Override
    public void deleteSessionKey(String uuid) {
        String redisSessionKey = "session::" + uuid;
        redisTemplate.delete(redisSessionKey);
    }


}
