package view;

import controller.Controller;
import interfaces.Initialisable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Duration;
import model.FractalFactory;
import model.FractalTaskAgregator;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @FXML private TextArea  taConsole;
    @FXML private TextField tfMaxIter;
    @FXML private TextField tfRe;
    @FXML private TextField tfIm;
    @FXML private Pane pnFractalView;

    private Stage                stage = null;
    private Controller           controller = null;
    private FractalFactory       factory = null;
    private FractalTaskAgregator taskAggregator = null;
    private ForkJoinPool         forkJoinPool = null;
    private DateTimeFormatter    dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private double               startDragX = 0.0;
    private double               startDragY = 0.0;
    private long                 timeStart  = 0;
   

    private enum enumDrawingState {IDLE, RUNNING};
    private enumDrawingState state = enumDrawingState.IDLE;


    //=========================== Initialisation GUI =============================
    @FXML
    public void initialize()
    {
        initEnvironment();
        display();
    }

    public void setFractalController(Controller c)
    {
        controller = c;
        factory = new FractalFactory(controller);
    }

    public void setStage(Stage primaryStage)
    {
        stage = primaryStage;
    }

    private void initEnvironment()
    {
        menuExportConfig.setOnAction         (e -> exportConfig(e));
        menuImportConfig.setOnAction         (e -> importConfig(e));
        menuSaveAsPNG.setOnAction            (e -> saveAsPNG(e));
        menuQuadratic.setOnAction            (e -> controller.setFunctionType(Initialisable.TypeFunction.QUADRATIC));
        menuCubic.setOnAction                (e -> controller.setFunctionType(Initialisable.TypeFunction.CUBIC));
        menuJulia.setOnAction                (e -> controller.setFractalType(Initialisable.TypeFractal.JULIA));
        menuMandelbrot.setOnAction           (e -> controller.setFractalType(Initialisable.TypeFractal.MANDELBROT));
        menuColorBlue.setOnAction            (e -> controller.setColorScheme(Initialisable.ColorScheme.BLUE));
        menuColorRed.setOnAction             (e -> controller.setColorScheme(Initialisable.ColorScheme.RED));
        menuColorGreen.setOnAction           (e -> controller.setColorScheme(Initialisable.ColorScheme.GREEN));
        menu1Th.setOnAction                  (e -> controller.setCountThreads(1));
        menu2Th.setOnAction                  (e -> controller.setCountThreads(2));
        menu4Th.setOnAction                  (e -> controller.setCountThreads(4));
        menu8Th.setOnAction                  (e -> controller.setCountThreads(8));
        menu16Th.setOnAction                 (e -> controller.setCountThreads(16));
        menuOptimumTh.setOnAction            (e -> controller.setCountThreads(Runtime.getRuntime().availableProcessors()));
        taConsole.textProperty().addListener (e -> taConsole.setScrollTop(Double.MAX_VALUE));

        ivFractal.setOnScroll(e -> {
            double zoomFactor = 0;
            double deltaY = e.getDeltaY();
            if ((deltaY < 0))
            {
                zoomFactor = 0.8;
            } else
            {
                zoomFactor = 1.1;
            }
            Rectangle2D.Double oldR = controller.getComplexRect();
            double x = oldR.x/zoomFactor;
            double y = oldR.y/zoomFactor;
            double w = oldR.width/zoomFactor;
            double h = oldR.height/zoomFactor;
            Rectangle2D.Double newR = new Rectangle2D.Double(x, y, w, h);
            controller.setComplexRect(newR);
            e.consume();
        });

        ivFractal.setOnMousePressed(e -> {
            startDragX = e.getX();
            startDragY = e.getY();
        });

        ivFractal.setOnMouseReleased(e -> {
            double coefX = (e.getX() - startDragX) / ivFractal.getFitWidth();
            double coefY = (e.getY() - startDragY) / ivFractal.getFitHeight();

            Rectangle2D.Double oldR = controller.getComplexRect();
            double complexOffsetX = oldR.width * coefX;
            double complexOffsetY = oldR.height * coefY;

            double x = oldR.x - complexOffsetX;
            double y = oldR.y - complexOffsetY;
            double w = oldR.width;
            double h = oldR.height;

            Rectangle2D.Double newR = new Rectangle2D.Double(x, y, w, h);
            controller.setComplexRect(newR);
            e.consume();
        });

        tfMaxIter.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
            {
                try
                {
                    int val = Integer.parseInt(tfMaxIter.getText());
                    controller.setMaxIter(val);
                } catch (NumberFormatException ex)
                {
                    taConsole.appendText("Wrong input: unexpected value of max iterations\n");
                }
            }
        });

        tfRe.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
            {
                try
                {
                    double val = Double.parseDouble(tfRe.getText());
                    controller.setNewRealPart(val);
                } catch (NumberFormatException ex)
                {
                    taConsole.appendText("Wrong input: unexpected value of real part of constant\n");
                }
            }
        });

        tfIm.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
            {
                try
                {
                    double val = Double.parseDouble(tfIm.getText());
                    controller.setNewImagPart(val);
                } catch (NumberFormatException ex)
                {
                    taConsole.appendText("Wrong input: unexpected value of imaginary part of constant\n");
                }
            }
        });
    }

    //============================ Thread management ==============================

    private void display()
    {
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(100), ev ->
        {
            if (state == enumDrawingState.IDLE)
            {
                updateSize();

                if(controller.isControllerChanged())
                {
                    taConsole.appendText(dtf.format(LocalDateTime.now()) +
                            " Start fractal calculation with new parameters\n");
                    timeStart = System.currentTimeMillis();
                    taskAggregator = new FractalTaskAgregator(factory, controller);
                    forkJoinPool  = new ForkJoinPool(controller.getCountThreads(),
                                                     ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                                              null,
                                            true);
                    forkJoinPool.execute(taskAggregator);

                    state = enumDrawingState.RUNNING;
                }
            }
            if (state == enumDrawingState.RUNNING)
            {
                updateSize();

                if(controller.isControllerChanged())
                {
                    taConsole.appendText("\n" + dtf.format(LocalDateTime.now()) + " aborting current calculation\n");
                    controller.setAbort(true);
                    taskAggregator.join();
                    controller.setAbort(false);

                    timeStart = System.currentTimeMillis();
                    taConsole.appendText(dtf.format(LocalDateTime.now()) +
                            " Restarting\n");
                    taskAggregator = new FractalTaskAgregator(factory, controller);
                    forkJoinPool = new ForkJoinPool(controller.getCountThreads(),
                                                    ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                                             null,
                                           true);
                    forkJoinPool.execute(taskAggregator);
                }
                else
                {
                    if (forkJoinPool.isQuiescent())
                    {
                        taConsole.appendText(dtf.format(LocalDateTime.now()) +
                                " Calculation is finished (processing time " + String.valueOf(System.currentTimeMillis() - timeStart) + " ms)\n");
                        int[] result = taskAggregator.join();

                        int width = taskAggregator.getWidth();
                        int height = result.length / width;

                        WritableImage img = new WritableImage(width, height);
                        PixelWriter pw = img.getPixelWriter();
                        pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), result, 0, width);
                        ivFractal.setFitWidth(img.getWidth());
                        ivFractal.setFitHeight(img.getHeight());
                        ivFractal.setImage(img);

                        state = enumDrawingState.IDLE;
                    }
                    else
                    {
                        taConsole.appendText(".\n");
                    }
                }
            }
        }));

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    //============================ Menu's functions ==============================

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
        setMenusAndFields();
    }

    private void saveAsPNG(ActionEvent e)
    {

    }

    //============================ Auxiliary functions ==============================

    private void setMenusAndFields()
    {
        tfMaxIter.setText(Integer.toString(controller.getMaxIter()));
        tfIm.setText(Double.toString(controller.getIm()));
        tfRe.setText(Double.toString(controller.getRe()));
        switch (controller.getFunctionType())
        {
            case QUADRATIC:
            {
                menuQuadratic.setSelected(true);
                break;
            }
            case CUBIC:
            {
                menuCubic.setSelected(true);
                break;
            }
        }

        switch (controller.getTypeFractal())
        {
            case JULIA:
            {
                menuJulia.setSelected(true);
                break;
            }
            case MANDELBROT:
            {
                menuMandelbrot.setSelected(true);
                break;
            }
        }

        switch (controller.getColorScheme())
        {
            case BLUE:
            {
                menuColorBlue.setSelected(true);
                break;
            }
            case RED:
            {
                menuColorBlue.setSelected(true);
                break;
            }
            case GREEN:
            {
                menuColorGreen.setSelected(true);
                break;
            }
        }

        switch (controller.getCountThreads())
        {
            case 1:
            {
                menu1Th.setSelected(true);
                break;
            }
            case 2:
            {
                menu2Th.setSelected(true);
                break;
            }
            case 4:
            {
                menu4Th.setSelected(true);
                break;
            }
            case 8:
            {
                menu8Th.setSelected(true);
                break;
            }
            case 16:
            {
                menu16Th.setSelected(true);
                break;
            }
            default:
            {
                menuOptimumTh.setSelected(true);
                break;
            }
        }
    }

    private void updateSize()
    {
        if (       (((int) pnFractalView.getHeight()) != controller.getHeightPNG())
                || (((int) pnFractalView.getWidth()) != controller.getWidthPNG()) )
        {
            controller.setSize((int) pnFractalView.getWidth(), (int) pnFractalView.getHeight());
        }
    }
}
