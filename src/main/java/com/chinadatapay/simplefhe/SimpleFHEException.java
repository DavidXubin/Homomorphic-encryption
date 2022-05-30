package com.chinadatapay.simplefhe;

public class SimpleFHEException extends Throwable {

    private static final long serialVersionUID = 5205268290744573000L;

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SimpleFHEException(int code) {
        this.code = code;
    }

    public SimpleFHEException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SimpleFHEException(SimpleFHEErrorCode code, String message) {
        super(code.getMessage() + "[" + message + "]");
        this.code = code.getCode();
    }

    public SimpleFHEException(int code, String message, Throwable cause) {
        super(message + "->" + cause.getMessage(), cause);
        this.code = code;
    }

    public SimpleFHEException(SimpleFHEErrorCode returnCode, Throwable cause) {
        super(returnCode.getMessage() + "->" + cause.getMessage(), cause);
        this.code = returnCode.getCode();
    }

    public SimpleFHEException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
