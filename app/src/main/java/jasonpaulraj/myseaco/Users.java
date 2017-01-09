package jasonpaulraj.myseaco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GPH on 12/20/2016.
 */

public class Users {

    private String userId;
    private String userFullName;
    private String userName;
    private String userPassword;
    private String userRegtype;
    private String regDate;

    public String JSON_STRING;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRegtype() {
        return userRegtype;
    }

    public void setUserRegtype(String userRegtype) {
        this.userRegtype = userRegtype;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public static Users myMethod(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
        JSONObject c = result.getJSONObject(0);

        Users u = new Users();
        u.setUserId(c.getString(Config.KEY_USER_ID));
        u.setUserFullName(c.getString(Config.KEY_USER_FULLNAME));
        u.setUserName(c.getString(Config.KEY_USER_NAME));
        u.setUserPassword(c.getString(Config.KEY_USER_PASSWORD));
        u.setUserRegtype(c.getString(Config.KEY_USER_Regtype));
        u.setRegDate(c.getString(Config.KEY_USER_REGDATE));
        return u;
    }
}
