package spring_park_api.exception;

public class ReciboCheckinNotFoundException  extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String recibo;

	public ReciboCheckinNotFoundException(String recibo) {
		this.recibo = recibo;
	}

	public String getRecibo() {
		return recibo;
	}
}
