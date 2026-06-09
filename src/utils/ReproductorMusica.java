package utils;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ReproductorMusica {

    private Clip clip;

    public void reproducir(String ruta) {
        try {
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(new File(ruta));

            clip = AudioSystem.getClip();
            clip.open(audio);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            System.out.println("Error al reproducir la música.");
            e.printStackTrace();
        }
    }

    public void detener() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
