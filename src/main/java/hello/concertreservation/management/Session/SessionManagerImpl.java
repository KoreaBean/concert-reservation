package hello.concertreservation.management.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.concertreservation.entity.user.UserEntity;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class SessionManagerImpl implements SessionManager{

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public UserEntity findUser(Cookie cookie){
        String cookieValue = cookie.getValue();
        String redisSessionKey = "session::" + cookieValue;
        Object targetUser = redisTemplate.opsForValue().get(redisSessionKey);
        if (targetUser == null){
            return  null;
        }
        UserEntity userEntity = objectMapper.convertValue(targetUser, UserEntity.class);

        return userEntity;
    }


}
