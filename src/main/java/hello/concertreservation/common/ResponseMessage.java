package hello.concertreservation.common;

public interface ResponseMessage {

    // state 200
    String SUCCESS = "SUCCESS";


    // state 400
    String NOT_EXISTED_COOKIE = "다시 로그인 해주세요.";
    String NOT_EXISTED_TICKET = "모든 티켓이 소진 되었습니다.";

    // state 500
    String DATABASE_ERROR = "DATABASE_ERROR";



}
