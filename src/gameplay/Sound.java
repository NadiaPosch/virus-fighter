package gameplay;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * used to play sounds during game play
 */
public class Sound {

    public static final String LASER = "resources/sounds/laser.wav";
    public static final String SQUASH = "resources/sounds/squash.wav";
    public static final String GRUNT = "resources/sounds/grunt.wav";

    public static void playSound(String fileName) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(fileName)));
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException("Unable to play sound", e);
        }
    }
}
