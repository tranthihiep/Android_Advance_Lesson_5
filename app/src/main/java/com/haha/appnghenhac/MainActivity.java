package com.haha.appnghenhac;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ArrayList<Songs> arraySong;
    private Button mBtnTimer;
    private EditText mEdtTime;
    private ImageButton mBtnPlay,mBtnNext,mBtnPrevious;
    private TextView mTxtTimeEnd,mTxtTimeStart;
    private SeekBar mSeekBar;
    private int mPos = 0;
    private MediaPlayer mMediaPlayer;
    private PendingIntent mpPendingIntent;
    private AlarmManager mAlarmManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
        mpPendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
        initView();
        addSong();
        initMedia();


    }

    private void addSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Songs(R.raw.chuctet));
        arraySong.add(new Songs(R.raw.ngaytetqueem));
        arraySong.add(new Songs(R.raw.tetnguyendan));
    }

    private void initView() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mEdtTime = (EditText) findViewById(R.id.edtTime);
        mBtnTimer = (Button) findViewById(R.id.btnTimerOff);
        mBtnNext = (ImageButton) findViewById(R.id.btnNext);
        mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
        mBtnPrevious = (ImageButton) findViewById(R.id.btnPerious);
        mTxtTimeEnd = (TextView) findViewById(R.id.txtTimeEnd);
        mTxtTimeStart = (TextView) findViewById(R.id.txtTimeStart);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mBtnTimer.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnPrevious.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(mSeekBar.getProgress());
            }
        });

        switch (view.getId()){
            case R.id.btnPlay :
                onPlayMusic();
                break;
            case R.id.btnNext :
                onNextSongs();
                break;
            case R.id.btnPerious :
               onPeriousSongs();
                break;
            case R.id.btnTimerOff:
                try {
                    long timeInput = Long.parseLong(mEdtTime.getText().toString());
                    setTimeTurnOffMusic(timeInput*60*1000);
                }catch(Exception ex)
                {
                    Log.i("Exception", ": "+ ex.toString());
                }
                createNotification();
                break;

        }
    }

    private void onPeriousSongs() {
        mPos--;
        if (mPos <0){
            mPos = arraySong.size() -1;
        }
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        initMedia();
        mMediaPlayer.start();
        mBtnPlay.setImageResource(R.drawable.pause);
        setTimeEnd();
        updateTimeSong();
    }

    private void onNextSongs() {
        mPos++;
        if (mPos >arraySong.size()-1){
            mPos = 0;
        }
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        initMedia();
        mMediaPlayer.start();
        mBtnPlay.setImageResource(R.drawable.pause);
        setTimeEnd();
        updateTimeSong();
    }

    private void setTimeTurnOffMusic(long interval){

        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime()+interval,interval, mpPendingIntent);
        mEdtTime.setEnabled(false);
    }
    private void onPlayMusic(){
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            mBtnPlay.setImageResource(R.drawable.play);

        }else {
            initMedia();
            mMediaPlayer.start();
            mBtnPlay.setImageResource(R.drawable.pause);
            setTimeEnd();
            updateTimeSong();
        }
    }
    private void initMedia(){
        mMediaPlayer = MediaPlayer.create(MainActivity.this,arraySong. get(mPos).getFile());

    }
    private void setTimeEnd(){
        SimpleDateFormat formatHout  = new SimpleDateFormat("mm:ss");
        mTxtTimeEnd.setText(formatHout.format(mMediaPlayer.getDuration()));
        mSeekBar.setMax(mMediaPlayer.getDuration());
    }

    private  void updateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat fomatHout = new SimpleDateFormat("mm:ss");
                mTxtTimeStart.setText(fomatHout.format(mMediaPlayer.getCurrentPosition()));
                mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onNextSongs();
                    }
                });
                handler.postDelayed(this,500);
            }
        },100);
    }
    private void createNotification(){
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(MainActivity.this);
        notiBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Bạn hẹn tắt ứng dụng trong "+ mEdtTime.getText()+"phút")
                .setContentTitle("Thông báo Hẹn giờ tắt nhạc").setAutoCancel(true);
        Notification notify = notiBuilder.build();
        NotificationManager notifyManeger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManeger.notify(1,notify);
    }

}
