package spring_park_api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EstacionamentoUtils {

	private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;
    
    private EstacionamentoUtils() {}
    
    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            
            total = PRIMEIROS_15_MINUTES;
            
        } else if (minutes <= 60) {
            
            total = PRIMEIROS_60_MINUTES;
            
        } else {
            
            long minutosAdicionais = minutes - 60;
            
            double blocosAdicionais = Math.ceil((double)minutosAdicionais / 15.0);
            
            total = PRIMEIROS_60_MINUTES + (blocosAdicionais * ADICIONAL_15_MINUTES);
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }
    
	private static final double DESCONTO_PERCENTUAL = 0.30;

	public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {

		BigDecimal desconto = null;

		if (numeroDeVezes > 0 && numeroDeVezes % 10 == 0) {
			BigDecimal fatorDesconto = BigDecimal.valueOf(DESCONTO_PERCENTUAL);
			desconto = custo.multiply(fatorDesconto);
		} else {
			desconto = BigDecimal.ZERO;
		}

		return desconto.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public static String gerarRecibo() {
		LocalDateTime date = LocalDateTime.now();
		String recibo = date.toString().substring(0, 19);
		return recibo.replace("-", "").replace(":", "").replace("T", "-");
	}
}
