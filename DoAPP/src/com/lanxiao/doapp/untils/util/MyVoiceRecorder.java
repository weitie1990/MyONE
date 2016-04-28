package com.lanxiao.doapp.untils.util;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;

import com.czt.mp3recorder.MP3Recorder;
import com.easemob.EMError;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Thinkpad on 2016/2/24.
 */
public class MyVoiceRecorder {
    static final String PREFIX = "voice";
    static final String EXTENSION = ".mp3";
    private boolean isRecording = false;
    private long startTime;
    File isMy;
    private Handler handler;
    private MP3Recorder mRecorder;
    String Mp3voiceFileName=null;
    public MyVoiceRecorder(Handler var1) {
        this.handler = var1;
    }
    Context context;
    public String startRecording(String var1, String var2, Context var3) {
        this.context=var3;
        try {

            Mp3voiceFileName=getMp3voiceFileName(var2);
            isMy=new File(getMp3VoiceFilePath());
            this.isRecording = true;
            mRecorder=new MP3Recorder(isMy);
            mRecorder.start();
        } catch (IOException var5) {
            EMLog.e("voice", "prepare() failed");
        }

        (new Thread(new Runnable() {
            public void run() {
                while(isRecording) {
                    try {
                            Message var1 = new Message();
                            var1.what = mRecorder.getVolume() * 13 / 0x7FFF;
                            MyVoiceRecorder.this.handler.sendMessage(var1);
                            LogUtils.i("volume:"+var1.what);
                            SystemClock.sleep(100L);
                    } catch (Exception var2) {
                        EMLog.e("voice", var2.toString());
                        return;
                    }

                }
            }
        })).start();
        this.startTime = (new Date()).getTime();
        return this.isMy == null?null:this.isMy.getAbsolutePath();
    }

    public void discardRecording() {
        if(mRecorder!=null) {
            mRecorder.stop();
        }
        this.isRecording = false;
    }

    public int stopRecoding() {
        this.isRecording = false;
        if(this.mRecorder != null) {
            this.mRecorder.stop();
            this.mRecorder = null;

            if( isMy== null || !isMy.exists() || !isMy.isFile()){
                LogUtils.i("1");
                return EMError.INVALID_FILE;
            }
            if (isMy.length() == 0) {
                isMy.delete();
                LogUtils.i("2");
                return 0;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            EMLog.d("voice", "voice recording finished. seconds:" + seconds + " file length:" + isMy.length());
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();


    }


    public String getMp3voiceFileName(String var1) {
        Time var2 = new Time();
        var2.setToNow();
        return var1 + var2.toString().substring(0, 15)+"encodemp3" + ".mp3";
    }
    public boolean isRecording() {
        return this.isRecording;
    }


    public String getMp3VoiceFilePath() {
        return Utils.getExternalCacheDir(context)+Mp3voiceFileName;
    }
}
