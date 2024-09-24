package hello.concertreservation.entity.user;

import hello.concertreservation.common.Sex;
import hello.concertreservation.dto.request.PostJoinRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

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
}
