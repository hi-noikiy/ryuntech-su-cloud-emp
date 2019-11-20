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
public class ApiGetChattelMortgage {
    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        /**
         * 登记编号
         */
        private String RegisterNo;
        /**
         * 登记时间
         */
        private String RegisterDate;
        /**
         * 公示时间
         */
        private String PublicDate;
        /**
         * 登记机关
         */
        private String RegisterOffice;
        /**
         * 被担保债权数额
         */
        private String DebtSecuredAmount;
        /**
         * 状态
         */
        private String Status;
        /**
         * 动产抵押详细信息
         */
        private Detail Detail;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class Detail{
            /**
             * 抵押权人信息
             */
            private List<Pledgee> PledgeeList;
            /**
             * 抵押物信息
             */
            private List<Guarantee> GuaranteeList;
            /**
             * 动产抵押登记变更信息
             */
            private List<Change> ChangeList;
            /**
             * 动产抵押登记信息
             */
            private Pledge Pledge;
            /**
             * 被担保主债权信息
             */
            private SecuredClaim SecuredClaim;
            /**
             * 动产抵押登记注销信息
             */
            private CancelInfo CancelInfo;

            @Data
            @EqualsAndHashCode(callSuper = false)
            @Accessors(chain = true)
            public class Pledge{
                private String RegistNo;
                private String RegistDate;
                private String RegistOffice;
            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @Accessors(chain = true)
            public class Pledgee{
                private String Name;
                private String IdentityType;
                private String IdentityNo;
            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @Accessors(chain = true)
            public class SecuredClaim{
                private String Kind;
                private String Amount;
                private String AssuranceScope;
                private String FulfillObligation;
                private String Remark;
            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @Accessors(chain = true)
            public class Guarantee{
                private String Name;
                private String Ownership;
                private String Other;
                private String Remark;
            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @Accessors(chain = true)
            public class CancelInfo{

            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @Accessors(chain = true)
            public class Change{

            }

        }
    }
}
