package hello.concertreservation.dto.response;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PostConcertReservationLimitResponseDto extends ResponseDto {
    public PostConcertReservationLimitResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<? super PostConcertReservationLimitResponseDto> NotExistedTicket() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_TICKET, ResponseMessage.NOT_EXISTED_TICKET);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);

    }

    public static ResponseEntity<? super PostConcertReservationLimitResponseDto> success() {
        PostConcertReservationLimitResponseDto result = new PostConcertReservationLimitResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
