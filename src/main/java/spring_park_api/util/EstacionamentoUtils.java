package spring_park_api.util;

import java.time.LocalDateTime;

public class EstacionamentoUtils {

	public static String gerarRecibo() {
		LocalDateTime date = LocalDateTime.now();
		String recibo = date.toString().substring(0, 19);
		return recibo.replace("-", "").replace(":", "").replace("T", "-");
	}
}
