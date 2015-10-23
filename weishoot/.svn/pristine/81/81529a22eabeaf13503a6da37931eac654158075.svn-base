/**
 * @Title: PushUtil.java
 * @Package com.itotem.traffic.broadcasts.utils
 * Company:isoftstone
 * @author wzh
 * @date 2013-5-30 下午3:01:52
 * @version V1.0
 */

package com.NationalPhotograpy.weishoot.push;

import java.util.Set;

import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @ClassName: PushUtil
 * @Description：请用一句话描述此类的作用
 * @author wzh
 * @date 2013-5-30 下午3:01:52
 */

public class PushUtil implements TagAliasCallback {
    private int num = 0;
    private Context mcontext;

    /**
     * setAlias(为推送设置别名)
     * 
     * @param c
     * @param alias
     * @throws
     * @author wzh
     */
    public void setAlias(Context context, String alias) {
        try {
            mcontext = context;
            Log.w("wzh", "推送设置别名" + alias);
            JPushInterface.setAliasAndTags(mcontext, alias, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void gotResult(int code, String alias, Set<String> tags) {
        switch (code) {
            case 0:
                Log.w("wzh", "推送别名设置成功");
                break;
            case 6002:
                if (num < 20) {
                    // 尝试3次
                    ++num;
                    Log.w("wzh", "推送别名设置失败，开始重试");
                    setAlias(mcontext, alias);

                }
                break;
            default:
                Log.w("wzh", "推送设置别名未知错误，Code=" + code);
        }
    }
}
