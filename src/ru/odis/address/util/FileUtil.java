package ru.odis.address.util;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ru.odis.address.MainApp;
import ru.odis.address.model.Analyzer;
import ru.odis.address.model.ListWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.prefs.Preferences;

public class FileUtil {

    // Если preference не был найден, то возвращается null.
    public File getFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
     * в реестре, специфичном для конкретной операционной системы.
     *
     * @param file - файл или null, чтобы удалить путь
     */
    public void setFilePath(File file, Stage primaryStage) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            // Обновление заглавия сцены.
            primaryStage.setTitle("LABHelper v1.3 - " + file.getName());
        } else {
            prefs.remove("filePath");
            // Обновление заглавия сцены.
            primaryStage.setTitle("LABHelper v1.3");
        }
    }

    /**
     * Загружает информацию  из указанного файла.
     *
     * @param file
     */
    public void loadPersonDataFromFile(File file, Stage primaryStage, ObservableList<Analyzer> labItems) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            ListWrapper wrapper = (ListWrapper) um.unmarshal(file);

            labItems.addAll(wrapper.getAnalyzers());
            // Сохраняем путь к файлу в реестре.
            new FileUtil().setFilePath(file, primaryStage);
        } catch (Exception e) {
            // Ловим ошибки
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Внимание");
            alert.setHeaderText(null);
            alert.initOwner(primaryStage);
            alert.setContentText("Невозможно загрузить таблицу!");
            alert.showAndWait();
        }
    }

    /**
     * Сохраняет текущую информацию в указанном файле.
     *
     * @param file
     */
    public void savePersonDataToFile(File file, Stage primaryStage, ObservableList<Analyzer> labItems ) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Обёртываем наши данные
            ListWrapper wrapper = new ListWrapper();
            ((ListWrapper) wrapper).setAnalyzers(labItems);
            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            new FileUtil().setFilePath(file, primaryStage);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.initOwner(primaryStage);
            alert.setContentText("");
            alert.showAndWait();
        }
    }
}
