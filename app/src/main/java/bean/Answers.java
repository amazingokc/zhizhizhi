package bean;

/**
 * Created by xgj on 2016/1/2.
 */
public class Answers {
    private String title;

    private String time;

    private String summary;

    private String questionid;

    private String answerid;

    private String authorname;

    private String authorhash;

    private String avatar;

    private String vote;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return this.time;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }
    public String getSummary(){
        return this.summary;
    }
    public void setQuestionid(String questionid){
        this.questionid = questionid;
    }
    public String getQuestionid(){
        return this.questionid;
    }
    public void setAnswerid(String answerid){
        this.answerid = answerid;
    }
    public String getAnswerid(){
        return this.answerid;
    }
    public void setAuthorname(String authorname){
        this.authorname = authorname;
    }
    public String getAuthorname(){
        return this.authorname;
    }
    public void setAuthorhash(String authorhash){
        this.authorhash = authorhash;
    }
    public String getAuthorhash(){
        return this.authorhash;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public void setVote(String vote){
        this.vote = vote;
    }
    public String getVote(){
        return this.vote;
    }
}
