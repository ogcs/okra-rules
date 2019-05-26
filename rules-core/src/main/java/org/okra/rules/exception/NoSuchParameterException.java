package org.okra.rules.exception;

public class NoSuchParameterException extends RuntimeException {

    private static final long serialVersionUID = -6284005190319121589L;
    private String missingParameter;

    public NoSuchParameterException(String message, String missingParameter) {
        super(message);
        this.missingParameter = missingParameter;
    }

    public String getMissingParameter() {
        return missingParameter;
    }
}
