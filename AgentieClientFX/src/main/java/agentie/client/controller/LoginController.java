package agentie.client.controller;

import agentie.model.Angajat;
import agentie.services.AgentieException;
import agentie.services.IAgentieServices;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginController {
    @FXML
    TextField textFieldUsername;
    @FXML
    PasswordField passwordFieldPassword;
    @FXML
    Button buttonLogin;
    @FXML
    AnchorPane loginLayout;
    Scene sessionScene;

    private IAgentieServices service;
    private SessionController sessionController;
    private AnchorPane sessionLayout;

    public void setService(IAgentieServices service) {
        this.service = service;
    }

    public void setSessionController(SessionController sessionController, AnchorPane sessionLayout) {
        this.sessionController = sessionController;
        this.sessionLayout = sessionLayout;
    }

    @FXML
    public void handleLogin() throws AgentieException {
        Platform.runLater(()->{
            String username = textFieldUsername.getText();
            String password = passwordFieldPassword.getText();
            passwordFieldPassword.setText("");
            try {
                service.login(username, password, sessionController);
                Angajat angajat = new Angajat(username, password);
                makeFadeOut(angajat);
            } catch (AgentieException e) {
                ControllerAlert.showErrorMessage(null, e.getMessage());
            }
        });
    }

    private void makeFadeOut(Angajat angajat) {
        Platform.runLater(()->{
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(100));
            fadeTransition.setNode(loginLayout);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setOnFinished((ActionEvent event) -> {
                loadSessionScene(angajat);
            });
            fadeTransition.play();
        });
    }

    private void loadSessionScene(Angajat angajat) {
        try {
            sessionController.setService(service);
            sessionController.setUtils(angajat, sessionLayout);
            Stage currentStage = (Stage) loginLayout.getScene().getWindow();
            currentStage.setTitle("AgentieTurism: " + angajat.getUsername());
            if (sessionScene != null)
                currentStage.setScene(sessionScene);
            else currentStage.setScene(new Scene(sessionLayout));
            sessionController.makeFadeIn();
            sessionController.initialize();
        } catch (AgentieException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void setSessionScene(Scene currentScene) {
        this.sessionScene = currentScene;
    }
}
