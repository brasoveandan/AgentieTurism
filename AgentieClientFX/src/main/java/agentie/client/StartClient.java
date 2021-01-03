package agentie.client;

import agentie.client.controller.LoginController;
import agentie.client.controller.SessionController;
import agentie.services.IAgentieServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            IAgentieServices services = (IAgentieServices)factory.getBean("agentieService");
            System.out.println("Obtained a reference to remote agentie server");

            FXMLLoader loginLoader = new FXMLLoader();
            loginLoader.setLocation(getClass().getResource("/views/loginView.fxml"));
            Parent loginLayout = loginLoader.load();
            primaryStage.setScene(new Scene(loginLayout));
            LoginController loginController = loginLoader.getController();
            loginController.setService(services);

            FXMLLoader sessionLoader = new FXMLLoader();
            sessionLoader.setLocation(getClass().getResource("/views/sessionView.fxml"));
            Parent sessionLayout = sessionLoader.load();
            SessionController sessionController = sessionLoader.getController();
            loginController.setSessionController(sessionController, (AnchorPane) sessionLayout);

            primaryStage.setTitle("AgentieTurism");
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Agentie Initialization exception: " + e);
            e.printStackTrace();
        }
    }
}
