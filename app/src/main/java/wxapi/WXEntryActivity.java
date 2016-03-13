package wxapi;


import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, "wxa7fe36921eba81f5", false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
    }


	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			//分享成功
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			//分享取消
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			//分享拒绝
			break;

		}

	}
}