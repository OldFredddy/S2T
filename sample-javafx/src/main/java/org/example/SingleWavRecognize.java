package org.example;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Paths;

public class SingleWavRecognize {
    private  Controller contr;
    private String PathToTempWav;
    SingleWavRecognize(Controller contr, String PathToTempWav){
        this.contr=contr;
        this.PathToTempWav=PathToTempWav;
    }
    public void Recognize() throws UnsupportedAudioFileException, IOException {
        GetTextFromWav GTF=new GetTextFromWav(this.contr);
        GTF.startSingle(Paths.get(PathToTempWav),4096,contr.model);
    }
}
