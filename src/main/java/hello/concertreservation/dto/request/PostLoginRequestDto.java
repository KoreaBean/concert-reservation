package hello.concertreservation.dto.request;

import hello.concertreservation.common.ResponseCode;
import hello.concertreservation.common.ResponseDto;
import hello.concertreservation.common.ResponseMessage;
import hello.concertreservation.dto.response.PostLoginResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLoginRequestDto {

    private String username;
    private String password;

}
