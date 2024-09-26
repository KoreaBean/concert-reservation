package hello.concertreservation.common;

import hello.concertreservation.dto.response.GetReservationSeatLockResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class ResponseDto {

    private String code;
    private String message;

    public static ResponseEntity<ResponseDto> databaseError(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> NotExistedCookie() {
        ResponseDto result = new ResponseDto(ResponseCode.NOTEXISTED_COOKIE, ResponseMessage.NOTEXISTED_COOKIE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }


}
