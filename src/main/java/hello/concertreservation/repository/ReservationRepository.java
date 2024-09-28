package hello.concertreservation.repository;

import hello.concertreservation.entity.user.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {
}
