package secondapp.bignerdranch.com.intentservicedemo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int POLL_INTERVAL = 1000 * 60 * 60;

    private Button mServiceButton;
    private TextView mTextView;
    private EditText mEditText;
    private ResponseReceiver mResponseReceiver;

    public static Intent newIntent(Context context)
    {
         return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mServiceButton = (Button)findViewById(R.id.start_service);
        mEditText = (EditText)findViewById(R.id.edit_text_msg);
        final String in_str = mEditText.getText().toString();
        mServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = PollService.newIntent(getApplicationContext());
                i.putExtra(PollService.PARAM_IN_MSG, in_str);
                PendingIntent pi = PendingIntent.getService(getApplicationContext(),0,i,0);
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                boolean isOn = true;
                if(isOn)
                {

                    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
                }
               else
                {
                    alarmManager.cancel(pi);
                    pi.cancel();
                }

                Resources resources = getResources();
                Intent ni = MainActivity.newIntent(getApplicationContext());
                PendingIntent pin = PendingIntent.getActivity(getApplicationContext(),0,ni,0);
                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                        .setTicker("IntentServiceDemo")
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                        .setContentIntent(pin)
                        .setAutoCancel(true)
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(0, notification);
                Toast.makeText(MainActivity.this, "Intent Service Started", Toast.LENGTH_SHORT).show();
            }
        });

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mResponseReceiver = new ResponseReceiver();
        registerReceiver(mResponseReceiver,filter);
    }

    public class ResponseReceiver extends BroadcastReceiver
    {
        public static final String ACTION_RESP = "com.bignerdranch.secondapp.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent)
        {
            TextView textView = (TextView) findViewById(R.id.my_text_view);
            String text = intent.getStringExtra(PollService.PARAM_OUT_MSG);
            textView.setText(text);
        }
    }
}
