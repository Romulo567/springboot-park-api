package spring_park_api.exception;

public class UserNameUniqueViolationException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UserNameUniqueViolationException(String message) {
		super(message);
	}
}
