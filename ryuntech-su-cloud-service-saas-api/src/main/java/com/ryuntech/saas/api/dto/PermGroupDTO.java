package com.ryuntech.saas.api.dto;


import com.ryuntech.saas.api.model.SysPerm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 权限组DTO
 */
@Data
@Accessors(chain = true)
public class PermGroupDTO {

    private String groupSort;
    private String name;
    private List<SysPerm> sysPermList ;

}
