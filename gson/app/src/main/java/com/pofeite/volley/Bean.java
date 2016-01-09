package com.pofeite.volley;

import java.util.List;

/**
 * Created by xgj on 2015/12/12.
 */
public class Bean{

    public Bean() {}

    int showapi_res_code;
    String showapi_res_error;

    ShowapiResBodyEntity showapi_res_body;

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public void setShowapi_res_body(ShowapiResBodyEntity showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public ShowapiResBodyEntity getShowapi_res_body() {
        return showapi_res_body;
    }

    public static class ShowapiResBodyEntity {
        int ret_code;

        public ShowapiResBodyEntity() {}


        public List<TypeListEntity> typeList;

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public void setTypeList(List<TypeListEntity> typeList) {
            this.typeList = typeList;
        }

        public int getRet_code() {
            return ret_code;
        }

        public List<TypeListEntity> getTypeList() {
            return typeList;
        }

    }
}
