package hello.concertreservation.controller;

import hello.concertreservation.dto.request.PostJoinRequestDto;
import hello.concertreservation.dto.request.PostLoginRequestDto;
import hello.concertreservation.dto.response.PostJoinResponseDto;
import hello.concertreservation.dto.response.PostLoginResponseDto;
import hello.concertreservation.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<? super PostJoinResponseDto> join(@RequestBody PostJoinRequestDto postJoinRequestDto){

        ResponseEntity<? super PostJoinResponseDto> result = userService.join(postJoinRequestDto);

        return result;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<? super PostLoginResponseDto> login(@RequestBody PostLoginRequestDto dto, HttpServletResponse response){
        ResponseEntity<? super PostLoginResponseDto> result = userService.login(dto, response);
        return result;
    }


}
