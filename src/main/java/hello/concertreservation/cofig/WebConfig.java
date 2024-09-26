package hello.concertreservation.cofig;

import hello.concertreservation.interceptor.LogInterceptor;
import hello.concertreservation.interceptor.LoginCheckInterceptor;
import hello.concertreservation.management.cookie.CookieManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RedisTemplate<String, String> redisTemplate;
    private final CookieManager cookieManager;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico","/error","/application/**");
        registry.addInterceptor(new LoginCheckInterceptor(redisTemplate, cookieManager))
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/login","/logout","/join","/css/**","/*.ico","/error","/application/**");
    }



}
