package hello.concertreservation.management.Session;

import hello.concertreservation.entity.user.UserEntity;
import jakarta.servlet.http.Cookie;

import java.util.Optional;

public interface SessionManager {

    UserEntity findUser(Cookie cookie);
}
