package hello.concertreservation.management.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface CookieManager {

    // 쿠키 찾기
    Cookie findCookie(HttpServletRequest request, String findCookieName);


}