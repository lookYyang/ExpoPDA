package com.orangehi.expo.common;


/**
 * <b>全局常量表</b>
 *
 */
public class OHCons {
    /**
     * 系统名称
     */
    public static final String APP_TITLE = "专业观众预登记PDA扫描系统";

    /**
     * 系统标识，对应枚举subsys_id
     */
    public static final String SYS_REGISTRY_ID = "SYS_SPECIAL_VISITOR";

    /**
     * 系统用户类型
     */
    public static final class UTYPE {
        // 	专业观众
        public static final String VISITOR = "VISITOR";
    }

    /**
     * 系统返回状态
     */
    public static final class  SYS_STATUS{
        public static final String SUCCESS = "1";
        public static final String FAILURE = "0";
    }

    // 访问url
    public static final class URL {
        public static final String LOGIN_URL = "";
    }
}
