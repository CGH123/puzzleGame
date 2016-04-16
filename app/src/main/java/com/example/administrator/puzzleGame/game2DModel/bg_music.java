package com.example.administrator.puzzleGame.game2DModel;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.administrator.puzzleGame.sqlServer.GameDB;

public class bg_music extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (mediaPlayer == null) {
            // R.raw.mmp是资源文件，MP3格式的
            switch(GameDB.musicSelect)
                {
                /*
                case 0:mediaPlayer = MediaPlayer.create(this, R.raw.tiankongzhicheng);break;
                case 1:mediaPlayer = MediaPlayer.create(this, R.raw.kanong);break;
                case 2:mediaPlayer = MediaPlayer.create(this, R.raw.loveisyou);break;
                case 3:mediaPlayer = MediaPlayer.create(this, R.raw.quanwang);break;
                case 4:mediaPlayer = MediaPlayer.create(this, R.raw.yese);break;
                case 5:mediaPlayer = MediaPlayer.create(this, R.raw.hundouluo);break;
                case 6:mediaPlayer = MediaPlayer.create(this, R.raw.huoying);break;
                case 7:mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);break;
                case 8:mediaPlayer = MediaPlayer.create(this, R.raw.yueguang);break;
                case 9:mediaPlayer = MediaPlayer.create(this, R.raw.always_with_me);break;
                case 10:mediaPlayer = MediaPlayer.create(this, R.raw.chenwu);break;
                default:mediaPlayer = MediaPlayer.create(this, R.raw.maliao);break;
                */
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {  // TODO Auto-generated method stub
        super.onDestroy();
        mediaPlayer.stop();
    }
}