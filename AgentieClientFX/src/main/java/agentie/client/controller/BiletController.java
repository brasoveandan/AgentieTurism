package agentie.client.controller;

import agentie.model.Zbor;
import agentie.services.AgentieException;
import agentie.services.IAgentieServices;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BiletController {
    @FXML
    AnchorPane biletLayout;
    @FXML
    TextField textFieldDestinatia;
    @FXML
    TextField textFieldData;
    @FXML
    TextField textFieldOra;
    @FXML
    TextField textFieldID;
    @FXML
    TextField textFieldNumeClient;
    @FXML
    TextField textFieldNumeTuristi;
    @FXML
    TextField textFieldAdresaClient;
    @FXML
    TextField textFieldNumarLocuri;
    private IAgentieServices service;
    private SessionController sessionController;

    private String idZbor;
    private String destinatia;
    private String data;
    private String ora;

    public void setService(IAgentieServices service, String idZbor, String destinatia, String data, String ora, SessionController sessionController) {
        this.sessionController = sessionController;
        this.service = service;
        this.idZbor = idZbor;
        this.destinatia = destinatia;
        this.data = data;
        this.ora = ora;
        textFieldDestinatia.setText(destinatia);
        textFieldData.setText(data);
        textFieldOra.setText(ora);
    }

    public void initialize() {
        makeFadeIn();
    }

    private void makeFadeIn() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(100));
        fadeTransition.setNode(biletLayout);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    @FXML
    public void handleOk(ActionEvent actionEvent) throws AgentieException {
        int locuri = -1;
        String id = textFieldID.getText();
        String numeClient = textFieldNumeClient.getText();
        String numeTuristi = textFieldNumeTuristi.getText();
        String adresaClient = textFieldAdresaClient.getText();
        String numarLocuri = textFieldNumarLocuri.getText();
        try {
            locuri = Integer.parseInt(numarLocuri);
        } catch (NumberFormatException exception) {
            ControllerAlert.showErrorMessage(null, "Numarul locuri trebuie sa fie intreg!");
            textFieldNumarLocuri.setText("");
            return;
        }
        service.saveBilet(id, idZbor, numeClient, adresaClient, numeTuristi, locuri);
        ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Cumparare Bilet", "Biletul a fost achizitionat cu succes!");
        loadSessionScene();
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(100));
        fadeTransition.setNode(biletLayout);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished((ActionEvent event) -> {
            loadSessionScene();
        });
        fadeTransition.play();
    }

    public void loadSessionScene() {
        try {
            sessionController.setService(service);
            Stage currentStage = (Stage) biletLayout.getScene().getWindow();
            currentStage.setScene(sessionController.getScene());
            sessionController.initialize();
        } catch (AgentieException e) {
            Logger.getLogger(SessionController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
