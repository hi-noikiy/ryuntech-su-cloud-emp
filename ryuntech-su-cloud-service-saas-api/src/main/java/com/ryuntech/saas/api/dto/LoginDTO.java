package com.ryuntech.saas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
@AllArgsConstructor
public class LoginDTO {
    private static final long serialVersionUID = 1L;

    private List<LoginConpanyDTO> list;

    private String sysUserId;
}
