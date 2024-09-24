package hello.concertreservation.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public enum Sex {

    Man("Man"), Woman("Woman");

    private String sex;


    Sex(String sex) {
        this.sex = sex;
    }

    @JsonValue
    public String getSex() {
        return sex;
    }
}
