package com.ryuntech.saas.api.dto;


import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class PermUriDTO {

    private String uri;

    private String resKey;
}
