package edu.kit.ipd.sdq.mediastore.basic.exceptions;

public class PropertiesException extends AppException {

    private static final long serialVersionUID = 8722508008622524418L;

    public PropertiesException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PropertiesException(final String message) {
        super(message);
    }
}
