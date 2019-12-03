package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权限 DTO
 *
 * @author luojbin
 * @version 1.0
 * @date 2019/12/3 19:16
 */
@Data
@Accessors(chain = true)
public class PermDTO {

    /**
     * 权限id
     */
    String permId;
    /**
     * 序号
     */
    Integer sort;
    /**
     * 权限名
     */
    String resName;
    /**
     * 权限标识符
     */
    String resKey;
}