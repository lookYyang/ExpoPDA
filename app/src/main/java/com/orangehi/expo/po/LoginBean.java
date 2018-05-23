package com.orangehi.expo.po;

import java.util.List;

/**
 * @author yang
 * @date 2018-05-14 02:03:10
 */

public class LoginBean {

    private static final long serialVersionUID = 1L;

    private String appcode;

    private String appmsg;

    private List<AosUserPO> data;

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getAppmsg() {
        return appmsg;
    }

    public void setAppmsg(String appmsg) {
        this.appmsg = appmsg;
    }

    public List<AosUserPO> getData() {
        return data;
    }

    public void setData(List<AosUserPO> data) {
        this.data = data;
    }
}
