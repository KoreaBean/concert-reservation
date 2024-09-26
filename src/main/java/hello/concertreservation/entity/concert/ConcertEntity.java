package hello.concertreservation.entity.concert;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import hello.concertreservation.dto.request.PostConcertAddRequestDto;
import hello.concertreservation.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "concertInfo")
@Getter
@Setter
@NoArgsConstructor
public class ConcertEntity {
    //   1. 콘서트 명, 콘서트 가수, 콘서트 오픈 날짜, 콘서트 마감 날짜, 콘서트 입장객수, 결제 비용, 등록한 유저

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String concertName;
    private String singer;
    private String openAt;
    private String closeAt;
    private Integer enterNumber;
    private Integer price;
    @ManyToOne
    @JsonBackReference
    private UserEntity user;


    public ConcertEntity(PostConcertAddRequestDto dto, UserEntity user) {
        this.concertName = dto.getConcertName();
        this.singer = dto.getSinger();
        this.openAt = dto.getOpenAt();
        this.closeAt = dto.getCloseAt();
        this.enterNumber = dto.getEnterNumber();
        this.price = dto.getPrice();
        this.user = user;
    }


}
