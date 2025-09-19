package xyz.joseyamut.updatequerybuilder.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5723543240029047190L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
