package hello.concertreservation.repository;

import hello.concertreservation.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    // 핸드폰 조회
    boolean existsByPhoneNumber(String phoneNumber);
    // username 조회
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
}
