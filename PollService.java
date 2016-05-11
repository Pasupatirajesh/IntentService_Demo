package secondapp.bignerdranch.com.intentservicedemo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * Created by SSubra27 on 5/10/16.
 */
public class PollService extends IntentService{

    private static final String TAG = "PollService";
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG= "omsg";

    public static Intent newIntent(Context context)
    {
        return new Intent(context, PollService.class);
    }

    public PollService()
    {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.i(TAG, "Intent Service has started:" + intent);

        String msg = intent.getStringExtra(PARAM_IN_MSG);
        SystemClock.sleep(6000);
        String resultText = msg + " "+ DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis());

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(PARAM_OUT_MSG, resultText);
        sendBroadcast(broadcastIntent);

    }
}
