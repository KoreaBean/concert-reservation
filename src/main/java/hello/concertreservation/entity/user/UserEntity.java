package hello.concertreservation.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hello.concertreservation.common.Sex;
import hello.concertreservation.dto.request.PostConcertAddRequestDto;
import hello.concertreservation.dto.request.PostJoinRequestDto;
import hello.concertreservation.entity.concert.ConcertEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String password;

    private String name;
    private Integer age;
    private Sex sex;
    private String phoneNumber;
    private String createAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ConcertEntity> concertList;

    public UserEntity(PostJoinRequestDto dto) {
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(now);


        this.username = dto.getUsername();
        this.name = dto.getName();
        this.age = dto.getAge();
        this.sex = dto.getSex();
        this.phoneNumber = dto.getPhoneNumber();;
        this.createAt = date;
    }

    @Transactional
    public void addConcert(PostConcertAddRequestDto dto) {
        ConcertEntity concertEntity = new ConcertEntity(dto,this);
        concertList.add(concertEntity);
    }

}
