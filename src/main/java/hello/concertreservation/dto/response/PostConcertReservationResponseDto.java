package hello.concertreservation.dto.response;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PostConcertReservationResponseDto extends ResponseDto {
    public PostConcertReservationResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<? super PostConcertReservationResponseDto> success() {
        PostConcertReservationResponseDto result = new PostConcertReservationResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
