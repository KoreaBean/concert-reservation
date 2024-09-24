package hello.concertreservation.service.impl;

import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.cookie.CookieConst;
import hello.concertreservation.dto.request.PostJoinRequestDto;
import hello.concertreservation.dto.request.PostLoginRequestDto;
import hello.concertreservation.dto.response.PostJoinResponseDto;
import hello.concertreservation.dto.response.PostLoginResponseDto;
import hello.concertreservation.entity.user.UserEntity;
import hello.concertreservation.repository.UserRepository;
import hello.concertreservation.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;


    // 회원가입
    @Override
    public ResponseEntity<? super PostJoinResponseDto> join(PostJoinRequestDto dto) {
        log.info("회원가입 process start");
        UserEntity userEntity = new UserEntity(dto);

        try {
            // 0. 동일한 username이 있을 경우 fail
            boolean isUsernameTrue = userRepository.existsByUsername(dto.getUsername());
            if (isUsernameTrue){
                return PostJoinResponseDto.duplicatedUsername("동일한 닉네임이 존재합니다.");
            }
            // 1. 데이터베이스에 동일한 핸드폰 있을시 error
            boolean isTrue = userRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (isTrue){
                log.info("회원가입 동일한 핸드폰");
                return PostJoinResponseDto.duplicatedPhoneNumber("동일한 핸드폰번호로 가입된 이력이 존재합니다.");
            }
            log.info("회원가입 isTrue = {} , {}",isTrue, dto.toString());
            // save 하기 전에 비밀번호 bcrypt 암호화 해서 넣기
            String encodePassword = passwordEncoder.encode(dto.getPassword());
            log.info("회원가입 encodePasword ={}",encodePassword);
            userEntity.setPassword(encodePassword);
            userRepository.save(userEntity);
        }catch (Exception e){
            // 2. 데이터베이스 error
            log.info("회원가입 process fail");
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        log.info("회원가입 process success");
        return PostJoinResponseDto.success();
    }


    @Override
    public ResponseEntity<? super PostLoginResponseDto> login(PostLoginRequestDto dto, HttpServletResponse response) {

        UserEntity userEntity = new UserEntity();
        // 0. login 메서드 입장하는 조건 -> 쿠키 x or 쿠키 만료

        try {
            // 1. username 조회
            Optional<UserEntity> byUsername = userRepository.findByUsername(dto.getUsername());
            if (!byUsername.isPresent()){
                return PostLoginResponseDto.notExistedUser("일치하는 유저가 없습니다.");
            }
            userEntity = byUsername.get();
            // 2. username 조회해서 얻은 entity 의 password match
            boolean isMatch = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
            if (!isMatch){
                return PostLoginResponseDto.notExistedUsernameOrPassword("닉네임 또는 비밀번호가 틀립니다.");
            }else {
                // 3. 일치하면 UUID 생성
                String uuid = UUID.randomUUID().toString();
                // 4. 쿠키 value 에 값 넣기
                Cookie cookie = new Cookie(CookieConst.LOGIN_COOKIE_NAME,uuid);
                cookie.setMaxAge(60 * 5); // 3분
                cookie.setHttpOnly(true);
                response.addCookie(cookie);

                // 5. 레디스 세션에 key 에 uuid , value 에 userEntity
                String redisKey = "session::" + uuid;
                redisTemplate.opsForValue().set(redisKey,userEntity,5,TimeUnit.MINUTES);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        // 6. success
        return PostLoginResponseDto.success();
    }


}
