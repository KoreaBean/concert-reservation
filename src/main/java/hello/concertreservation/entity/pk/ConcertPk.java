package hello.concertreservation.entity.pk;

import jakarta.persistence.IdClass;

import java.io.Serializable;
import java.util.Objects;

public class ConcertPk implements Serializable {
    private String concertName;
    private String openAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcertPk concertPk = (ConcertPk) o;
        return Objects.equals(concertName, concertPk.concertName) && Objects.equals(openAt, concertPk.openAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concertName, openAt);
    }
}
