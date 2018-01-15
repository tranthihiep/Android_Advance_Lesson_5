package com.haha.appnghenhac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by trant on 15/01/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        Toast.makeText(context, dateFormat.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
        //turn off music

        System.exit(0);

    }

}
