package org.example;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.vosk.Model;

import javax.imageio.IIOException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Controller {
    public  ObservableList<String> WaveFiles = FXCollections.observableArrayList();
    public static List<String> WaveFilesAbsPath=new ArrayList<String>();
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    public  Model model;
    public static List<Path> LangModelDirs;
    public static String TxtDirPath;
    public String startPath;
    public static double ProgressBarValue;
    public static WavPlayer rec;
    @FXML
    private CheckBox CheckBox1;

    @FXML
    private ComboBox<String> ComboBox1;

    @FXML
    private Button DecButton;

    @FXML
    private TextArea IsxTA;

    @FXML
    private Label LabelLang;

    @FXML
    private Label LabelPervious;

    @FXML
    private ProgressIndicator ProgressBarInd;
    @FXML
    private ProgressIndicator ProgressBarInd1;

    @FXML
    public ProgressBar ProgressBarMainStage;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuOpenDec;

    @FXML
    private Menu menuOpenFile;

    @FXML
    private Button StopButton1;
    @FXML
    private MenuItem menuOpenTable;

    @FXML
    private MenuItem menuSaveResult;

    @FXML
    private AnchorPane myPane;

    @FXML
    private ScrollPane parentPane;

    @FXML
    private SplitPane superParentPane;

    @FXML
    private VBox vbMenu;
    @FXML
    private ListView<String> ListOfFiles;

    @FXML
    void initialize() throws IOException, URISyntaxException {

        directoryChooser.setTitle("Выберите папку");
        getListWave("Basic");
        getModelsList();
        getTxtDirPath();
        //fileChooser.getExtensionFilters().clear();
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));
       CheckBox1.setOnAction(event -> {
           if (CheckBox1.isSelected()){

           } else {

           }
       });
       ComboBox1.setOnAction(event -> {
           if(!(model==null) ) {
               model.close();
           }
           Service<Void> service = new Service<Void>() {
               @Override
               protected Task<Void> createTask() {
                   return new Task<Void>() {
                       @Override
                       protected Void call() throws Exception {

                           model = new Model(LangModelDirs.get(ComboBox1.getSelectionModel().getSelectedIndex()+1).toString());
                           return null;
                       }
                   };
               }
           };
           service.setOnRunning(event1 -> {
               ProgressBarInd1.setProgress(0.5);
           });
           service.setOnSucceeded(event1 -> {
               ProgressBarInd1.setProgress(1);
                DecButton.setDisable(false);
           });
           service.start();
           ProgressBarInd1.setVisible(true);


       });
      //
       // ListOfFiles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        menuHelp.setOnAction(event -> {
          /*  try {
                String jarPath = GUIStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                File jarFile1 = new File(jarPath);
                String wavFolderPath = jarFile1.getParent()+"/spravka.doc";
                Runtime.getRuntime().exec(wavFolderPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }*/
        });
        DecButton.setOnAction(event -> {
                    if (!CheckBox1.isSelected()) {
                        Service<Void> service2 = new Service<Void>() {
                            @Override
                            protected Task<Void> createTask() {
                                return new Task<Void>() {
                                    @Override
                                    protected Void call() throws Exception {
                                        try {
                                            StartAutoRecognize SAR = new StartAutoRecognize(Controller.this);
                                            SAR.startRec(model);
                                        } catch (UnsupportedAudioFileException e) {
                                            throw new RuntimeException(e);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return null;
                                    }
                                };
                            }
                        };
                        service2.setOnRunning(event1 -> {
                            DecButton.setDisable(true);
                            // ProgressBarMainStage.setProgress(0);
                        });

                        service2.setOnSucceeded(event1 -> {
                            // service3.cancel();
                            DecButton.setDisable(false);
                            if (!(model == null)) {
                                model.close();
                            }
                            //ProgressBarMainStage.setProgress(0);
                        });
                        service2.start();
                        // service3.start();
                        StopButton1.setOnAction(event3 -> {

                        });

                    } else // тут код с двумя потоками
                    {

                        Service<Void> service3 = new Service<Void>() {
                            @Override
                            protected Task<Void> createTask() {
                                return new Task<Void>() {
                                    @Override
                                    protected Void call() throws Exception {
                                        try {
                                            StartAutoRecognize SAR = new StartAutoRecognize(Controller.this);
                                            SAR.startRec(model);
                                        } catch (UnsupportedAudioFileException e) {
                                            throw new RuntimeException(e);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return null;
                                    }
                                };
                            }
                        };
                        Service<Void> service4 = new Service<Void>() {
                            @Override
                            protected Task<Void> createTask() {
                                return new Task<Void>() {
                                    @Override
                                    protected Void call() throws Exception {
                                        try {
                                            StartAutoRecognize SAR = new StartAutoRecognize(Controller.this);
                                            SAR.startRec(model);
                                        } catch (UnsupportedAudioFileException e) {
                                            throw new RuntimeException(e);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return null;
                                    }
                                };
                            }
                        };
                    }
                }
         );


        menuOpenDec.setOnAction(event -> {
            try {
                getListWave("Custom");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            //File file = fileChooser.showOpenDialog(myPane.getScene().getWindow());
            //TxtOpen txt = new TxtOpen(file.getAbsolutePath());
           // fileChooser.setInitialDirectory(file.getPath());


        });
        menuOpenTable.setOnAction(event -> {
            fileChooser.getExtensionFilters().clear();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xls", "*.xls"));
            File file = fileChooser.showOpenDialog(myPane.getScene().getWindow());


        });

        ListOfFiles.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        rec = new WavPlayer(WaveFilesAbsPath.get(ListOfFiles.getSelectionModel().getSelectedIndex()),Controller.this);
                        Riffer rif1=new Riffer(rec.getDurationInSeconds(),rec,Controller.this);
                        rif1.CreateWindow(WaveFilesAbsPath.get(ListOfFiles.getSelectionModel().getSelectedIndex()));

                        ///rec.play();
                        rif1.primaryStage.setOnCloseRequest(c -> {
                            rec.close();
                            rec=null;
                           System.gc();
                        });
                    }
                }

            }
        });


    }

    /**
     * @param modify = {Basic, Custom}
     * @throws URISyntaxException
     */
    private void getListWave(String modify) throws URISyntaxException {
       WaveFiles.clear();
       WaveFilesAbsPath.clear();
        if(modify=="Basic") {
            String jarPath = GUIStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File jarFile1 = new File(jarPath);
            String wavFolderPath = jarFile1.getParent() + File.separator + "wav";
           File jarFile = new File(wavFolderPath);
            System.out.println("Я ТУТ");
            System.out.println("***************************************************************");
            System.out.println(wavFolderPath);
            System.out.println(jarFile.getPath());
            File[] listOfFiles = jarFile.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                WaveFiles.add(listOfFiles[i].getName());
                WaveFilesAbsPath.add(listOfFiles[i].getAbsolutePath());
             }
            }
        if(modify=="Custom") {
          File selectedDirectory = directoryChooser.showDialog(myPane.getScene().getWindow());

            File[] listOfFiles = selectedDirectory.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                WaveFiles.add(listOfFiles[i].getName());
                WaveFilesAbsPath.add(listOfFiles[i].getAbsolutePath());
            }
        }
        ListOfFiles.setItems(WaveFiles);
        }
        private void getModelsList() throws URISyntaxException, IOException {
            String jarPath = GUIStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File jarFile1 = new File(jarPath);
            String wavFolderPath = jarFile1.getParent() + File.separator + "LangModels";
            File jarFile = new File(wavFolderPath);
            System.out.println("Я ТУТ");
            System.out.println("***************************************************************");
            System.out.println(wavFolderPath);
            System.out.println(jarFile.getPath());
             LangModelDirs= Files.walk(Paths.get(wavFolderPath),1)
                     .filter(Files::isDirectory)
                     .collect(Collectors.toList());
            for (int i = 1; i < LangModelDirs.size(); i++) {
                ComboBox1.getItems().add(LangModelDirs.get(i).getFileName().toString());
               // WaveFiles.add(listOfFiles[i].getName());
                //WaveFilesAbsPath.add(listOfFiles[i].getAbsolutePath());
            }
        }
    private void getTxtDirPath() throws URISyntaxException, IOException {
        String jarPath = GUIStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        File jarFile1 = new File(jarPath);
        TxtDirPath = jarFile1.getParent() + File.separator + "txt";


    }
    public static String removeJsonAttributes(String jsonString) {
        String result = jsonString.replaceAll("[{}\":,]", "");
        result = result.replaceAll("text", "");
        return result;
    }
   public void setPB(double value){
        ProgressBarMainStage.setProgress(value/100);
        ProgressBarInd.setProgress(value/100);
   }
   public void ClearTextArea(){
        IsxTA.clear();
   }
   public void setToTextArea(String tex){
        IsxTA.appendText(removeJsonAttributes(tex));
   }

}

