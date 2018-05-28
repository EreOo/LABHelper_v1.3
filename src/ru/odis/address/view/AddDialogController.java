package ru.odis.address.view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import ru.odis.address.MainApp;
import ru.odis.address.model.Analyzer;

public class AddDialogController {


    @FXML
    private TextField analyzerName;
    @FXML
    private TextField materialName;
    @FXML
    private TextField idMaterial;
    @FXML
    private TextField countBox;
    @FXML
    private TextField countINTOBox;
    @FXML
    private DatePicker exp;
    @FXML
    private DatePicker addDate;
    @FXML
    private ChoiceBox<String> typeMaterial;

    //список для чойзбокса
    private ArrayList<String> type = new ArrayList<>();


    @FXML
    Button add;
    @FXML
    Button close;


    private Stage dialogStage;
    private Analyzer analyzer;
    private boolean okClicked = false;


    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        type.add("Тест система");
        type.add("Реагенты");
        type.add("Другое");
        typeMaterial.setItems(FXCollections.observableArrayList(type));

        this.dialogStage = dialogStage;

    }
    
    /**
     * Mетод созданния новых данных
     */
    @FXML
    public void newAnalyzer() {


        //проверка заполнения данных
        if (analyzerName.getText().equals("") ||
                materialName.getText().equals("") ||
                countBox.getText().equals("") ||
                exp.getValue() == null ||
                typeMaterial.getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(dialogStage.getOwner());
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните корректно все поля с красной звездочкой.");
            alert.showAndWait();
        } else {
            //проверка на данные в строках количество, должны быть цифры иначе ловим ошибку.
            try {
                MainApp.getData().add(new Analyzer(analyzerName.getText(),
                        typeMaterial.getValue(),
                        materialName.getText(),
                        idMaterial.getText(),
                        Integer.parseInt(countBox.getText()),
                        Integer.parseInt(countINTOBox.getText()),
                        exp.getValue(),
                        addDate.getValue()));

                okClicked = true;
                dialogStage.close();

            } catch (NumberFormatException ex) {
                //не число!
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(dialogStage.getOwner());
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("В строку \"количество\" внесены недопустимые символы! ");
                alert.showAndWait();
            }
        }

    }


    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void cancel() {
        dialogStage.close();
    }

    public boolean isOkClicked() {

        return okClicked;
    }

}
