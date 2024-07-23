import lejos.hardware.motor.Motor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.Button;
import lejos.robotics.SampleProvider;

public class A {
    public static void main(String[] args) {
        
    	 EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
         SampleProvider touchSampleProvider = touchSensor.getTouchMode();
         float[] touchSample = new float[touchSampleProvider.sampleSize()];
    	
        EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S4);
        SampleProvider distanceMode = usSensor.getDistanceMode();
        float[] sample = new float[distanceMode.sampleSize()];
        
        Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 5.5).offset(74);
        Wheel wheel2 = WheeledChassis.modelWheel(Motor.A, 5.5).offset(-74);
        Chassis chassis = new WheeledChassis(new Wheel[] {wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot pilot = new MovePilot(chassis);
                
        LCD.drawString("Press any button\n to start...", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();
        
        Motor.A.setSpeed(Motor.A.getSpeed()+1000);
        Motor.B.setSpeed(Motor.B.getSpeed()+1000);

        pilot.forward();
        int wait = 0;
        while (!Button.ESCAPE.isDown()) {
        
        touchSampleProvider.fetchSample(touchSample, 0);
        
        if(touchSample[0] == 1) {
            pilot.stop();
            wait = 0;
            LCD.drawString("Press any button\n to start...", 0, 0);
            Button.waitForAnyPress();
            LCD.clear();        
            pilot.forward();
        }
            distanceMode.fetchSample(sample, 0);
            if (sample[0] < 0.33&&wait>=100){				//(R(der erste wert))
            	turn();
            	wait = 0;
            }
       
      		Delay.msDelay(       40      );				//(R)
            wait++;
        }
        touchSensor.close();
        usSensor.close();
        pilot.stop();
        LCD.drawString("Stopped", 0, 0);
        Button.waitForAnyPress();
    }
    
    public static void turn() {
    	int old=Motor.A.getSpeed();
    	Motor.A.setSpeed(Motor.B.getSpeed()-250); 			//(R)
        Delay.msDelay(       3000      );				//(R)
    	Motor.A.setSpeed(old);

    }
}
