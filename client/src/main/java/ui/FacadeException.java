package ui;

public class FacadeException extends Exception {
    String message;

    FacadeException(String message) {
        this.message = message;
    }
}
