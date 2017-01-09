package jasonpaulraj.myseaco;

public class UserDetail {

    private String userId;
    private String userFullName;
    private String userName;
    private String userPassword;
    private String userRegtype;
    private String regDate;


    public UserDetail(){}

    public UserDetail(String userId, String userFullName, String userName, String userPassword, String userRegtype,  String regDate){
        super();
        this.userId = userId;
        this.userFullName = userFullName;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRegtype = userRegtype;
        this.regDate = regDate;
    }

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
}