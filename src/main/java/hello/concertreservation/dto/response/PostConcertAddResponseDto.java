package hello.concertreservation.dto.response;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PostConcertAddResponseDto extends ResponseDto {

    public PostConcertAddResponseDto() {

        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<? super PostConcertAddResponseDto> succss() {

        PostConcertAddResponseDto result = new PostConcertAddResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
