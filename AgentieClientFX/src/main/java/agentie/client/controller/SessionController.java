package agentie.client.controller;

import agentie.model.Angajat;
import agentie.model.Bilet;
import agentie.model.Zbor;
import agentie.services.AgentieException;
import agentie.services.IAgentieObserver;
import agentie.services.IAgentieServices;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class SessionController extends UnicastRemoteObject implements IAgentieObserver, Serializable {
    ObservableList<Zbor> modelZbor = FXCollections.observableArrayList();
    ObservableList<Zbor> modelZborCautare = FXCollections.observableArrayList();
    @FXML
    AnchorPane sessionLayout;
    @FXML
    TableView<Zbor> tableViewZbor;
    @FXML
    TableColumn<Zbor, String> tableColumnID;
    @FXML
    TableColumn<Zbor, String> tableColumnDestinatia;
    @FXML
    TableColumn<Zbor, String> tableColumnData;
    @FXML
    TableColumn<Zbor, String> tableColumnOra;
    @FXML
    TableColumn<Zbor, String> tableColumnAeroport;
    @FXML
    TableColumn<Zbor, String> tableColumnNumarLocuri;
    @FXML
    TableView<Zbor> tableViewZborCautare;
    @FXML
    TableColumn<Zbor, String> tableColumnID2;
    @FXML
    TableColumn<Zbor, String> tableColumnDestinatia2;
    @FXML
    TableColumn<Zbor, String> tableColumnData2;
    @FXML
    TableColumn<Zbor, String> tableColumnOra2;
    @FXML
    TableColumn<Zbor, String> tableColumnAeroport2;
    @FXML
    TableColumn<Zbor, String> tableColumnNumarLocuri2;
    @FXML
    TextField textFieldDestinatia;
    @FXML
    TextField textFieldDataPlecare;

    private IAgentieServices service;
    private Angajat angajat;

    public SessionController() throws RemoteException {
    }

    public void setService(IAgentieServices service) throws AgentieException {
        this.service = service;
        modelZbor.setAll(getZborList());
    }

    public Scene getScene() {
        return sessionLayout.getScene();
    }

    public void setUtils(Angajat angajat, AnchorPane sessionLayout) {
        this.angajat = angajat;
        this.sessionLayout = sessionLayout;
    }

    private List<Zbor> getZborList() throws AgentieException {
        Iterable<Zbor> zboruri = Arrays.asList(service.findAllZbor());
        List<Zbor> listaZboruri = StreamSupport.stream(zboruri.spliterator(), false)
                .filter(zbor -> zbor.getNrLocuri() != 0)
                .collect(Collectors.toList());
        return listaZboruri;
    }

    @FXML
    public void initialize() {
        makeFadeIn();
        tableColumnID.setCellValueFactory(new PropertyValueFactory("id"));
        tableColumnDestinatia.setCellValueFactory(new PropertyValueFactory("destinatia"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory("data"));
        tableColumnOra.setCellValueFactory(new PropertyValueFactory("ora"));
        tableColumnAeroport.setCellValueFactory(new PropertyValueFactory("aeroport"));
        tableColumnNumarLocuri.setCellValueFactory(new PropertyValueFactory("nrLocuri"));
        tableViewZbor.setItems(modelZbor);

        tableColumnID2.setCellValueFactory(new PropertyValueFactory("id"));
        tableColumnDestinatia2.setCellValueFactory(new PropertyValueFactory("destinatia"));
        tableColumnData2.setCellValueFactory(new PropertyValueFactory("data"));
        tableColumnOra2.setCellValueFactory(new PropertyValueFactory("ora"));
        tableColumnAeroport2.setCellValueFactory(new PropertyValueFactory("aeroport"));
        tableColumnNumarLocuri2.setCellValueFactory(new PropertyValueFactory("nrLocuri"));
        tableViewZborCautare.setItems(modelZborCautare);
    }

    public void makeFadeIn() {
        Platform.runLater(()-> {
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(100));
            fadeTransition.setNode(sessionLayout);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
        });
    }

    private void loadBiletScene(String idZbor, String destinatia, String data, String ora, SessionController sessionController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/biletView.fxml"));
            AnchorPane biletView = loader.load();
            BiletController biletController = loader.getController();
            biletController.setService(service, idZbor, destinatia, data, ora, sessionController);
            Scene newScene = new Scene(biletView);
            Stage currentStage = (Stage) sessionLayout.getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            Logger.getLogger(SessionController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void handleCumparare(ActionEvent actionEvent) {
        Zbor zbor = tableViewZbor.getSelectionModel().getSelectedItem();
        if (zbor != null) {
            if (zbor.getNrLocuri() > 0) {
                FadeTransition fadeTransition = new FadeTransition();
                fadeTransition.setDuration(Duration.millis(100));
                fadeTransition.setNode(sessionLayout);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                fadeTransition.setOnFinished((ActionEvent event) -> {
                    loadBiletScene(zbor.getId(), zbor.getDestinatia(), zbor.getDataString(), zbor.getOra().toString(), this);
                });
                fadeTransition.play();
            } else ControllerAlert.showErrorMessage(null, "Nu mai sunt locuri disponibile pentru acest zbor!");
        } else ControllerAlert.showErrorMessage(null, "Mai intai selectati un zbor!");
    }

    private void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/loginView.fxml"));
            AnchorPane loginView = loader.load();
            LoginController loginController = loader.getController();
            loginController.setService(service);
            loginController.setSessionController(this, sessionLayout);
            Scene newScene = new Scene(loginView);
            Stage currentStage = (Stage) sessionLayout.getScene().getWindow();
            loginController.setSessionScene(sessionLayout.getScene());
            currentStage.setTitle("AgentieTurism");
            currentStage.setScene(newScene);
        } catch (IOException e) {
            Logger.getLogger(SessionController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void handleLogout(ActionEvent actionEvent) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(100));
        fadeTransition.setNode(sessionLayout);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished((ActionEvent event) -> {
            try {
                service.logout(angajat, this);
                loadLoginScene();
            } catch (AgentieException e) {
                e.printStackTrace();
            }
        });
        fadeTransition.play();
    }

    public void handleCautare(ActionEvent actionEvent) throws AgentieException {
        Predicate<Zbor> p1 = zbor -> zbor.getDestinatia().startsWith(textFieldDestinatia.getText());
        Predicate<Zbor> p2 = zbor -> zbor.getData().toString().equals(textFieldDataPlecare.getText());
        modelZborCautare.setAll(getZborList()
                .stream()
                .filter(p1.and(p2))
                .collect(Collectors.toList()));
        if (modelZborCautare.isEmpty())
            ControllerAlert.showErrorMessage(null, "Nu exista zboruri!");
        else {
            textFieldDestinatia.setText("");
            textFieldDataPlecare.setText("");
        }
    }

    @Override
    public void cumparareBilet(Zbor[] zbors) {
        Platform.runLater(()->{
            System.out.println("Apel metoda Observer");
            tableViewZbor.setItems(modelZbor);
            modelZbor.clear();
            modelZbor.setAll(zbors);
            tableViewZbor.refresh();
        });
    }
}
