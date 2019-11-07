package com.ryuntech.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EDZ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseForm implements Serializable {

    private Class aClass;
    private Object t;


    @TableField(exist = false)
    private Map<String,Object> property;

    public BaseForm(){
        property = new HashMap<>();
    }
    public void setPropertys(String key,Object value) {
        property.put(key,value);
    }

}
