package hello.concertreservation.dto.request;

import lombok.Getter;

@Getter
public class PostConcertReservationRequestDto {

    private Long concertId ;
    private Integer seatId;

}
