package hello.concertreservation.dto.response;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PostJoinResponseDto extends ResponseDto {


    public PostJoinResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    
    public static ResponseEntity<ResponseDto> duplicatedPhoneNumber(String message){
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATED_PHONENUMBER, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }


    public static ResponseEntity<? super PostJoinResponseDto> success() {
        PostJoinResponseDto result = new PostJoinResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<? super PostJoinResponseDto> duplicatedUsername(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATED_USERNAME, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);


    }
}
