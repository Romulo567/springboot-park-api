package spring_park_api.jwt;

public class JwtToken {

	private String token;

	public JwtToken() {
	}

	public JwtToken(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
