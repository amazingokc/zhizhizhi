package fragment;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by xgj on 2016/1/2.
 */
public class ArchiveFragment extends BaseFragment {

    @Override
    public void GetRecentdate() {
        Calendar now = Calendar.getInstance();
        String Year = now.get(Calendar.YEAR) + "";
        int month = (now.get(Calendar.MONTH) + 1);
        int day = (now.get(Calendar.DAY_OF_MONTH) - 1);
        String Month;
        String Day;
        if ((day < 10) && (day > 0)) {
            Day = "0" + day;
        } else if (day == 0) {
            Day = "27";
            month = month - 1 ;
        } else {
            Day = day + "";
        }

        if ((month < 10)&&(month > 0)){
            Month = "0" + month;
        } else if (month == 0){
            month = 12;
            Month = month + "";
            Year = now.get(Calendar.YEAR) - 1 + "";
        } else {
            Month = month + "";
        }
        Log.d("aa11", "ArchiveFragment");
        httpUrl = httpUrl + Year + Month + Day + "/" + "archive";
    }

    @Override
    protected void lazyLoad() {

    }
}
