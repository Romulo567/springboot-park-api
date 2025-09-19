package spring_park_api.exception;

public class EntityNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String recurso;
	private String codigo;
	
	public EntityNotFoundException(String message) {
		super(message);
	}
	
	public EntityNotFoundException(String recurso, String codigo) {
		this.recurso = recurso;
		this.codigo = codigo;
	}


	public String getRecurso() {
		return recurso;
	}

	public String getCodigo() {
		return codigo;
	}
}
