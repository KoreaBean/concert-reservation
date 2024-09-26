package hello.concertreservation.interceptor;

import hello.concertreservation.management.cookie.CookieConst;
import hello.concertreservation.management.cookie.CookieManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, String> redisTemplate;
    private final CookieManager cookieManager;

    private static final String[] whitePath = {"/", "/login", "/logout","/join"};

    public LoginCheckInterceptor(RedisTemplate<String, String> redisTemplate, CookieManager cookieManager) {
        this.redisTemplate = redisTemplate;
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        if (PatternMatchUtils.simpleMatch(whitePath,requestURI)){
            return true;
        }

        // 쿠키 받고
        Cookie findCookie = cookieManager.findCookie(request, CookieConst.LOGIN_COOKIE_NAME);

        // 쿠키 없으면 false -> 로그인 화면으로 리다이렉트
        // 로그인 화면으로 이동 시키고 로그인 하면 원래 요청 페이지로 이동
        if (findCookie == null){
            log.info("쿠키 없는 사용자");
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        // 받은 쿠키 name 을 레디스에서 조회
        String redisKey = "session::"+ findCookie.getValue();


        // 쿠키 의 uuid 받아서 -> 레디스 key 조회하는데 -> value 를 필요없고 레디스 key값만 맞는지 조회하면 되니까
        if (!checkIfKeyExists(redisKey)){
            log.info("uuid가 일치하지 않습니다.");
            log.info("LoginCheckInterceptor:: cookie:uuid ={} , redis:uuid ={}",findCookie.getValue());
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        log.info("LoginCheckInterceptor::preHandle: true");
        return  true;
    }


    public boolean checkIfKeyExists(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
