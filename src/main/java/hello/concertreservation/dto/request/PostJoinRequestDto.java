package hello.concertreservation.dto.request;

import hello.concertreservation.common.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostJoinRequestDto {

    private String username;
    private String password;

    private String name;
    private Integer age;
    private Sex sex;
    private String phoneNumber;

}
