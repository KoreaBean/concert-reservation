package hello.concertreservation.dto.response;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetReservationSeatLockResponseDto extends ResponseDto{
    public GetReservationSeatLockResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<? super GetReservationSeatLockResponseDto> success() {
        GetReservationSeatLockResponseDto result = new GetReservationSeatLockResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<? super GetReservationSeatLockResponseDto> duplicatedSeat(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATED_SEAT, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);

    }
}
