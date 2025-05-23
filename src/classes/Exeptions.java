package classes;

public class Exeptions {

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class NotAcceptableExeption extends RuntimeException {
        public NotAcceptableExeption(String message) {
            super(message);
        }
    }
}
