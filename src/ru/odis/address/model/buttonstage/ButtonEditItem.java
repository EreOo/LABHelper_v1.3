package ru.odis.address.model.buttonstage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.odis.address.MainApp;
import ru.odis.address.model.Analyzer;
import ru.odis.address.view.EditDialogController;

import java.io.IOException;

public class ButtonEditItem {
    // кнопка "изменить"
    public boolean showEditDialog(Analyzer a, Stage primaryStage) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Создаём диалоговое окно Stage.
            Stage dialogStage = createEditItemStage(page, primaryStage);
            // Передаём анализатор
            EditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAnalyzerE(a);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Stage createEditItemStage(AnchorPane page, Stage primaryStage) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Изменение");
        dialogStage.getIcons().add(
                new Image("resources/images/microscope.png"));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        return dialogStage;
    }
}
