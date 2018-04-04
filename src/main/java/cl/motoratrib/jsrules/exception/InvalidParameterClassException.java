package cl.motoratrib.jsrules.exception;

/**
 * Created by Marcelo Lomeña 4/30/2015
 */
public class InvalidParameterClassException extends InvalidParameterException {
    public InvalidParameterClassException() {
    }

    public InvalidParameterClassException(String message) {
        super(message);
    }

    public InvalidParameterClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterClassException(Throwable cause) {
        super(cause);
    }

    public InvalidParameterClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
