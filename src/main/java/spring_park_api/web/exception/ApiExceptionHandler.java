package spring_park_api.web.exception;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import spring_park_api.exception.CodigoUniqueViolationException;
import spring_park_api.exception.CpfUniqueViolationException;
import spring_park_api.exception.EntityNotFoundException;
import spring_park_api.exception.PasswordInvalidException;
import spring_park_api.exception.ReciboCheckinNotFoundException;
import spring_park_api.exception.UserNameUniqueViolationException;
import spring_park_api.exception.VagaDisponivelException;
import spring_park_api.jwt.JwtAuthenticationEntryPoint;

@RestControllerAdvice
public class ApiExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	
	
	@ExceptionHandler(ReciboCheckinNotFoundException.class)
	public ResponseEntity<ErrorMessage> reciboCheckinNotFoundException(ReciboCheckinNotFoundException ex, HttpServletRequest request){
		Object[] params =  {ex.getRecibo()};
		String message = messageSource.getMessage("exception.reciboCheckinNotFoundException", params, request.getLocale());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException ex, HttpServletRequest request){
		Object[] params =  {ex.getRecurso(), ex.getCodigo()};
		String message = messageSource.getMessage("exception.entityNotFoundException", params, request.getLocale());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
	}
	
	@ExceptionHandler(CodigoUniqueViolationException.class)
	public ResponseEntity<ErrorMessage> codigoUniqueViolationException(CodigoUniqueViolationException ex, HttpServletRequest request){
		Object[] params =  {ex.getRecurso(), ex.getCodigo()};
		String message = messageSource.getMessage("exception.codigoUniqueViolationException", params, request.getLocale());
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.CONFLICT, message));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result){
		log.error("Api Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, messageSource.getMessage("message.invalid.field", null, request.getLocale()), result, messageSource));
	}
	
	@ExceptionHandler({UserNameUniqueViolationException.class, CpfUniqueViolationException.class})
	public ResponseEntity<ErrorMessage> UniqueViolationException(RuntimeException ex, HttpServletRequest request){
		log.error("Api Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
	}
	
	@ExceptionHandler(VagaDisponivelException.class)
	public ResponseEntity<ErrorMessage> VagaDisponivelException(RuntimeException ex, HttpServletRequest request){
		String message = messageSource.getMessage("exception.vagaDisponivelException", null, request.getLocale());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
	}
	
	@ExceptionHandler(PasswordInvalidException.class)
	public ResponseEntity<ErrorMessage> passwordInvalideException(RuntimeException ex, HttpServletRequest request){
		log.error("Api Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException ex, HttpServletRequest request){
		log.error("Api Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> internalServerErrorException(Exception ex, HttpServletRequest request){
		ErrorMessage error = new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		log.error("Internal Server Error {} {}", error, ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.APPLICATION_JSON)
				.body(error);
	}
}
