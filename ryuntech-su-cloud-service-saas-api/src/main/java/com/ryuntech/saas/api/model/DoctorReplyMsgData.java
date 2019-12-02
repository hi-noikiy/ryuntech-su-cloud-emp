package com.ryuntech.saas.api.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DoctorReplyMsgData {
    private KeyNote keyword1;

    private KeyNote keyword2;

    private KeyNote keyword3;

    private KeyNote keyword4;

    private KeyNote keyword5;
}
