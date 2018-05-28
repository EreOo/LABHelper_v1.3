package ru.odis.address.model.buttonstage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.odis.address.MainApp;
import ru.odis.address.view.AddDialogController;

import java.io.IOException;

public class ButtonNewItem {
    //кнопка новый
    public boolean showAddDialog(Stage primaryStage) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Создаём диалоговое окно Stage.
            Stage dialogStage = createNewItemStage(page, primaryStage);
            // Передаём  в контроллер.
            AddDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Stage createNewItemStage(AnchorPane page, Stage primaryStage) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Новые материалы");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.getIcons().add(
                new Image("resources/images/microscope.png"));
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        return dialogStage;
    }


}
