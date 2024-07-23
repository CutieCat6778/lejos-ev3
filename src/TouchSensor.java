import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

public class TouchSensor extends Thread {
    private EV3TouchSensor us;
    private SampleProvider sp;
    private float[] sample;
    private Robot robot;
    private boolean ready = false;
    
    /**
     * @param rob - reference to the Robot object to control.
     */
    TouchSensor(Robot rob) {
        robot = rob;
    }
    
    public boolean getReady() {
        return ready;
    }
    
    /**
     * Continuously read the distance to the closest object.
     * If distance is less than 20 cm, turn the robot left 90 degrees.
     */
    public void run() {
        while (us == null) {
            try {
                us = new EV3TouchSensor(SensorPort.S4);
            } catch (IllegalArgumentException e) {
                System.err.println("Ultrasonic sensor: " + e.getMessage() + ". Retrying...");
            }
        }
        sp = us.getTouchMode();
        sample = new float[sp.sampleSize()];
        
        Sound.beepSequenceUp();
        ready = true;
        
        
        while (!Button.ESCAPE.isDown()) {
            boolean touched = getTouched();
            if (touched == true && robot.getIgnoreObject() == false) { // distance < 20 cm => turn the robot left 90 degrees
            	Sound.beepSequence();
            	robot.turn90DegreesLeft();
            }
            if(touched == true && robot.getIgnoreObject() == true) {
            	for(int i = 0;i<3;i++) {
            		Sound.buzz();
            	}
            }
        }
        
        us.close();
    }
    
    /**
     * Return the distance in meters to any object.
     */
    private boolean getTouched() {
        sp.fetchSample(sample, 0);
        return sample[0] == 1;
    }
}
