package hello.concertreservation.management.redis;

import hello.concertreservation.dto.response.GetReservationSeatLockResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisManager {
    private final RedisTemplate<String, Object> redisTemplate;

    // 좌석 임시 배정
    public boolean seatLock(Integer concertId, Integer seatId, Long userId){

        // 3. redis에 좌석 미배정인지 검증 -> 미배정이면 배정 해주고 3분간 rock
        String redisSeatKey = "reservation::" + concertId +":" + seatId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(redisSeatKey, userId, 3, TimeUnit.MINUTES));
    }

}
