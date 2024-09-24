package hello.concertreservation.dto.request;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import hello.concertreservation.dto.response.PostLoginResponseDto;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class PostLoginRequestDto {

    private String username;
    private String password;

}
