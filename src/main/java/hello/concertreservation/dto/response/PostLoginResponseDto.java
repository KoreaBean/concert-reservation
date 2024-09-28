package hello.concertreservation.dto.response;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PostLoginResponseDto extends ResponseDto {


    public PostLoginResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<? super PostLoginResponseDto> success() {
        PostJoinResponseDto result = new PostJoinResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    public static ResponseEntity<? super PostLoginResponseDto> notExistedUser(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<? super PostLoginResponseDto> notExistedUsernameOrPassword(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER_PASSWORD, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
