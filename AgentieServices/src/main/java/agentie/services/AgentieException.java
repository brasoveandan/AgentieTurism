package agentie.services;

public class AgentieException extends Exception {
    public AgentieException() {
    }

    public AgentieException(String message) {
        super(message);
    }

    public AgentieException(String message, Throwable cause) {
        super(message, cause);
    }
}