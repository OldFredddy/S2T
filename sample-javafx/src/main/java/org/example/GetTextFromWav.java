package org.example;

import javafx.concurrent.Task;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class GetTextFromWav  {
    private  Controller contr;
public static double progress;
    GetTextFromWav(Controller contr){
        this.contr=contr;
    }
    public List<String> start(Path PathToFile, int BufferSize, Model model) throws IOException, UnsupportedAudioFileException {

        LibVosk.setLogLevel(LogLevel.WARNINGS);
       int SampleRate;
        File file=new File(PathToFile.toUri());
        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        SampleRate=(int)baseFormat.getSampleRate();
        Recognizer recognizer = new Recognizer(model, SampleRate);
        try (
             InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(PathToFile.toFile())));
             ) {

            List<String> temp=new ArrayList<>();
            int nbytes;
            double progress=0.000;
            int counter=1;
            byte[] b = new byte[BufferSize];
            long fileSize=(int)(Files.size(PathToFile));
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    String tempS=new String(recognizer.getResult().getBytes(),"utf8");
                    temp.add(tempS);
                    //System.out.println("FINAL = "+recognizer.getFinalResult());
                    progress=(double) (BufferSize*counter*100/fileSize);
                    contr.setPB(progress);
                    System.out.print("\r" + getProgressBar(BufferSize*counter,fileSize));
                    contr.setToTextArea(tempS);
                    counter++;
                } else {
                    //System.out.println(recognizer.getPartialResult());
                    System.out.print("\r" + getProgressBar(BufferSize*counter,fileSize));

                    progress=BufferSize*counter*100/fileSize;
                    contr.setPB(progress);
                    counter++;
                }
            }
            String tempS=new String(recognizer.getResult().getBytes(),"utf8");
            contr.setToTextArea(tempS);
            temp.add(tempS);
            System.out.println();
           return temp;
        }
    }
    public static List<String> startShort(Path PathToFile, int SampleRate, int BufferSize) throws IOException, UnsupportedAudioFileException {
        LibVosk.setLogLevel(LogLevel.WARNINGS);

        try (
             InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(PathToFile.toFile())));
            ) {
            List<String> temp=new ArrayList<>();
            int nbytes;
            int counter=1;
            byte[] b = new byte[BufferSize];
            short[] s = new short[BufferSize/2];
            long fileSize=(int)((double)(Files.size(PathToFile)));
            Recognizer recognizer = new Recognizer(new Model(), SampleRate);
            while ((nbytes = ais.read(b)) >= 0) {
                ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(s);
                if (recognizer.acceptWaveForm(s, nbytes / 2)) {

                    temp.add(new String(recognizer.getResult().getBytes(),"utf8"));
                    //System.out.println("FINAL = "+recognizer.getFinalResult());
                    System.out.print("\r" + getProgressBar(BufferSize*counter,fileSize));
                    counter++;
                } else {
                 if (recognizer.getPartialResult().length()>0){
                    //    temp.add(new String(recognizer.getPartialResult().getBytes(),"utf8"));
                    }


                    System.out.print("\r" + getProgressBar(BufferSize*counter,fileSize));
                    counter++;
                }
            }
            /*  while ((nbytes = ais.read(b)) >= 0) {
                ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(s);
                if (recognizer.acceptWaveForm(s, nbytes / 2)) {
                    System.out.println(recognizer.getResult());
                } else {
                    System.out.println(recognizer.getPartialResult());
                }
            }*/
            temp.add(new String(recognizer.getFinalResult().getBytes(),"utf8"));
            System.out.println();
            return temp;
        }
    }
    private static String getProgressBar(int current, long total) {
        int barLength = 100;
        int progress = (int) (((double) current / total) * barLength);
        StringBuilder bar = new StringBuilder();
        bar.append("[");
        for (int i = 0; i < barLength; i++) {
            if (i < progress) {
                bar.append("=");
            } else {
                bar.append(" ");
            }
        }
        bar.append("] " + (int)((double)current/total*100) + "%");
        return bar.toString();
    }

    public void startSingle(Path PathToFile, int BufferSize, Model model) throws IOException, UnsupportedAudioFileException {
        contr.ClearTextArea();
        LibVosk.setLogLevel(LogLevel.WARNINGS);
        int SampleRate;
        File file=new File(PathToFile.toUri());
        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        SampleRate=(int)baseFormat.getSampleRate();
        Recognizer recognizer = new Recognizer(model, SampleRate);
        try (
                InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(PathToFile.toFile())));
        ) {

            List<String> temp=new ArrayList<>();
            int nbytes;
            double progress=0.000;
            int counter=1;
            byte[] b = new byte[BufferSize];
            long fileSize=(int)(Files.size(PathToFile));
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    contr.setToTextArea(new String(recognizer.getResult().getBytes(),"utf8"));
                    progress=(double) (BufferSize*counter*100/fileSize);
                    contr.setPB(progress);
                    System.out.print("\r" + getProgressBar(BufferSize*counter,fileSize));
                    counter++;
                } else {
                    //System.out.println(recognizer.getPartialResult());
                    System.out.print("\r" + getProgressBar(BufferSize*counter,fileSize));
                    progress=BufferSize*counter*100/fileSize;
                    contr.setPB(progress);
                    counter++;
                }
            }
            contr.setToTextArea(new String(recognizer.getFinalResult().getBytes(),"utf8"));
            System.out.println();

        }
    }
}

