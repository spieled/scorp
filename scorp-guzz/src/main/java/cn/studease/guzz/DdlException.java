package cn.studease.guzz;

public class DdlException extends RuntimeException {
    public DdlException(String message) {
        super(message);
    }

    public DdlException(Throwable cause) {
        super(cause);
    }

    public DdlException(String message, Throwable cause) {
        super(message, cause);
    }
}


