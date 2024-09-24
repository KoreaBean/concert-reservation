package hello.concertreservation.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieManager {




    public static Cookie findCookie(HttpServletRequest request, String loginCookieName) {

        if (request.getCookies() == null){
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(loginCookieName))
                .findAny()
                .orElse(null);
    }

}
