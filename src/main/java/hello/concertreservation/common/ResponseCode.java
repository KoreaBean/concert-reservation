package hello.concertreservation.common;

public interface ResponseCode {

    // http 200
    String SUCCESS = "SU";

    // http 400
    String DUPLICATED_SEAT = "DS";
    String DUPLICATED_PHONENUMBER = "DP";
    String DUPLICATED_USERNAME = "DU";
    String NOTEXISTED_USER = "NU";
    String NOTEXISTED_USER_PASSWORD = "NUP";
    String NOTEXISTED_COOKIE = "NC";
    // http 500
    String DATABASE_ERROR = "DBE";


}
