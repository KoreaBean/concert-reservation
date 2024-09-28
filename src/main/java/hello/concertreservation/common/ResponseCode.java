package hello.concertreservation.common;

public interface ResponseCode {

    // http 200
    String SUCCESS = "SU";

    // http 400
    String DUPLICATED_SEAT = "DS";
    String DUPLICATED_PHONENUMBER = "DP";
    String DUPLICATED_USERNAME = "DU";
    String NOT_EXISTED_USER = "NU";
    String NOT_EXISTED_USER_PASSWORD = "NUP";
    String NOT_EXISTED_COOKIE = "NC";
    String NOT_FOUNT = "NF";
    String NOT_EXISTED_TICKET = "NT";
    // http 500
    String DATABASE_ERROR = "DBE";


}
