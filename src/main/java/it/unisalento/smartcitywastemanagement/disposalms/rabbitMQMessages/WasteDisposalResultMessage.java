package it.unisalento.smartcitywastemanagement.disposalms.rabbitMQMessages;

public class WasteDisposalResultMessage {


    private Status status;

    private String message;


    public enum Status {
        SUCCESS,ERROR
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WasteDisposalResultMessage(Status status, String message) {
        this.status = status;
        this.message = message;
    }
}
