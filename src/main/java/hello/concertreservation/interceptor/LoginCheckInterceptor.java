package hello.concertreservation.interceptor;

import hello.concertreservation.cookie.CookieConst;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import static hello.concertreservation.cookie.CookieManager.findCookie;
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String[] whitePath = {"/", "/login", "/logout","/join"};

    public LoginCheckInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        if (PatternMatchUtils.simpleMatch(whitePath,requestURI)){
            return true;
        }

        // 쿠키 받고
        Cookie findCookie = findCookie(request, CookieConst.LOGIN_COOKIE_NAME);

        // 쿠키 없으면 false -> 로그인 화면으로 리다이렉트
        // 로그인 화면으로 이동 시키고 로그인 하면 원래 요청 페이지로 이동
        if (findCookie == null){
            log.info("쿠키 없는 사용자");
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        // 받은 쿠키 name 을 레디스에서 조회
        String uuid = redisTemplate.opsForValue().get(findCookie.getValue());
        if (!findCookie.getValue().equals(uuid)){
            log.info("uuid가 일치하지 않습니다.");
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        return  true;
    }


}
