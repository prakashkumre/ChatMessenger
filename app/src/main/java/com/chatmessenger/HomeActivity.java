package com.chatmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chatmessenger.adapter.ChatAdapter;
import com.chatmessenger.database.SqLiteHelper;
import com.chatmessenger.model.ChatMessageModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    private EditText et_chat;
    private ImageView iv_send;
    private ArrayList<ChatMessageModel> chatMessageList;
    private ChatAdapter chatAdapter;
    private static final String ALLOWED_CHARACTERS = "qwertyuiopasdfghjklzxcvbnm";
    private RecyclerView rv_chat;
    private SqLiteHelper sqLiteHelper;
    String[] randomMsgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        et_chat = (EditText) findViewById(R.id.et_chat);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        chatMessageList = new ArrayList<>();
        sqLiteHelper = new SqLiteHelper(this);
        iv_send.setOnClickListener(sendClickListener);
        et_chat.addTextChangedListener(chatTextWatcher);
        et_chat.setOnClickListener(editTextClickListner);
        chatMessageList = sqLiteHelper.getSendeData();

        chatAdapter = new ChatAdapter(HomeActivity.this, chatMessageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        rv_chat.setLayoutManager(mLayoutManager);
        rv_chat.setAdapter(chatAdapter);
        rv_chat.scrollToPosition(chatMessageList.size() - 1);


    }

    private ImageView.OnClickListener sendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == iv_send) {
                sendMessage(et_chat.getText().toString());
            }

            et_chat.setText("");

        }
    };
    private EditText.OnClickListener editTextClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rv_chat.scrollToPosition(chatMessageList.size()+1);
        }
    };

    private final TextWatcher chatTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
           // rv_chat.scrollToPosition(chatMessageList.size()-1);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (et_chat.getText().toString().equals("")) {

            } else {
                iv_send.setImageResource(R.drawable.ic_chat_send);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                iv_send.setImageResource(R.drawable.ic_chat_send);
            } else {
                iv_send.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };

    private void sendMessage(final String messageText) {
        if (messageText.trim().length() == 0)
            return;

        final ChatMessageModel message = new ChatMessageModel();
        message.setMessageStatus(1);
        message.setMessageText(messageText);
        message.setType_row(1);
        message.setMessageTime(new Date().getTime());
        chatMessageList.add(message);


        if (chatAdapter != null)
            chatAdapter.notifyDataSetChanged();
        rv_chat.scrollToPosition(chatMessageList.size() - 1);

        // Mark message as delivered after one second

        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

        exec.schedule(new Runnable() {
            @Override
            public void run() {
                message.setMessageStatus(2);
                sqLiteHelper.addSenderData(message);
                final ChatMessageModel message = new ChatMessageModel();
                message.setMessageStatus(1);
                message.setMessageText(getRandomString(messageText.length()));
                message.setType_row(2);
                message.setMessageTime(new Date().getTime());
                chatMessageList.add(message);
                sqLiteHelper.addSenderData(message);
                HomeActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                        rv_chat.scrollToPosition(chatMessageList.size() - 1);
                    }
                });


            }
        }, 1, TimeUnit.SECONDS);

    }


    private String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        int randomMsgIndex = random.nextInt(chatMessageList.size());
        return chatMessageList.get(randomMsgIndex).getMessageText();
        /*final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();*/
    }
}
