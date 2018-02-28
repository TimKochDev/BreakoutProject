package project.breakout.view;

import java.io.IOException;
import java.util.Arrays;

import de.cau.infprogoo.lighthouse.LighthouseDisplay;

public class TestLightHouseView {
	private static final String USERNAME = "cctim";
	private static final String PASSWORD = "API-TOK_tljB-M5T/-PRdX-DQMO-YM1p";
	private static LighthouseDisplay display1 = new LighthouseDisplay(USERNAME, PASSWORD,2);
	private static byte[] data1 = new byte[14 * 28 * 3];
	
	/**
	 * Initializes the connection to the lighthouse.
	 */
	public static  void initTestLighthouse() {
		
		try {
			display1.connect();
		} catch (Exception e) {
			System.out.println("Connection failed: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
			//return false;
		}
		System.out.println(display1.isConnected());
		while (!TestLightHouseView.isConnected()) {
			System.out.println("wait for connection");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(display1.isConnected());

		data1[0] = 127;
		data1[1] = 127;
		data1[2] = 127;
		data1[3] = 00;
		data1[4] = -100;
		data1[5] = -100;
		
		try {
			display1.send(data1);
			System.out.println(Arrays.toString(data1));
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println(display1.isConnected());

	}

	
	/**
	 * Checks if there in a connection to the lighthouse.
	 * 
	 * @return {@code true} if LighthouseView is connected, {@code false} if not.
	 */
	public static boolean isConnected() {
		if (display1 != null) {
			return display1.isConnected();
		} else {
			return false;
		}
	}
}
