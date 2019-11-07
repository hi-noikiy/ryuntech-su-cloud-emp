package com.ryuntech.saas.api.model;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;

/**
 * @author EDZ
 */
@Data
public class AttachmentFile extends BaseModel {


    private String id;
    private String url;

 /*   private File file;


    @Data
    class File{
        private String path;
        private String size;
    }*/

}
