/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import utils.Value;

/**
 *
 * @author Son Vu
 */
public class CaroClient {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		new Client(Value.serverPort, Value.hostAddress).start();
	}

}
