package hello.concertreservation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";


    // 컨트롤러 호출 전
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestUrl = request.getRequestURI().toString();
        String uuid = UUID.randomUUID().toString();

        String requestPassword = request.getParameter("password");
        log.info("requestPassword = {}",requestPassword);

        log.info("REQUEST {} {} {}",uuid,requestUrl,handler);
        request.setAttribute(LOG_ID,uuid);

        return true;
    }
    // 컨트롤러 호출 후
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러가 커리한 결과 -> modelAndView
        log.info("postHandler {}",modelAndView);

    }

    // view 랜더링 완료 후
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("Response {} {} {}",logId, requestURI, handler);

        if (ex != null){
            log.info("afterCompletion Error {}", ex);
        }


    }
}
