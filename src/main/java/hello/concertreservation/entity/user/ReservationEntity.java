package hello.concertreservation.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.concertreservation.entity.concert.ConcertEntity;
import hello.concertreservation.service.impl.ConcertServiceImpl;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity(name = "user_concert")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private ConcertEntity concert;
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private UserEntity user;
    private Integer quantity;

    public ReservationEntity(ConcertEntity concertEntity, UserEntity user, Integer quantity) {
        this.concert = concertEntity;
        this.user = user;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ReservationEntity{" +
                "id=" + id +
                ", concert=" + concert +
                ", user=" + user +
                ", quantity=" + quantity +
                '}';
    }
}
