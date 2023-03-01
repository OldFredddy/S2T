package org.example;

import org.vosk.Model;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StartAutoRecognizeFor2
{
    private  Controller contr;

    StartAutoRecognizeFor2(Controller contr){
    this.contr=contr;
}

    public void startRec(Model model, int PartOfDecimal) throws UnsupportedAudioFileException, IOException {
        GetTextFromWav GTF=new GetTextFromWav(this.contr);
      // contr.ClearTextArea();
        for (int i = 0; i < Controller.WaveFilesAbsPath.size(); i++) {

            CreateTxtWithText(GTF.start(Paths.get(Controller.WaveFilesAbsPath.get(i)),4096,model),String.valueOf(Controller.WaveFilesAbsPath.get(i)));
        }
    }
    public static void CreateTxtWithText(List<String> temp, String nameFile) {
        try {
            for (int i = 0; i < temp.size(); i++) {
                temp.set(i,temp.get(i).substring(temp.get(i).indexOf(":"),temp.get(i).length()));
                String temp2="";
                temp2+='"';
                temp.set(i,temp.get(i).replace(temp2,""));
                temp.set(i,temp.get(i).replace("}",""));
                temp.set(i,temp.get(i).replace(":",""));
            }
            Path name = Paths.get(nameFile);
            nameFile=name.getFileName().toString();
            FileOutputStream fos = new FileOutputStream(Controller.TxtDirPath+"/"+nameFile+".txt");
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw);
            for (int i = 0; i < temp.size(); i++) {
                if(temp.get(i).length()>0){
                    bw.write(temp.get(i)+"\n");}
            }
            bw.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }
    }
}
