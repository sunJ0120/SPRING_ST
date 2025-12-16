package this_is_java.enum_practice;

public enum HttpStatus {
    OK(200, "성공"),
    CREATED(201, "생성됨"),
    NOT_FOUND(404, "찾을 수 없음");

    public static int code;
    public static String message;

    HttpStatus(int code, String message) {}    // ENUM 변환시 생기는 new 객체를 위해 필드 생성자 필요
}
