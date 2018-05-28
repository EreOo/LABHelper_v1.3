package ru.odis.address.util;

import javafx.stage.FileChooser;
import ru.odis.address.MainApp;

import java.io.File;

public class SaveLoadUtil {

    public void saveFile(MainApp mainApp){
        FileChooser fileChooser = new FileChooser();
        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        // Показываем диалог сохранения файла
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            new FileUtil().savePersonDataToFile(file, mainApp.getPrimaryStage(), mainApp.getLabItems());
        }
    }

    public void loadFile(MainApp mainApp){
        FileChooser fileChooser = new FileChooser();

        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалог загрузки файла
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        mainApp.getLabItems().clear();
        if (file != null) {
            new FileUtil().loadPersonDataFromFile(file, mainApp.getPrimaryStage(), mainApp.getLabItems());
        }
    }
}
