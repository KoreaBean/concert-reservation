package hello.concertreservation.repository;

import hello.concertreservation.entity.concert.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<ConcertEntity,Long> {
}
