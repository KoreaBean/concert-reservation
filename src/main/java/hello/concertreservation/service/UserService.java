package hello.concertreservation.service;

import hello.concertreservation.dto.request.PostJoinRequestDto;
import hello.concertreservation.dto.request.PostLoginRequestDto;
import hello.concertreservation.dto.response.PostJoinResponseDto;
import hello.concertreservation.dto.response.PostLoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<? super PostJoinResponseDto> join(PostJoinRequestDto postJoinRequestDto);


    ResponseEntity<? super PostLoginResponseDto> login(PostLoginRequestDto dto, HttpServletResponse response);
}
