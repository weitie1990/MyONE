/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lanxiao.doapp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.easeui.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.util.EMLog;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;

/**
 * 语音row播放点击事件监听
 *
 */
public class VoicePlayClickListener implements View.OnClickListener {
	private static final String TAG = "VoicePlayClickListener";
	ImageView voiceIconView;

	private AnimationDrawable voiceAnimation = null;
	MediaPlayer mediaPlayer = null;
	Context activity;
	String url;
	String p;
	public static boolean isPlaying = false;
	public static VoicePlayClickListener currentPlayListener = null;
	public static String playMsgId;
	ProgressBar pb_post_voice;
	VoicePlayClickListener instance;
	SharedPreferences sp;
	public VoicePlayClickListener(String url, ImageView v, Context context,ProgressBar pb_post_voice,String p) {
		this.url = url;
		voiceIconView = v;
		this.activity = context;
		this.pb_post_voice=pb_post_voice;
		this.p=p;
		sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	public void stopPlayVoice() {
		voiceAnimation.stop();
			voiceIconView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
		// stop play voice
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
		playMsgId = null;
	}

	public void playVoice(String filePath) {
		instance=this;
		playMsgId=p;
		AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = new MediaPlayer();
		pb_post_voice.setVisibility(View.VISIBLE);
		int voiceMode=sp.getInt("voicemode", 1);
		LogUtils.i("voiceMode:"+voiceMode+"");
		if (voiceMode==1) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
			Utils.showToast("当前为听筒播放模式",activity);
		}
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					pb_post_voice.setVisibility(View.GONE);
					showAnimation();
					isPlaying = true;
					mediaPlayer.start();
					currentPlayListener = instance;
				}
			});
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mediaPlayer.release();
					mediaPlayer = null;
					stopPlayVoice(); // stop animation
				}

			});

		} catch (Exception e) {
		    System.out.println();
		}
	}

	// show the voice playing animation
	private void showAnimation() {
		// play voice, and start animation
			voiceIconView.setImageResource(R.anim.voice_from_icon);
		voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
		voiceAnimation.start();
	}

	@Override
	public void onClick(View v) {
		if (isPlaying) {
			if (playMsgId != null && playMsgId.equals(p)) {
				currentPlayListener.stopPlayVoice();
				return;
			}
			currentPlayListener.stopPlayVoice();
		}
			playVoice(url);
	}
}