import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class ColorSensor extends Thread {
	private EV3ColorSensor us;
	private SampleProvider sp;
	private float[] samples;
	private Robot robot;
	private boolean ready = false;


	ColorSensor(Robot rob) {
		robot = rob;
	}
	
	public boolean getReady() {
		return ready;
	}


	public void run() {
		while (us == null) {
            try {
                us = new EV3ColorSensor(SensorPort.S3);
            } catch (IllegalArgumentException e) {
                System.err.println("Ultrasonic sensor:« " + e.getMessage() + ". Retrying...");
            }
        }
		
        ready = true;
        
		sp = us.getRGBMode();
		samples = new float[sp.sampleSize()];
		
		while (!Button.ESCAPE.isDown()) {
			float[] colors = getReflectedRGB();
			if (checkColor(colors) == 1 && robot.getIgnoreObject() == false) { // distance<20 cm => stop gyroboy and buzz
				Sound.twoBeeps();
				robot.setIgnoreObject(true);
				Delay.msDelay(2800);
				robot.setIgnoreObject(false);
			}
		}
		
        us.close();
	}

	public float[] getReflectedRGB() {
		sp.fetchSample(samples, 0);

		return new float[] { samples[0], samples[1], samples[2] };
	}

	public static int checkColor(float[] colors) {
	    if (colors.length != 3) {
	        throw new IllegalArgumentException("RGB array must have 3 elements.");
	    }

	    // Define normalized RGB values for red and blue
	    float[] red = {1.0f, 0.0f, 0.0f};
	    float[] blue = {0.0f, 0.0f, 1.0f};

	    // Calculate Euclidean distance to red and blue
	    float distanceToRed = calculateDistance(colors, red);
	    float distanceToBlue = calculateDistance(colors, blue);

	    // Define a threshold for being "close" to a color
	    float threshold = 0.969f; // Adjust this threshold as needed

	    System.out.println(distanceToRed + " " + distanceToBlue);

	    // Return 1 if the color is close enough to red or blue
	    if (distanceToRed >= threshold ^ distanceToBlue >= threshold) {
	        return 1;
	    } else {
	        return 0;
	    }
	}

	private static float calculateDistance(float[] color1, float[] color2) {
	    return (float) Math.sqrt(
	        Math.pow(color1[0] - color2[0], 2) +
	        Math.pow(color1[1] - color2[1], 2) +
	        Math.pow(color1[2] - color2[2], 2)
	    );
	}
}