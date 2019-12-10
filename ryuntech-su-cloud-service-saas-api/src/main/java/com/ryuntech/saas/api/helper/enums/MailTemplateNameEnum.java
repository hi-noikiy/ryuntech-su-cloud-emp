package com.ryuntech.saas.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author wanh
 * @since 2019-12-06
 */
@Getter
@AllArgsConstructor
public enum MailTemplateNameEnum {

    PlanExpiresEmail("plan_expires_email.ftl", "应收计划到期提醒"),
    PlanOverdueEmail("overdue_followUp_email.ftl", "应收计划到期提醒");

    String code;

    String desc;

}
