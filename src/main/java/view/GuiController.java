package view;

import controller.Controller;
import interfaces.Initialisable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Duration;
import model.Complex;
import model.FractalFactory;
import model.FractalTaskAgregator;


import java.io.File;
import java.util.concurrent.ForkJoinPool;


public class GuiController
{
    @FXML private MenuItem menuExportConfig;
    @FXML private MenuItem menuImportConfig;
    @FXML private MenuItem menuSaveAsPNG;
    @FXML private RadioMenuItem menuQuadratic;
    @FXML private RadioMenuItem menuCubic;
    @FXML private RadioMenuItem menuJulia;
    @FXML private RadioMenuItem menuMandelbrot;
    @FXML private RadioMenuItem menuColorBlue;
    @FXML private RadioMenuItem menuColorRed;
    @FXML private RadioMenuItem menuColorGreen;
    @FXML private RadioMenuItem menu1Th;
    @FXML private RadioMenuItem menu2Th;
    @FXML private RadioMenuItem menu4Th;
    @FXML private RadioMenuItem menu8Th;
    @FXML private RadioMenuItem menu16Th;
    @FXML private RadioMenuItem menuOptimumTh;
    @FXML private ImageView ivFractal;
    @FXML private TextArea taConsole;
    @FXML private TextField taMaxIter;
    @FXML private TextField taRe;
    @FXML private TextField taIm;

    private Controller controller = null;
    private FractalFactory factory = null;
    private FractalTaskAgregator taskAgregator = null;
    private ForkJoinPool forkJoinPool = null;


    private Stage stage = null;
    private enum enumDrawingState {IDLE, RUNING};
    private enumDrawingState state = enumDrawingState.IDLE;

    @FXML
    public void initialize()
    {
        initMenu();
        display();
    }


    public void setFractalController(Controller c)
    {
        controller = c;
        factory = new FractalFactory(controller);
    }

    public void setStage(Stage primaryStage) { stage = primaryStage; }


    private void exportConfig(ActionEvent e)
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fractal config files (*.fct)", "*.fct");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null)
        {
            if (!controller.exportSettings(file.getPath()))
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Can't write file ");
                errorAlert.setContentText(file.getPath());
                errorAlert.showAndWait();
            }
        }
    }

    private void importConfig(ActionEvent e)
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fractal config files (*.fct)", "*.fct");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            if (!controller.importSettings(file.getPath()))
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Can't write file ");
                errorAlert.setContentText(file.getPath());
                errorAlert.showAndWait();
            }
        }
    }

    private void saveAsPNG(ActionEvent e)
    {

    }

    private void initMenu()
    {
        menuExportConfig.setOnAction(e -> exportConfig(e));
        menuImportConfig.setOnAction(e -> importConfig(e));
        menuSaveAsPNG.setOnAction   (e -> saveAsPNG(e));
        menuQuadratic.setOnAction   (e -> controller.setFunctionType(Initialisable.TypeFunction.QUADRATIC));
        menuCubic.setOnAction       (e -> controller.setFunctionType(Initialisable.TypeFunction.CUBIC));
        menuJulia.setOnAction       (e -> controller.setFractalType(Initialisable.TypeFractal.JULIA));
        menuMandelbrot.setOnAction  (e -> controller.setFractalType(Initialisable.TypeFractal.MANDELBROT));
        menuColorBlue.setOnAction   (e -> controller.setColorScheme(Initialisable.ColorScheme.BLUE));
        menuColorRed.setOnAction    (e -> controller.setColorScheme(Initialisable.ColorScheme.RED));
        menuColorGreen.setOnAction  (e -> controller.setColorScheme(Initialisable.ColorScheme.GREEN));
        menu1Th.setOnAction         (e -> controller.setCountThreads(1));
        menu2Th.setOnAction         (e -> controller.setCountThreads(2));
        menu4Th.setOnAction         (e -> controller.setCountThreads(4));
        menu8Th.setOnAction         (e -> controller.setCountThreads(8));
        menu16Th.setOnAction        (e -> controller.setCountThreads(16));
        menuOptimumTh.setOnAction   (e -> controller.setCountThreads(Runtime.getRuntime().availableProcessors()));

        taMaxIter.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    try
                    {
                        int val = Integer.parseInt(taMaxIter.getText());
                        controller.setMaxIter(val);
                    } catch (NumberFormatException e)
                    {
                        displayError("Wrong input: unexpected value of max iterations\n");
                        //System.exit(-1);
                    }
                }
            }

        });

        taRe.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    try
                    {
                        double val = Double.parseDouble(taRe.getText());
                        controller.setNewRealPart(val);
                    } catch (NumberFormatException e)
                    {
                        displayError("Wrong input: unexpected value of real part of constant\n");
                        //System.exit(-1);
                    }
                }
            }
        });

        taIm.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    try
                    {
                        double val = Double.parseDouble(taIm.getText());
                        controller.setNewImagPart(val);
                    } catch (NumberFormatException e)
                    {
                        displayError("Wrong input: unexpected value of imaginary part of constant\n");
                        //System.exit(-1);
                    }
                }
            }
        });
                 

        taConsole.textProperty().addListener(new ChangeListener<Object>()
        {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue)
            {
                taConsole.setScrollTop(Double.MAX_VALUE);
            }
        });
    }

    private void displayError(String str)
    {
        taConsole.appendText(str);
    }

    private void display()
    {
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(100), ev ->
        {
            if (state == enumDrawingState.IDLE)
            {
                if(controller.isControllerChanged())
                {
                    taConsole.appendText("start");
                    taskAgregator = new FractalTaskAgregator(factory);
                    forkJoinPool  = new ForkJoinPool(controller.getCountThreads(),
                                                     ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                                              null,
                                            true);
                    forkJoinPool.execute(taskAgregator);

                    state = enumDrawingState.RUNING;
                }
            }
            if (state == enumDrawingState.RUNING)
            {
                if(controller.isControllerChanged())
                {
                    taConsole.appendText("aborting\n");
                    controller.setAbort(true);
                    taskAgregator.join();
                    controller.setAbort(false);

                    taConsole.appendText("restart");
                    taskAgregator = new FractalTaskAgregator(factory);
                    forkJoinPool = new ForkJoinPool(controller.getCountThreads(),
                                                    ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                                             null,
                                           true);
                    forkJoinPool.execute(taskAgregator);
                }
                else
                {
                    if (forkJoinPool.isQuiescent())
                    {
                        taConsole.appendText("finished\n");
                        int[] result = taskAgregator.join();
                        
                        WritableImage img = new WritableImage(controller.getWidthPNG(), controller.getHeightPNG());
                        PixelWriter pw = img.getPixelWriter();
                        pw.setPixels(0, 0, controller.getWidthPNG(), controller.getHeightPNG(), PixelFormat.getIntArgbInstance(), result, 0, controller.getWidthPNG());
                        ivFractal.setFitWidth(img.getWidth());
                        ivFractal.setFitHeight(img.getHeight());
                        ivFractal.setImage(img);

                        state = enumDrawingState.IDLE;
                    }
                    else
                    {
                        taConsole.appendText(".");
                    }
                }
            }
        }));

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void stopTimeLine(Timeline t)
    {
        if (null != t)
        {
            t.stop();
            t = null;
        }
    }


}
