import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class TicTacToe {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        TicTacToe tic = new TicTacToe();
        tic.audioSound();
        new TicFrame();


    }
    public void audioSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        File file = new File("resources\\sound.wav");
        AudioInputStream audio = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

}
