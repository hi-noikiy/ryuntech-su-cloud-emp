package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginConpanyDTO {
    /**
     * 公司Id
     */
    private String companyId;

    /**
     * 所属部门名称
     */
    private String name;
}
