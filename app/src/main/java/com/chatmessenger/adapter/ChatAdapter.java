package com.chatmessenger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chatmessenger.R;
import com.chatmessenger.model.ChatMessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by prakashk on 7/7/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public ArrayList<ChatMessageModel> chatMessageModelList;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");


    public ChatAdapter(Context context, ArrayList<ChatMessageModel> chatMessageModelList) {
        this.context = context;
        this.chatMessageModelList = chatMessageModelList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1) {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_send, parent, false);

            return new ViewHolderSend(itemView);

        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_response, parent, false);
            return new ViewHolderResponse(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ChatMessageModel chatMessageModel = chatMessageModelList.get(position);

        if (holder.getItemViewType() == 1) {
            ((ViewHolderSend) holder).tv_sender_msg.setText(chatMessageModel.getMessageText());
            ((ViewHolderSend) holder).tv_sender_time.setText(SIMPLE_DATE_FORMAT.format(chatMessageModel.getMessageTime()));

            if (chatMessageModel.getMessageStatus()==2) {
                ((ViewHolderSend) holder).iv_seen_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_tick));
            } else  {
                ((ViewHolderSend) holder).iv_seen_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_single_tick));

            }
        } else {
            ((ViewHolderResponse) holder).tv_response_msg.setText(chatMessageModel.getMessageText());
            ((ViewHolderResponse) holder).tv_receiver_time.setText(SIMPLE_DATE_FORMAT.format(chatMessageModel.getMessageTime()));


        }

    }

    @Override
    public int getItemCount() {
        return chatMessageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessageModelList.get(position).getType_row();
    }

    public class ViewHolderSend extends RecyclerView.ViewHolder {
        public ImageView iv_seen_status;
        public TextView tv_sender_msg;
        public TextView tv_sender_time;

        public ViewHolderSend(View itemView) {
            super(itemView);
            this.iv_seen_status = (ImageView) itemView.findViewById(R.id.iv_seen_status);
            this.tv_sender_msg = (TextView) itemView.findViewById(R.id.tv_sender_msg);
            this.tv_sender_time = (TextView) itemView.findViewById(R.id.tv_sender_time);
        }
    }

    public class ViewHolderResponse extends RecyclerView.ViewHolder {
        public TextView tv_response_msg;
        public TextView tv_receiver_time;

        public ViewHolderResponse(View itemView) {
            super(itemView);
            this.tv_response_msg = (TextView) itemView.findViewById(R.id.tv_response_msg);
            this.tv_receiver_time = (TextView) itemView.findViewById(R.id.tv_receiver_time);
        }
    }
}
