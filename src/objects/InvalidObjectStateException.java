package objects;

public class InvalidObjectStateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7785086359157775342L;

	public InvalidObjectStateException() {
		super();
	}
	
	public InvalidObjectStateException(String msg) {
		super(msg);
	}
}
