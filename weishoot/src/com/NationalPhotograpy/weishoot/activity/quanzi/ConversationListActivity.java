
package com.NationalPhotograpy.weishoot.activity.quanzi;

import android.net.Uri;
import android.os.Bundle;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;

public class ConversationListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list);
        initView();
        // initData();
        setListener();

    }

    private void initView() {

    }

//    private void initData() {
//        /* 创建 conversationlist 的Fragment */
//        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.conversationlist);
//
//        /* 给 IMKit 传递默认的参数，用于显示 */
//        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversationlist")
//                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") // 设置私聊会话采用聚合显示
//                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true") // 设置群组会话采用聚合显示
//                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false") // 设置讨论组不采用聚合显示
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true") // 设置系统会话采用聚合显示
//                .build();
//
//        fragment.setUri(uri);
//    }

    private void setListener() {

    }
}
