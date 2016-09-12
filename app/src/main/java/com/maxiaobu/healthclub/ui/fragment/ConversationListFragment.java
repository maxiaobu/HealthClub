package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.util.NetUtils;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.MainActivity;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.Constant;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.InviteMessgeDao;
import com.maxiaobu.healthclub.chat.db.UserDao;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanAccountInfo;
import com.maxiaobu.healthclub.ui.activity.ChatActivity;
import com.maxiaobu.healthclub.ui.activity.HomeActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestJsonListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maxiaobu.easeui.domain.EaseUser;
import maxiaobu.easeui.model.EaseAtMessageHelper;
import maxiaobu.easeui.ui.EaseConversationListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationListFragment extends EaseConversationListFragment {
    private TextView errorText;
    private ConversationListFragment instance;


    @Override
    protected void initView() {
        super.initView();
        // TODO: 2016/9/6 重写加载失败页面
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        instance = this;
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });


// TODO: 2016/9/12 会话列表头像加载
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (final EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
                String userName = conversation.getUserName();
                Log.d("ConversationListFragmen", userName);
                if (!userName.equals("admin")) {
                    App.getRequestInstance().post(getActivity(), UrlPath.URL_ACCOUNT_INFO, BeanAccountInfo.class
                            , new RequestParams("memid","M"+ userName.substring(1)),
                            new RequestJsonListener<BeanAccountInfo>() {
                                @Override
                                public void requestSuccess(BeanAccountInfo beanAccountInfo) {
                                    String s = "m" + beanAccountInfo.getMemid().substring(1);
                                    EaseUser easeUser = new EaseUser(s);
                                    String nickName = beanAccountInfo.getNickname();
                                    String avatar = beanAccountInfo.getImgsfile();
                                    Log.d("ConversationListFragmen", nickName + "*********" + avatar);
                                    easeUser.setAvatar(avatar);
                                    easeUser.setNick(nickName);
                                    // 存入内存
                                    Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
                                    contactList.put(s, easeUser);
                                    // 存入db
                                    UserDao dao = new UserDao(App.getInstance());
                                    List<EaseUser> users = new ArrayList<EaseUser>();
                                    users.add(easeUser);
                                    dao.saveContactList(users);
                                    DemoHelper.getInstance().getModel().setContactSynced(true);
                                    // 通知listeners联系人同步完毕
                                    DemoHelper.getInstance().notifyContactsSyncListener(true);
                                }

                                @Override
                                public void requestAgain(NodataFragment nodataFragment) {

                                }
                            });
                }
            }
        }


    }


    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.getUserName());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((HomeActivity) getActivity()).updateUnreadAddressLable();
        return true;
    }

}
