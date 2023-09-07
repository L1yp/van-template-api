package org.cloud.web.model.DO.system.cache;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVerifyCodeDO {

    private String mail;

    private String verifyCode;

    private String ownerUserId;

}
