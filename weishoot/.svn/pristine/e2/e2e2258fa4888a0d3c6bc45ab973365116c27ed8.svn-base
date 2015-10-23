
package com.NationalPhotograpy.weishoot.utils.UserInfo;

import android.content.Context;
import android.text.TextUtils;

import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;

/**
 * 用户信息管理类
 */
public class UserInfo {
    private static Context context;

    private UserInfo() {
    }

    private static class UserInfoHolder {
        private final static UserInfo INSTANCE = new UserInfo();
    }

    public static UserInfo getInstance(Context con) {
        context = con;
        return UserInfoHolder.INSTANCE;
    }

    private String UCode = "";

    private String NickName = "";

    private String UserHead = "";

    private String Sex = "";

    private String Introduction = "";

    private String password = "";

    private String loginName = "";

    private String Telephone = "";

    public boolean isLogin() {
        return SharePreManager.getInstance(context).userIsLogin();
    }

    public void setUserLoginStatus(boolean isLogin) {
        SharePreManager.getInstance(context).setUserLoginStatus(isLogin);
    }

    public String getUserUCode() {
        if (TextUtils.isEmpty(UCode)) {
            UCode = SharePreManager.getInstance(context).getUserUCode();
        }
        return UCode;
    }

    public void setUserUCode(String ucode) {
        SharePreManager.getInstance(context).setUserUCode(ucode);
        this.UCode = ucode;
    }

    public String getUserNickName() {
        if (TextUtils.isEmpty(NickName)) {
            NickName = SharePreManager.getInstance(context).getUserNickName();
        }
        return NickName;
    }

    public void setUserNickName(String nickName) {
        SharePreManager.getInstance(context).setUserNickName(nickName);
        this.NickName = nickName;
    }

    public String getUserHead() {
        return SharePreManager.getInstance(context).getUserHead();
    }

    public void setUserHead(String userHead) {
        SharePreManager.getInstance(context).setUserHead(userHead);
        this.UserHead = userHead;
    }

    public String getUserSex() {
        if (TextUtils.isEmpty(Sex)) {
            Sex = SharePreManager.getInstance(context).getUserSex();
        }
        return Sex;
    }

    public void setUserSex(String userSex) {
        SharePreManager.getInstance(context).setUserSex(userSex);
        this.Sex = userSex;
    }

    public String getUserIntroduction() {
        if (TextUtils.isEmpty(Introduction)) {
            Introduction = SharePreManager.getInstance(context).getUserIntroduction();
        }
        return Introduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        SharePreManager.getInstance(context).setUserIntroduction(userIntroduction);
        this.Introduction = userIntroduction;
    }

    public String getUserPassword() {
        if (TextUtils.isEmpty(password)) {
            password = SharePreManager.getInstance(context).getUserPassword();
        }
        return password;
    }

    public void setUserPassword(String userPassword) {
        SharePreManager.getInstance(context).setUserPassword(userPassword);
        this.password = userPassword;
    }

    public String getUserLoginName() {
        if (TextUtils.isEmpty(loginName)) {
            loginName = SharePreManager.getInstance(context).getUserLoginName();
        }
        return loginName;
    }

    public void setUserLoginName(String userLoginName) {
        SharePreManager.getInstance(context).setUserLoginName(userLoginName);
        this.loginName = userLoginName;
    }

    public String getUserTelephone() {
        if (TextUtils.isEmpty(Telephone)) {
            Telephone = SharePreManager.getInstance(context).getUserTelephone();
        }
        return Telephone;
    }

    public void setUserTelephone(String userTelephone) {
        SharePreManager.getInstance(context).setUserTelephone(Telephone);
        this.Telephone = Telephone;
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {

        SharePreManager.getInstance(context).clearUserInfo();

        UCode = "";
        NickName = "";
        UserHead = "";
        Sex = "";
        Introduction = "";
        password = "";
        loginName = "";
    }

    /**
     * 注销帐号，退出登录
     */
    public void logout() {
        // 清空用户信息
        setUserLoginStatus(false);
        StaticInfo.isLoadWebView = true;
        clearUserInfo();
    }
}
