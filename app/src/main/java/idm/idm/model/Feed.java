package idm.idm.model;

/**
 * Created by Lily
 * Used with the Instagram API to store data.
 */

public class Feed{
    private String user;
    private String media_url;
    private String caption;
    private String timestamp;

    public Feed(String User, String Media_URL, String Caption, String Timestamp) {
        this.user = User;
        this.media_url = Media_URL;
        this.caption = Caption;
        this.timestamp = Timestamp;
    }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) { this.caption = caption; }

    public String getTimestamp() { return timestamp; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

}

