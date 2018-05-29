package ru.odis.address.view;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.odis.address.MainApp;
import ru.odis.address.model.Analyzer;
import ru.odis.address.util.FileUtil;
import ru.odis.address.model.buttonstage.ButtonEditItem;
import ru.odis.address.model.buttonstage.ButtonNewItem;
import ru.odis.address.util.DateUtil;
import ru.odis.address.util.SaveLoadUtil;

public class AnalyzerOverviewController {

    private static final String GRAY = "-fx-background-color: gray;";
    private static final String RED = "-fx-background-color: tomato;";
    private static final String ORANGE = "-fx-background-color: orange;";
    private static final String YELLOW = "-fx-background-color: khaki;";
    @FXML
    private TableView<Analyzer> analyzerTable;
    @FXML
    private TableColumn<Analyzer, String> analyzerNameColumn;
    @FXML
    private TableColumn<Analyzer, String> materialNameColumn;
    @FXML
    private TableColumn<Analyzer, Number> countBoxColumn;
    @FXML
    private TableColumn<Analyzer, LocalDate> expColumn;
    @FXML
    private Label analyzerNameLabel;
    @FXML
    private Label materialNameLabel;
    @FXML
    private Label idMaterialLabel;
    @FXML
    private Label countBoxLabel;
    @FXML
    private Label countINTOBoxLabel;
    @FXML
    private Label expLabel;
    @FXML
    private Label addDateLabel;
    @FXML
    private Label typeMaterial;
    @FXML
    private TextField filterField;
    @FXML
    private TextArea changeTime;

    public TableView<Analyzer> getAnalyzerTable() {
        return analyzerTable;
    }

    // Ссылка на главное приложение.
    private MainApp mainApp = new MainApp();

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public AnalyzerOverviewController() {
    }

    /**
     * Инициализация класса-контроллера. вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы
        //анализатор
        analyzerNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().analyzerNameProperty());
        //название материала
        materialNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().materialNameProperty());
        //кол-во коробок
        countBoxColumn.setCellValueFactory(
                cellData -> cellData.getValue().countBoxProperty());
        //срок годности
        expColumn.setCellValueFactory(
                cellData -> cellData.getValue().expProperty());
        //Выделение цветом строк
        //если просроченно или мало коробок
        analyzerTable.setRowFactory(tv -> new TableRow<Analyzer>() {
            @Override
            public void updateItem(Analyzer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getСountBox() == 0) {
                    setStyle(GRAY);
                } else if (item.getExp().isBefore(LocalDate.now())) {
                    setStyle(RED);
                } else if (item.getExp().isBefore(LocalDate.now().plus(Period.ofDays(30)))) {
                    setStyle(ORANGE);
                } else if (item.getСountBox() <= 5) {
                    setStyle(YELLOW);
                } else {
                    setStyle("");
                }
            }
        });


        // фильтр
        FilteredList<Analyzer> filteredData = new FilteredList<>(mainApp.getLabItems(), p -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(analyzer -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (analyzer.getAnalyzerName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter Analyzer name
                } else if (analyzer.getMaterialName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter Material
                }
                return false;
            });
        });

        SortedList<Analyzer> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(analyzerTable.comparatorProperty());

        //  Добавление информации в таблицу.
        analyzerTable.setItems(sortedData);


        // Очистка дополнительной информации
        showAnalyzerDetails(null);

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию
        analyzerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAnalyzerDetails(newValue));

    }

    //Вызывается главным приложением, которое даёт на себя ссылку.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Добавление в таблицу данных из наблюдаемого списка
        analyzerTable.setItems(mainApp.getLabItems());
    }

    /**
     * выводим дополнительное инфо
     **/
    private void showAnalyzerDetails(Analyzer analyzer) {
        if (analyzer != null) {
            // Заполняем метки информацией из объекта анализатор.
            analyzerNameLabel.setText(analyzer.getAnalyzerName());
            materialNameLabel.setText(analyzer.getMaterialName());
            idMaterialLabel.setText(analyzer.getIdMaterial());
            countBoxLabel.setText(Integer.toString(analyzer.getСountBox()));
            countINTOBoxLabel.setText(Integer.toString(analyzer.getСountINTOBox()));
            expLabel.setText(DateUtil.format(analyzer.getExp()));
            addDateLabel.setText(DateUtil.format(analyzer.getDateAdd()));
            typeMaterial.setText(analyzer.getTypeMaterial());
            changeTime.setText(analyzer.getChangeTime());
        } else {
            // Если analyzer = null, то убираем весь текст.
            analyzerNameLabel.setText("");
            materialNameLabel.setText("");
            idMaterialLabel.setText("");
            countBoxLabel.setText("");
            countINTOBoxLabel.setText("");
            expLabel.setText("");
            addDateLabel.setText("");
            typeMaterial.setText("");
            changeTime.clear();
        }
    }

    /**
     * Удаление анализатора (записи).
     */
    @FXML
    private void deleteAnalyzer() {
        int selectedIndex = analyzerTable.getSelectionModel().getSelectedIndex();
        //спришивает, точно ли хотят удалить?
        if (selectedIndex >= 0) {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Внимание");
            alert.setHeaderText(null);
            alert.setContentText("Вы уверенны, что хотите удалить данные?");
            //кнопки
            ButtonType yes = new ButtonType("Да");
            ButtonType no = new ButtonType("Нет");
            //добавляем кнопки в окно диалога
            alert.getButtonTypes().setAll(yes, no);
            alert.showAndWait();
            //если да - то удаляем
            if (alert.getResult() == yes) {
                //MainApp.getData().remove(analyzerTable.getSelectionModel().getFocusedIndex());
                mainApp.getLabItems().remove(analyzerTable.getSelectionModel().getSelectedItem());
                //analyzerTable.getItems().remove(analyzerTable.getSelectionModel());
            }
        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбрана запись!");
            alert.setContentText("Пожалуйста, выберите данные для удаления.");
            alert.showAndWait();
        }

    }

    //новая запись в таблицу
    @FXML
    private void newAnalyzer() {
        boolean okClicked = new ButtonNewItem().showAddDialog(mainApp.getPrimaryStage());
    }

    //редактор
    @FXML
    private void editAnalyzer() {

        Analyzer selectedA = analyzerTable.getSelectionModel().getSelectedItem();
        if (selectedA != null) {
            boolean okClicked = new ButtonEditItem().showEditDialog(selectedA, mainApp.getPrimaryStage());
        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбрана запись");
            alert.setContentText("Выберите данные для изминения в таблице.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNew() {
        mainApp.getLabItems().clear();
        new FileUtil().setFilePath(null, mainApp.getPrimaryStage());
    }

    @FXML
    private void handleOpen() {
        new SaveLoadUtil().loadFile(mainApp);
    }

    @FXML
    private void handleSave() {
        File personFile = new FileUtil().getFilePath();
        if (personFile != null) {
            new FileUtil().savePersonDataToFile(personFile, mainApp.getPrimaryStage(), mainApp.getLabItems());
        } else {
            new SaveLoadUtil().saveFile(mainApp);
        }
    }
}






