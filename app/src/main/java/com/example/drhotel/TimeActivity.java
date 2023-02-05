package com.example.drhotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Button;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

import com.example.drhotel.adapter.RoomCardAdapter;
import com.example.drhotel.databinding.ActivityMain2Binding;
import com.example.drhotel.databinding.ActivityTimeBinding;
import com.example.drhotel.model.CreateOrder;
import com.example.drhotel.model.Room;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private DatePicker datePicker;
    private EditText editTextDate;
    private EditText editTextClient;
    private Button buttonDate, buttonPay;
    private TimePicker timePicker;
    private TextView textViewTime, textViewCode;
    private TextView nameRoom;
    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int hour = calendar.get(Calendar.HOUR);
    private int minute = calendar.get(Calendar.MINUTE);
    private static final String TAG = "MainActivity";
    private String dateRoom;
    private List<Room> itemsList = new ArrayList<>();
    private RoomCardAdapter customAdapter;
    // Change this value and run the application again.
    private boolean is24HView = true;
    private Room room;
    private String value2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        calendar.setTimeInMillis(System.currentTimeMillis());
        this.scrollView = (ScrollView) this.findViewById(R.id.scrollView);
        this.editTextDate = (EditText) this.findViewById(R.id.editText_date);
        this.buttonDate = (Button) this.findViewById(R.id.button_date);
        this.datePicker = (DatePicker) this.findViewById(R.id.datePicker);
        this.editTextClient = (EditText) this.findViewById(R.id.editClient);
        this.textViewTime = (TextView) this.findViewById(R.id.textView_time);
        this.timePicker = (TimePicker) this.findViewById(R.id.timePicker);
        this.nameRoom = (TextView) this.findViewById(R.id.nameRoom);
        this.textViewCode = this.findViewById(R.id.code);
        this.timePicker.setIs24HourView(this.is24HView);
        this.buttonPay = this.findViewById(R.id.button_pay);
        Intent intent = getIntent();
        String value1 = intent.getStringExtra("nameRoomString");
        value2 = intent.getStringExtra("nameRoom");
        nameRoom.setText(value1);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // bind components with ids

        this.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timePickerChange(view, hourOfDay, minute);
            }
        });

        this.datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                datePickerChange(datePicker, year, month, dayOfMonth);
            }
        });
        this.buttonDate.setOnClickListener(view -> {
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        JSONObject data = orderApi.createOrder("100");


                        String code = data.getString("return_code");
                        Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                        if (code.equals("1")) {
                            textViewCode.setText(data.getString("zp_trans_token"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        this.buttonPay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String token = textViewCode.getText().toString();
                ZaloPaySDK.getInstance().payOrder(TimeActivity.this, token, "demode://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(() -> new AlertDialog.Builder(TimeActivity.this)
                                .setTitle("Payment Success")
                                .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                .setPositiveButton("OK", (dialog, which) -> {
                                })
                                .setNegativeButton("Cancel", null).show());
//                                showDate();
//                                getData();
////                                finish();
//                                onBackPressed();
                    }
                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(TimeActivity.this)
                                .setTitle("User Cancel Payment")
                                .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                .setPositiveButton("OK", (dialog, which) -> {
                                })
                                .setNegativeButton("Cancel", null).show();
//                                showDate();
//                                getData();
////                finish();
//                                onBackPressed();
                    }
                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(TimeActivity.this)
                                .setTitle("Payment Fail")
                                .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                .setPositiveButton("OK", (dialog, which) -> {
                                })
                                .setNegativeButton("Cancel", null).show();
                    }
                });


            }
        });

    }

    private void timePickerChange(TimePicker view, int hourOfDay, int minute) {
        this.textViewTime.setText(hourOfDay + ":" + minute);
    }

    private void datePickerChange(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
        Log.d("test", String.valueOf(this.hour));
        this.editTextDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showDate() {
        int year = this.datePicker.getYear();
        int month = this.datePicker.getMonth(); // 0 - 11
        int day = this.datePicker.getDayOfMonth();
        int hourOfDay = this.timePicker.getHour();
        int minute = this.timePicker.getMinute();
        Toast.makeText(this, "Date: " + day + "-" + (month + 1) + "-" + year, Toast.LENGTH_LONG).show();
        dateRoom = "Date: " + day + "-" + (month + 1) + "-" + year + " " + "Time: " + hourOfDay + ":" + minute;
    }

    private void getData() {
        // Read data from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://drhotle-default-rtdb.asia-southeast1.firebasedatabase.app/");
        String pathDate = "room/" + value2 + "/date";
        String pathStt = "room/" + value2 + "/status";
        String pathClient = "room/" + value2 + "/detailClient";
        DatabaseReference myRefDate = database.getReference(pathDate);
        myRefDate.setValue(dateRoom);
        DatabaseReference myRefStt = database.getReference(pathStt);
        myRefStt.setValue("1");
        DatabaseReference myRefClient = database.getReference(pathClient);
        myRefClient.setValue(editTextClient.getText().toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
