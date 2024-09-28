package hello.concertreservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostConcertReservationLimitRequestDto {

    private Long concertId;
    private Integer quantity;

    @Override
    public String toString() {
        return "PostConcertReservationLimitRequestDto{" +
                "concertId=" + concertId +
                ", quantity=" + quantity +
                '}';
    }
}
