package com.ryuntech.saas.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApiGetJudicialSaleList {
    private ApiHeader apiHeader;
    private List<Result> results;
    private List<GroupItems> GroupItems;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class GroupItems{
        private String Key;
        private List<Items> Items;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class Items{
            private String Value;
            private String Desc;
            private String Count;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        /**
         * 主键
         */
        private String Id;
        /**
         * 标题
         */
        private String Name;
        /**
         * 委托法院
         */
        private String Executegov;
        /**
         * 拍卖时间
         */
        private String ActionRemark;
        /**
         * 起拍价
         */
        private String YiWu;
    }
}
