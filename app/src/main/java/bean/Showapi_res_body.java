package bean;

import java.util.List;

/**
 * Created by xgj on 2015/12/13.
 */
public class Showapi_res_body {
    private int ret_code;

    private List<TypeList> typeList ;

    public void setRet_code(int ret_code){
        this.ret_code = ret_code;
    }
    public int getRet_code(){
        return this.ret_code;
    }
    public void setTypeList(List<TypeList> typeList){
        this.typeList = typeList;
    }
    public List<TypeList> getTypeList(){
        return this.typeList;
    }

}