package hello.concertreservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostConcertAddRequestDto {

    private String concertName;
    private String singer;
    private String openAt;
    private String closeAt;
    private Integer enterNumber;
    private Integer price;

}
