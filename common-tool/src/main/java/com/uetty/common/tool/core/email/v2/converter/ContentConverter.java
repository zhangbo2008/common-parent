package com.uetty.common.tool.core.email.v2.converter;

import com.uetty.common.tool.core.email.v2.model.MailMessage;

import javax.mail.MessagingException;
import javax.mail.Multipart;

/**
 * @author : Vince
 * @date: 2019/8/2 13:58
 */
public interface ContentConverter {

    void setContent(Multipart multipart, MailMessage mailMessage) throws MessagingException;

}
