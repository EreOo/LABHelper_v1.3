package ru.odis.address.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ru.odis.address.MainApp;
import ru.odis.address.util.FileUtil;
import ru.odis.address.model.buttonstage.ButtonNewItem;
import ru.odis.address.util.SaveLoadUtil;


/**
 * Контроллер для корневого макета. Корневой макет предоставляет базовый
 * макет приложения, содержащий строку меню и место, где будут размещены
 * остальные элементы JavaFX.
 */
public class RootLayoutController {

    // Ссылка на главное приложение
    private MainApp mainApp;

    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Создаёт пустую адресную книгу.
     */
    @FXML
    private void handleNew() {
        mainApp.getLabItems().clear();
        new FileUtil().setFilePath(null, mainApp.getPrimaryStage());
    }

    /**
     * Открывает FileChooser, чтобы пользователь имел возможность
     * выбрать адресную книгу для загрузки.
     */
    @FXML
    private void handleOpen() {
        new SaveLoadUtil().loadFile(mainApp);
    }

    /**
     * Сохраняет файл в файл адресатов, который в настоящее время открыт.
     * Если файл не открыт, то отображается диалог "save as".
     */
    @FXML
    private void handleSave() {
        File personFile = new FileUtil().getFilePath();
        if (personFile != null) {
            new FileUtil().savePersonDataToFile(personFile, mainApp.getPrimaryStage(), mainApp.getLabItems());
        } else {
            handleSaveAs();
        }
    }

    /**
     * Открывает FileChooser, чтобы пользователь имел возможность
     * выбрать файл, куда будут сохранены данные
     */
    @FXML
    private void handleSaveAs() {
        new SaveLoadUtil().saveFile(mainApp);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("LABHelper");
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setHeaderText("Спасибо, что установили и пользуетесь LAB Helper.");
        alert.setContentText("LAB Helper - это open source проект для оптимизации инвентарного учета в лаборатории. \n\n"
                + "Если у Вас возникли вопросы - обратитесь к руководству, которое находится в корневой папке программы.\n\n"
                + "Так же вы можете связаться с разработчиком по ел. почте LabHelperSupport@gmail.com \n\n\n"
                + "by Vladimir Shekhavtsov 2016.");
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        handleSave();
        System.exit(0);
    }

    //новая запись в таблицу
    @FXML
    private void newAnalyzer() {
        boolean okClicked = new ButtonNewItem().showAddDialog(mainApp.getPrimaryStage());
    }

}