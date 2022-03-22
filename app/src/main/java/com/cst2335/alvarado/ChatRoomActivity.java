package com.cst2335.alvarado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoomActivity extends AppCompatActivity {

    //Create an OpenHelper to store data:
    MyOpenHelper myOpener;
    SQLiteDatabase theDatabase;

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

        //initialize it in onCreate
        myOpener = new MyOpenHelper( this );
        //open the database:
        theDatabase = myOpener.getWritableDatabase();

        //load from the database:
        Cursor results = theDatabase.rawQuery( "Select * from " + MyOpenHelper.TABLE_NAME + ";", null );//no arguments to the query

        printCursor(results,1);

        //Convert column names to indices:
        int idIndex = results.getColumnIndex( MyOpenHelper.COL_ID );
        int  messageIndex = results.getColumnIndex( MyOpenHelper.COL_MESSAGE);
        int sOrRIndex = results.getColumnIndex( MyOpenHelper.COL_SEND_RECEIVE);
        int timeIndex = results.getColumnIndex( MyOpenHelper.COL_TIME_SENT );

        //cursor is pointing to row -1
        while( results.moveToNext() ) //returns false if no more data
        { //pointing to row 2
            int id = results.getInt(idIndex);
            String message = results.getString( messageIndex );
            int sendOrReceive = results.getInt( sOrRIndex);

            //add to arrayList:
            messages.add( new Message( message, sendOrReceive, id ));
        }

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

                //insert into database:
                ContentValues newRow = new ContentValues();// like intent or Bundle

                //Message column:
                newRow.put(MyOpenHelper.COL_MESSAGE, whatIsTyped);

                //Send or receive column:
                newRow.put(MyOpenHelper.COL_SEND_RECEIVE, 1);

                //TimeSent column:
                newRow.put(MyOpenHelper.COL_TIME_SENT, currentDateandTime);

                //now that columns are full, you insert:

                long id = theDatabase.insert(MyOpenHelper.TABLE_NAME, null, newRow); //returns the id

                Message mess1 = new Message(edit.getText().toString(),1, id);

                //adding a new message to our history:
                messages.add(mess1); //what is the database id?


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

                //insert into database:
                ContentValues newRow = new ContentValues();// like intent or Bundle

                //Message column:
                newRow.put(MyOpenHelper.COL_MESSAGE, whatIsTyped);

                //Send or receive column:
                newRow.put(MyOpenHelper.COL_SEND_RECEIVE, 2);

                //TimeSent column:
                newRow.put(MyOpenHelper.COL_TIME_SENT, currentDateandTime);

                //now that columns are full, you insert:

                long id = theDatabase.insert(MyOpenHelper.TABLE_NAME, null, newRow); //returns the id

                Message mess2 = new Message(edit.getText().toString(),2,id);

                //adding a new message to our history:
                messages.add(mess2); //what is the database id?


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
                        .setMessage("The selected row is: " + position + "\nThe database id is: " + theAdapter.getItemViewType(position))
                        .setNegativeButton("Negative", (dialog, click1)->{ })
                        .setPositiveButton("Positive", (dialog, click2)->{
                            //actually delete something:
                            messages.remove(position);
                            theAdapter.notifyItemRemoved(position);
                            Snackbar.make(send, "You removed item # " + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", (click4)-> {
                                        messages.add(position, whatWasClicked);
                                        theAdapter.notifyItemInserted(position);
                                        //reinsert into the database
                                        theDatabase.execSQL( String.format( " Insert into %s values (\"%d\", \"%s\", \"%d\", \"%s\" );",
                                                MyOpenHelper.TABLE_NAME      , whatWasClicked.getId()  , whatWasClicked.getMessageTyped() , 1, whatWasClicked.getSendOrReceive()));

                                    })
                                    .show();
                            //delete from database:, returns number of rows deleted
                            theDatabase.delete(MyOpenHelper.TABLE_NAME,
                                    MyOpenHelper.COL_ID +" = ?", new String[] { Long.toString( whatWasClicked.getId() )  });
                        }).create().show();
            });
        }
    }

    private class Message{

        String messageTyped;
        int sendOrReceive;
        long id;

        public Message(String messageTyped, int sendOrReceive, long _id) {

            this.messageTyped = messageTyped;
            this.sendOrReceive = sendOrReceive;
            this.id = _id;
        }

        public String getMessageTyped() {
            return messageTyped;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public long getId(){return id; }

        public void setId(long id){this.id = id; }
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

    public void printCursor(Cursor c, int version){

        Log.v("LOGS", "Version is: " + theDatabase.getVersion());

        Log.v("LOGS", "Number of columns in the cursor: " + c.getColumnCount());

        Log.v("LOGS", "Name of columns in cursor: " +c.getColumnNames());

        Log.v("LOGS", "Number of rows in the cursor: " + c.getCount());

    }
}