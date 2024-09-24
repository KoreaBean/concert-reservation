package hello.concertreservation.dto.request;

import hello.concertreservation.common.Sex;
import lombok.Data;

@Data
public class PostJoinRequestDto {

    private String username;
    private String password;

    private String name;
    private Integer age;
    private Sex sex;
    private String phoneNumber;

}
