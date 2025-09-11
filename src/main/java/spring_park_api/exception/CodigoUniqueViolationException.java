package spring_park_api.exception;

public class CodigoUniqueViolationException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CodigoUniqueViolationException(String message) {
		super(message);
	}
}
