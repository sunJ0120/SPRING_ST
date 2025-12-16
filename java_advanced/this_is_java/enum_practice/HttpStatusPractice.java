package this_is_java.enum_practice;

public class HttpStatusPractice {
    public static void main(String[] args) {
        HttpStatus status = HttpStatus.OK;

        switch (status){
            case OK -> handleSuccess();
            case NOT_FOUND -> handleNotFound();
            case CREATED -> handleCreate();
        }
    }

    static void handleSuccess() {
        System.out.println("성공 처리");
    }

    static void handleNotFound() {
        System.out.println("404 처리");
    }

    static void handleCreate() {
        System.out.println("생성 처리");
    }
}
