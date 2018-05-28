package ru.odis.address;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.odis.address.model.Analyzer;
import ru.odis.address.util.FileUtil;
import ru.odis.address.view.AnalyzerOverviewController;
import ru.odis.address.view.RootLayoutController;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    // Список расходников.
    private static ObservableList<Analyzer> labItems = FXCollections.observableArrayList();

    public static ObservableList<Analyzer> getData() {
        return labItems;
    }

    //Конструктор
    public MainApp() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("LABHelper v1.3");
        this.primaryStage.getIcons().add(new Image("resources/images/microscope.png"));
        initRootLayout();
        showMaterialOverview();
    }

    // Инициализирует корневой макет.
    @Override
    public void stop() throws Exception {
        try {
            File file = new FileUtil().getFilePath();
            if (file != null) {
                new FileUtil().savePersonDataToFile(file, primaryStage, labItems);
            }
        } catch (Exception e) {
            //ничего не делать
        }
    }

    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному прилодению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Пытается загрузить последний открытый файл с адресатами.
        File file = new FileUtil().getFilePath();
        if (file != null) {
            new FileUtil().loadPersonDataFromFile(file, primaryStage, labItems);
        }
    }

    // Показывает в корневом макете сведения.
    public void showMaterialOverview() {
        try {
            // Загружаем сведения об анализатарах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MaterialOverview.fxml"));
            AnchorPane materialOverview = (AnchorPane) loader.load();
            // Помещаем сведения об анализатарах в центр корневого макета.
            rootLayout.setCenter(materialOverview);
            // Даём контроллеру доступ к главному приложению.
            AnalyzerOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Возвращает главную сцену.
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    // Возвращает данные в виде наблюдаемого списка.
    public ObservableList<Analyzer> getLabItems() {
        return labItems;
    }
}
