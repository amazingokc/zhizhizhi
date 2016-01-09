package bean;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by xgj on 2016/1/7.
 */
public class Collection extends SugarRecord implements Serializable {

    public String allTitle;
    public String allUrl;
    public String allSummary;
    public String allVote;

    public Collection() {}

    public Collection(String allTitle, String allUrl, String allSummary, String allVote) {
        this.allTitle = allTitle;
        this.allUrl = allUrl;
        this.allSummary = allSummary;
        this.allVote = allVote;
    }

    public void setAllTitle(String title) {
        this.allTitle = title;
    }

    public String getAllTitle(){
        return allTitle;
    }

    public String getAllUrl(){
        return allUrl;
    }

    public String getAllSummary() {
        return allSummary;
    }

    public String getAllVote() {
        return allVote;
    }

}
