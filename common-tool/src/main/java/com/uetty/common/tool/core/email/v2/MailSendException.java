package com.uetty.common.tool.core.email.v2;

/**
 * @author : Vince
 * @date: 2019/8/2 11:51
 */
public class MailSendException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MailSendException(Throwable cause) {
        super(cause);
    }

    public MailSendException(String message) {
        super(message);
    }
}
