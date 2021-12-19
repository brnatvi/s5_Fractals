package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class GuiController
{
    @FXML private MenuItem menuExportConfig;
    @FXML private MenuItem menuImportConfig;
    private Controller controller = null;
    private Stage stage = null;

    @FXML
    public void initialize()
    {
        menuExportConfig.setOnAction(e -> exportConfig(e));
        menuImportConfig.setOnAction(e -> importConfig(e));
    }

    public void setFractalController(Controller c)
    {
        controller = c;
    }

    public void setStage(Stage primaryStage)
    {
        stage = primaryStage;
    }

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
}
