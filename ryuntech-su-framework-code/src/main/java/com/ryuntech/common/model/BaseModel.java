package com.ryuntech.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseModel implements Serializable {

    @TableField(exist = false)
    @JsonIgnore
    private Map<String,Object> property;

    public BaseModel(){
        property = new HashMap<>();
    }
    public void setPropertys(String key,Object value) {
        property.put(key,value);
    }


}
