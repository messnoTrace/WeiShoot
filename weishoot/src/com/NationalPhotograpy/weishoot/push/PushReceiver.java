
package com.NationalPhotograpy.weishoot.push;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.NationalPhotograpy.weishoot.activity.registered.WelcomeActivity;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;

/**
 * 自定义接收器 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private static final String myPackageName = "com.NationalPhotograpy.weishoot"; // 项目名称

    /**
     * 软件打开中
     */
    private static final int ISOPEN = 1; // 打开中

    /**
     * 软件在后台运行中
     */
    private static final int ISBACK = 2; // 后台运行中

    /**
     * 软件关闭中
     */
    private static final int NORUN = 3; // 没有打开

    private static String topActivity = null;

    private static final String LOGPAGENAME = ".activity.registered.LoginActivity";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        } else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String flag = null;
            if (bundle.containsKey(JPushInterface.EXTRA_EXTRA))
                flag = bundle.getString(JPushInterface.EXTRA_EXTRA);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String extra_msg = "";
            topActivity = null;
            if (bundle.containsKey(JPushInterface.EXTRA_EXTRA))
                extra_msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
            // if (extra_msg == null || extra_msg.equals("") ||
            // extra_msg.equals("{}")) {
            // return;
            // }
            extra_msg = bundle.getString(JPushInterface.EXTRA_ALERT);
            // 获取项目的运行状态
            int runState = isRuning(context);
            // 如果当前已经是打开状态则不做操作
            if (runState == ISOPEN) {
                pushIntent(context, extra_msg);
            } else if (runState == ISBACK) {
                pushIntent(context, extra_msg);
            } else if (runState == NORUN) {
                // 如果项目没有运行
                StaticInfo.isPushIntent = true;
                Intent i = new Intent(context, WelcomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // 关闭时如果没有附加值则打开启动页正常启动
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                i.setAction(Intent.ACTION_MAIN);
                context.startActivity(i);
            }

        }
    }

    private void pushIntent(Context context, String msg) {
        if (topActivity != null && topActivity.equals(LOGPAGENAME)) {
            Intent mIntent = new Intent("finish_login_page_action");
            context.sendBroadcast(mIntent);
        }
        Intent i = new Intent(context, PushMsgActivity.class);
        i.putExtra("msg", msg);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * isRuning(判断应用是否在运行中)
     */
    private static int isRuning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(100);
        if (taskList == null)
            return ISOPEN;
        for (int i = 0; i < taskList.size(); i++) {
            RunningTaskInfo info = taskList.get(i);
            String n1 = info.topActivity.getPackageName();
            String n2 = info.baseActivity.getPackageName();
            if (n1.equals(myPackageName) && n2.equals(myPackageName)) {
                topActivity = info.topActivity.getShortClassName();
                if (i == 0) {
                    return ISOPEN;
                } else {
                    return ISBACK;
                }
            }
        }
        return NORUN;
    }
}
