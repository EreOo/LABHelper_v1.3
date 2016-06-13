package ru.odis.address;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.odis.address.model.Analyzer;
import ru.odis.address.model.ListWrapper;
import ru.odis.address.view.AddDialogController;
import ru.odis.address.view.AnalyzerOverviewController;
import ru.odis.address.view.EditDialogController;
import ru.odis.address.view.RootLayoutController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    FileChannel channel;
  
    

    // Список расходников.
     
    private static ObservableList<Analyzer> aData = FXCollections.observableArrayList();
    
    public static ObservableList<Analyzer> getData(){
    	return aData;
    }

    // Инициализирует корневой макет.
     
    @Override
	public void stop() throws Exception {
	
    	channel.close();
    	try{
   	File personFile = this.getFilePath();
       if (personFile != null) {
            this.savePersonDataToFile(personFile);
        } }catch(Exception e){
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
        File file = getFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    //Показывает в корневом макете сведения.
     
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


    //Конструктор
    
    public MainApp() {
//        // В качестве образца добавляем некоторые данные
//        aData.add(new Analyzer("WalkWay", "NBC41"));
//        aData.add(new Analyzer("WalkWay", "PBC20"));
//        aData.add(new Analyzer("SensiTitre", "GNID"));
//        aData.add(new Analyzer("Crystal", "NH"));
//        aData.add(new Analyzer("WalkWay", "PC29"));
//    aData.add(new Analyzer("SensiTitre", "GPID"));
//       aData.add(new Analyzer("SensiTitre", "GP4D"));
//        aData.add(new Analyzer("Crystal", "NG"));
//        aData.add(new Analyzer("Crystal", "PG"));
//        aData.add(new Analyzer("WalkWay", "RYID"));
//        aData.add(new Analyzer("SensiTitre", "GP4D"));
//aData.add(new Analyzer("SensiTitre", "GNID"));
        
    }

    //Возвращает данные в виде наблюдаемого списка.
     
    public ObservableList<Analyzer> getPersonData() {
        return aData;
    }
    
    
    //кнопка новый
    public  boolean showAddDialog() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Новые материалы");
            

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(
					new Image("resources/images/microscope.png"));
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            


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
    
    // кнопка "изменить"
    public boolean showEditDialog(Analyzer a) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EditDialog.fxml"));
            
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Изменение");
            dialogStage.getIcons().add(
					new Image("resources/images/microscope.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

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
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("LABHelper v1.3");
        
        this.primaryStage.getIcons().add(new Image("resources/images/microscope.png"));

        initRootLayout();

        showMaterialOverview();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
    // Если preference не был найден, то возвращается null.
    
    public  File getFilePath() {
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
    public void setFilePath(File file) {
    	
    	
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
    public void loadPersonDataFromFile(File file) {
    	
    	
        try {
        	
        	
      
        	 channel = new RandomAccessFile(file, "rw").getChannel();

           
             
             
        	JAXBContext context = JAXBContext
                    .newInstance(ListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            ListWrapper wrapper = (ListWrapper) um.unmarshal(file);

           
            aData.addAll(wrapper.getAnalyzers());

            // Сохраняем путь к файлу в реестре.
            setFilePath(file);
            
            channel.lock();
           

        } catch (Exception e) {
        	// Ловим ошибки
            Alert alert = new Alert(AlertType.ERROR);
          
            alert.setTitle("Внимание");
            alert.setHeaderText(null);
            alert.initOwner(primaryStage);
            alert.setContentText("Таблиц открыта другим пользователем. \nЗакройте приложение и дождитесь окончания сеанса.");

            alert.showAndWait();
        }
    }

    /**
     * Сохраняет текущую информацию в указанном файле.
     * 
     * @param file
     */
    public void savePersonDataToFile(File file) {

        try {
        	
            JAXBContext context = JAXBContext
                    .newInstance(ListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные 
            ListWrapper wrapper = new ListWrapper();
            ((ListWrapper) wrapper).setAnalyzers(aData);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            setFilePath(file);
            
        } catch (Exception e) { 
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.initOwner(primaryStage);
            alert.setContentText("");

            alert.showAndWait();
        }
    }
}
