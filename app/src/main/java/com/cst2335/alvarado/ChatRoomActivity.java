package com.cst2335.alvarado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoomActivity extends AppCompatActivity {
    //use them anywhere in the class:
    Button send;
    Button receive;
    EditText edit;
    RecyclerView rView;
    MyAdapter theAdapter;   //<<cannot be anonymous<<
    ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);
        edit = findViewById(R.id.editTextChat);
        rView = findViewById(R.id.myRecycleView);

        theAdapter = new MyAdapter();
        rView.setAdapter( theAdapter ) ;
        rView.setLayoutManager(new LinearLayoutManager(this));
        //rView.setLayoutManager(new GridLayoutManager (this, 2) );


        send.setOnClickListener( click ->{
            String whatIsTyped = edit.getText().toString();
            Date timeNow = new Date(); //when was this code run

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

            String currentDateandTime = sdf.format( timeNow ); //convert date to String


            //adding a new message to our history if not empty
            if ( !whatIsTyped.isEmpty()) {
                Message mess1 = new Message(edit.getText().toString(),1);
                messages.add(mess1);

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                theAdapter.notifyItemInserted(messages.size() - 1); //at the end of ArrayList


            }
        });

        receive.setOnClickListener( click ->{
            String whatIsTyped = edit.getText().toString();
            Date timeNow = new Date(); //when was this code run

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

            String currentDateandTime = sdf.format( timeNow ); //convert date to String


            //adding a new message to our history if not empty
            if ( !whatIsTyped.isEmpty()) {
                Message mess2 = new Message(edit.getText().toString(),2);
                messages.add(mess2);

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                theAdapter.notifyItemInserted(messages.size() - 1); //at the end of ArrayList


            }
        });


    }
    public class MyAdapter extends RecyclerView.Adapter< MyViewHolder > {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            LayoutInflater inflater = getLayoutInflater();
            int layoutID;
            if(viewType == 1)
                layoutID = R.layout.row_send;
            else
                layoutID = R.layout.row_receive;
            View thisRow = inflater.inflate(layoutID, parent, false);
            return new MyViewHolder(thisRow);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) { //need an ArrayList to hold all the messages.
            //MyViewHolder has time and message textViews

            // What message object is at position:
            //Message thisRow = messages.get(position);//

            //                      String object:
            //holder.timeView.setText( thisRow.getTimeSent() );//what time goes on row position
            holder.messageView.setText( messages.get(position).getMessageTyped() );//what message goes on row position
        }

        //returns the number of items in the array
        @Override
        public int getItemCount() {
            return messages.size() ; //how many items in the list
        }
        @Override
        public int getItemViewType(int position) {
            return messages.get(position).getSendOrReceive();
        }
    }

    //this holds TextViews on a row:
    public class MyViewHolder extends RecyclerView.ViewHolder{
        //TextView timeView;
        TextView messageView;

        //View will be a ConstraintLayout
        public MyViewHolder(View itemView) {
            super(itemView);

            messageView = itemView.findViewById(R.id.message);

            itemView.setOnClickListener( click -> {
                int position = getAdapterPosition();//which row was clicked.
                Message whatWasClicked = messages.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoomActivity.this );

                builder.setTitle("Do you want to delete this?")
                        .setMessage("The selected row is: " + position + "\nThe database id is: " + position)
                        .setNegativeButton("Negative", (dialog, click1)->{ })
                        .setPositiveButton("Positive", (dialog, click2)->{
                            //actually delete something:
                            messages.remove(position);
                            theAdapter.notifyItemRemoved(position);
                        }).create().show();
            });
        }
    }

    private class Message{

        String messageTyped;
        int sendOrReceive;

        public Message(String messageTyped, int sendOrReceive) {

            this.messageTyped = messageTyped;
            this.sendOrReceive = sendOrReceive;
        }

        public String getMessageTyped() {
            return messageTyped;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }
    }

    public int getCount(){
        return messages.size();
    }

    public Object getItem(int position){
        return messages.get(position);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        return convertView;
    }
    public long getItemId(int position){
        position = 7;
        return position;
    }
}