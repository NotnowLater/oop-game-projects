package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Hashtable;

public class SoundManager {

    String currentBGM = "NOCURRENTBGM";
    String currentSFX = "NOCURRENTSFX";

    Clip bgm;
    Clip sfx;

    Hashtable<String, String> sounds = new Hashtable<>();

    public SoundManager(){
        sounds.put("main", "sounds/bgm_main.wav");
        sounds.put("select", "sounds/bgm_select.wav");
        sounds.put("password", "sounds/bgm_password.wav");
        sounds.put("water", "sounds/bgm_water.wav");
        sounds.put("fire", "sounds/bgm_fire.wav");
        sounds.put("grass", "sounds/bgm_grass.wav");
        sounds.put("dark", "sounds/bgm_dark.wav");
        sounds.put("boss", "sounds/bgm_boss.wav");
        sounds.put("boss2", "sounds/bgm_boss2.wav");
        sounds.put("win_bgm", "sounds/bgm_win.wav");
        sounds.put("end", "sounds/bgm_end.wav");
        sounds.put("cursor", "sounds/sfx_cursor.wav");
        sounds.put("jump", "sounds/sfx_jump.wav");
        sounds.put("power_up", "sounds/sfx_power_up.wav");
        sounds.put("attack", "sounds/sfx_attack.wav");
        sounds.put("enemy_dead", "sounds/sfx_enemy_dead.wav");
        sounds.put("player_dead", "sounds/sfx_player_dead.wav");
        sounds.put("win", "sounds/sfx_win.wav");
        sounds.put("win_end", "sounds/sfx_win_end.wav");
        sounds.put("error", "sounds/sfx_error.wav");
    }

    public void loadBGM(String filePath){
        try {
            AudioInputStream a = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(filePath));
            bgm = AudioSystem.getClip();
            bgm.open(a);
            a.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadSFX(String filePath){
        try {
            AudioInputStream a = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(filePath));
            sfx = AudioSystem.getClip();
            sfx.open(a);
            a.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playBGM(String soundName){
        if (!currentBGM.equals(soundName)){
            loadBGM(sounds.get(soundName));
            currentBGM = soundName;
        }
        bgm.start();
        bgm.setFramePosition(0);
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBGM(){
        bgm.stop();
    }

    public void playSFX(String soundName){
        loadSFX(sounds.get(soundName));
        currentSFX = soundName;
        stopSFX();
        sfx.start();
    }


    public void stopSFX(){
        sfx.stop();
    }
}
