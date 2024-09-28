package hello.concertreservation.management.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
public class CookieManagerImpl implements CookieManager{


    public Cookie findCookie(HttpServletRequest request, String findCookieName) {

        if (request.getCookies() == null){
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(findCookieName))
                .findAny()
                .orElse(null);
    }



}
