package com.ryuntech.saas.api.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 权限组DTO
 *
 * @author luojbin
 * @version 1.0
 * @date 2019/12/3 19:16
 */
@Data
@Accessors(chain = true)
public class PermGroupDTO {

    /**
     * 分组序号
     */
    private String groupSort;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 权限列表
     */
    private List<PermDTO> permList;

}
