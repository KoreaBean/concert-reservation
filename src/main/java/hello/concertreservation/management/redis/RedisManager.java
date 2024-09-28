package hello.concertreservation.management.redis;

public interface RedisManager {

    boolean seatLock(Long concertId, Integer seatId, Long userId);

    boolean reservation(Long concertId, Integer seatId, Long userId);
    // 선착순 티켓 저장
    Long reservationLimit(Long concertId, Integer quantity);
    // 선착순 남은 티켓 확인
    Long checkQuantity(Long concertId);
    // 콘서트 선착순 예매 key 생성
    void createReservationKey(Long concertId);
    // 중복 로그인 이전 session 삭제
    void deleteSessionKey(String uuid);
}
