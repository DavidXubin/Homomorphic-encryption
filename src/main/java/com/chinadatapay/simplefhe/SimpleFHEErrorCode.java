package com.chinadatapay.simplefhe;

public enum SimpleFHEErrorCode {

    SIMPLE_PHE_UNKNOWN_ERROR(50000,"发生未知错误!"),
    SIMPLE_PHE_CONTEXT_MISMATCH_ERROR(50001,"全同态上下文不匹配!"),
    SIMPLE_PHE_CREATE_CONTEXT_ERROR(50002,"全同态上下文创建失败!"),
    SIMPLE_FHE_CREATE_ENCRYPT_KEY_ERROR(50003,"全同态密钥生成错误!"),
    SIMPLE_FHE_KEY_MISMATCH_ERROR(50004,"全同态密钥不匹配!"),
    SIMPLE_FHE_ENCRYPT_ERROR(50005,"全同态加密错误!"),
    SIMPLE_FHE_DIVIDE_BY_ZERO_ERROR(50006,"全同态被零除错误!"),
    SIMPLE_FHE_ADD_SUBTRACT_ERROR(50007,"全同态加减错误!"),
    SIMPLE_FHE_MULTIPLY_DIVIDE_ERROR(50008,"全同态乘除错误!"),
    SIMPLE_FHE_DECRYPT_ERROR(50009,"全同态解密错误!"),
            ;

    private int code;
    private String message;
    SimpleFHEErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
