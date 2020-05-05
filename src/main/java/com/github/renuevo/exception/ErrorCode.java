package com.github.renuevo.exception;

import lombok.Getter;

@Getter
public enum  ErrorCode {

    // Payment Error
    CARD_USE_STATUS_ERROR(500, "P001", "동일한 카드를 현재 사용중입니다"),
    PAYMENT_ERROR(500, "P002", "결제 도중 오류가 발생 하였습니다"),

    //Cancel Error
    PAYMENT_CANCEL_ERROR(500, "C001", "결제 취소 도중 오류가 발생 하였습니다"),

    //View Error
    PAYMENT_VIEW_ERROR(500, "V001", "결제 조회를 할 수 있습니다"),

    //기본 Basic Error
    INVALID_INPUT_VALUE(400, "B001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "B002", " Invalid Input Value"),
    INTERNAL_SERVER_ERROR(500, "B003", "Server Error"),
    INVALID_TYPE_VALUE(400, "B004", " Invalid Type Value");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


}
