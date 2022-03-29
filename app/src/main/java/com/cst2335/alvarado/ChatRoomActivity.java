package com.cst2335.alvarado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoomActivity extends AppCompatActivity {

    //Create an OpenHelper to store data:

    SQLiteDatabase theDatabase;

    //use them anywhere in the class:
    Button send;
    Button receive;
    EditText edit;
    RecyclerView rView;
    ListView lView;
    ListAdapter listAdapter;
    MyAdapter theAdapter;   //<<cannot be anonymous<<
    ArrayList<Message> messages = new ArrayList<>();
    boolean isTablet = false;
    static String ITEM_ID = "ITEM_ID";
    static String ITEM_SELECT = "ITEM_SELECT";
    static String ITEM_SorR = "ITEM_SorR";
    //MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        isTablet = findViewById(R.id.frameLayout) != null;
        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);
        edit = findViewById(R.id.editTextChat);
        lView = findViewById(R.id.listView);

        lView.setAdapter( (ListAdapter) (theAdapter = new MyAdapter())) ;
        DetailsFragment firstFragment = new DetailsFragment();

        loadData();

        send.setOnClickListener( click ->{
            String whatIsTyped = edit.getText().toString();
            Date timeNow = new Date(); //when was this code run

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

            String currentDateandTime = sdf.format( timeNow ); //convert date to String


            //adding a new message to our history if not empty
            if ( whatIsTyped.isEmpty()) {

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

                Message mess1 = new Message(edit.getText().toString(),true, id);


                edit.setText("");//clear the text


                //adding a new message to our history:
                messages.add(mess1); //what is the database id?

                //notify that new data was added at a row:
                theAdapter.notifyDataSetChanged(); //at the end of ArrayList


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

                Message mess2 = new Message(edit.getText().toString(),false,id);

                edit.setText("");//clear the text

                //adding a new message to our history:
                messages.add(mess2); //what is the database id?

                //notify that new data was added at a row:
                theAdapter.notifyDataSetChanged(); //at the end of ArrayList


            }
        });

        lView.setOnItemClickListener( (list, view, position, id) -> {
            Message whatWasClicked = messages.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoomActivity.this );

            builder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position + "\nThe database id is: " + theAdapter.getItemViewType(position))
                    .setNegativeButton("Negative", (dialog, click1)->{ })
                    .setPositiveButton("Positive", (dialog, click2)->{
                        //actually delete something:
                        messages.remove(position);
                        theAdapter.notifyDataSetChanged();
                        Snackbar.make(send, "You removed item # " + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", (click4)-> {
                                    messages.add(position, whatWasClicked);
                                    theAdapter.notifyDataSetChanged();
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


//        lView.setLayoutManager(new LinearLayout(this));
        //lView.setLayoutManager(new GridLayoutManager (this, 2) );

        lView.setOnItemClickListener((list, view, position, id) -> {

            Bundle bundle = new Bundle();
            bundle.putString(ITEM_SELECT,messages.get(position).getMessageTyped());
            bundle.putLong(ITEM_ID,id);
            bundle.putBoolean(ITEM_SorR,messages.get(position).getSendOrReceive());

            firstFragment.setArguments(bundle);

            if (isTablet) {
                // Add Fragment to FrameLayout (flContainer), using FragmentManager
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
                ft.setReorderingAllowed(true);
                ft.add(R.id.frameLayout, firstFragment);    // add    Fragment
                ft.commit();     // commit FragmentTransaction

            }else{
                Intent goToEmpty = new Intent(this, EmptyActivity.class);
                goToEmpty.putExtras(bundle);
                startActivity(goToEmpty);
            }
        } );

    }
    public class MyAdapter extends BaseAdapter {

//        @NonNull
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//
//            LayoutInflater inflater = getLayoutInflater();
//            int layoutID;
//            if(viewType == 1)
//                layoutID = R.layout.row_send;
//            else
//                layoutID = R.layout.row_receive;
//            View thisRow = inflater.inflate(layoutID, parent, false);
//            return new MyViewHolder(thisRow);
//
//        }

//        public void onBindViewHolder(MyViewHolder holder, int position) { //need an ArrayList to hold all the messages.
//            //MyViewHolder has time and message textViews
//
//            // What message object is at position:
//            //Message thisRow = messages.get(position);//
//
//            //                      String object:
//            //holder.timeView.setText( thisRow.getTimeSent() );//what time goes on row position
//            holder.messageView.setText( messages.get(position).getMessageTyped() );//what message goes on row position
//        }

        //returns the number of items in the array



        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View original, ViewGroup viewType) {

            LayoutInflater inflater = getLayoutInflater();
            int layoutID_SEND = R.layout.row_send;
            int layoutID_RECEIVE = R.layout.row_receive;

            View view = original;

            if(view == null){
                if (messages.get(position).getSendOrReceive())
                    view = inflater.inflate(layoutID_SEND, viewType, false);
                else
                    view = inflater.inflate(layoutID_RECEIVE, viewType, false);
        }

            TextView messageText  = view.findViewById(R.id.message);
            messageText .setText( messages.get(position).getMessageTyped() );
            return view;

        }

    }

    public void loadData(){


        //initialize it in onCreate
        MyOpenHelper myOpener = new MyOpenHelper( this );
        //open the database:
        theDatabase = myOpener.getWritableDatabase();

        //load from the database:
        Cursor results = theDatabase.rawQuery( "Select * from " + MyOpenHelper.TABLE_NAME + ";", null );//no arguments to the query

        printCursor(results,1);

        //Convert column names to indices:
        int idIndex = results.getColumnIndex( MyOpenHelper.COL_ID );
        int  messageIndex = results.getColumnIndex( MyOpenHelper.COL_MESSAGE);
        int sOrRIndex = results.getColumnIndex( MyOpenHelper.COL_SEND_RECEIVE);
        //       int timeIndex = results.getColumnIndex( MyOpenHelper.COL_TIME_SENT );

        //cursor is pointing to row -1
        while( results.moveToNext() ) //returns false if no more data
        { //pointing to row 2
            int id = results.getInt(idIndex);
            String message = results.getString( messageIndex );
            boolean sendOrReceive = results.getInt( sOrRIndex) == 1 ? true : false;

            //add to arrayList:
            messages.add( new Message( message, sendOrReceive, id ));
        }
    }

//    //this holds TextViews on a row:
//    public class MyViewHolder{
//        //TextView timeView;
//        TextView messageView;
//
//        //View will be a ConstraintLayout
//        public MyViewHolder(View itemView) {
//
//
//
//
//        }
//    }

    private class Message{

        String messageTyped;
        boolean sendOrReceive;
        long id;

        public Message(String messageTyped, boolean sendOrReceive, long id) {

            this.messageTyped = messageTyped;
            this.sendOrReceive = sendOrReceive;
            this.id = id;
        }

        public String getMessageTyped() {
            return messageTyped;
        }

        public boolean getSendOrReceive() {
            return sendOrReceive;
        }

        public long getId(){return id; }

    }

    public void printCursor(Cursor c, int version){

        Log.v("LOGS", "Version is: " + theDatabase.getVersion());

        Log.v("LOGS", "Number of columns in the cursor: " + c.getColumnCount());

        Log.v("LOGS", "Name of columns in cursor: " +c.getColumnNames());

        Log.v("LOGS", "Number of rows in the cursor: " + c.getCount());

    }
}