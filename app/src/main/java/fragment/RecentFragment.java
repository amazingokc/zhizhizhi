package fragment;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by xgj on 2016/1/2.
 */
public class RecentFragment extends BaseFragment {

    @Override
    public void GetRecentdate() {
        Calendar now = Calendar.getInstance();
        String Year = now.get(Calendar.YEAR) + "";
        int month = (now.get(Calendar.MONTH) + 1);
        int day = (now.get(Calendar.DAY_OF_MONTH));
        String Month;
        String Day;
        if (month < 10) {
            Month = "0" + month;
        }else {
            Month = month + "";
        }
        if (day < 10) {
            Day = "0" + day;
        }
        else {
            Day = day + "";
        }
        Log.d("aa11", "RecentFragment");
        httpUrl = httpUrl + Year + Month + Day + "/" + "recent";
    }

    @Override
    protected void lazyLoad() {

    }

}
