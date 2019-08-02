package com.uetty.common.tool.core.email.v2.converter;

import com.uetty.common.tool.core.email.v2.model.MailInfo;

import javax.mail.MessagingException;
import javax.mail.Multipart;

/**
 * @author : Vince
 * @date: 2019/8/2 13:58
 */
public interface ContentConverter {

    void setContent(Multipart multipart, MailInfo mailInfo) throws MessagingException;

}
