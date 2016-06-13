package ru.odis.address.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import ru.odis.address.MainApp;
import ru.odis.address.model.Analyzer;


//Контроллер для корневого макета. Корневой макет предоставляет базовый
//  макет приложения, содержащий строку меню и место, где будут размещены
//  остальные элементы JavaFX.

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
        mainApp.getPersonData().clear();
        mainApp.setFilePath(null);
    }

    /**
     * Открывает FileChooser, чтобы пользователь имел возможность
     * выбрать адресную книгу для загрузки.
     */
    @FXML
    private void handleOpen() {
    	 
        FileChooser fileChooser = new FileChooser();

        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалог загрузки файла
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        mainApp.getPersonData().clear();
        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }

    /**
     * Сохраняет файл в файл адресатов, который в настоящее время открыт.
     * Если файл не открыт, то отображается диалог "save as".
     */
    @FXML
    private void handleSave() {
        File personFile = mainApp.getFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
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
            mainApp.savePersonDataToFile(file);
        }
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
     
        boolean okClicked = mainApp.showAddDialog();
       
    }

}