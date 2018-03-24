package com.i550.beatbox;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private SoundPool mSoundPool;   //класс для работы с большими обьемами звуков (встроенный)
    private List<Sound> mSounds = new ArrayList<>();

    public BeatBox(Context context){
        mAssets = context.getAssets();  //assetMan - для работы с ассетами(конструктор для него)
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0); //есть более лучишй способ чем этот - билдер!
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);//получение списка доступных активов
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {     //пишет в лог это
            Log.e(TAG, "Could not list assets", ioe);
            return; //возвращает список имён
        }
        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);     //создается список объектов
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound" + filename, ioe);
            }
        }
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if(soundId==null){return;}
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    public void release(){
        mSoundPool.release();
    }

    private void load(Sound sound) throws IOException{
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd,1);   //загружает звук в СаундПул, возвращает идентификатор
        sound.setSoundId(soundId);      //устанавливаем этому звуку ИД
    }

    public List<Sound> getSounds(){
        return mSounds;
    }
}
