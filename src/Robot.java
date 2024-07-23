import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Robot extends Thread {
    private UnregulatedMotor rightMotor = new UnregulatedMotor(MotorPort.B);
    private UnregulatedMotor leftMotor = new UnregulatedMotor(MotorPort.C);

    private double speed = 10; // Forward motion speed of robot [-10,10]
    private boolean isTurning = false;
    private boolean ignoreObject = false;
    
    /**
     * Set forward speed of the robot.
     * 
     * @param s - speed of robot [-10,10]. A negative speed will make the robot go backwards.
     */
    public void setSpeed(double s) {
        if (s > 10) s = 10; // Limit speed
        if (s < -10) s = -10;
        speed = s;
    }
    
    /**
     * Increase or decrease speed.
     * 
     * @param i - the amount to change current speed
     */
    public void increaseSpeed(double i) {
        setSpeed(speed + i);
    }
    
    public void setIgnoreObject(boolean v) {
    	ignoreObject = v;
    }
    
    public boolean getIgnoreObject() {
    	return ignoreObject;
    }
    

    /**
     * Move the robot forward continuously.
     */
    public void moveForward() {
    	if(isTurning) return;
        int motorPower = (int) (speed * 10);
        rightMotor.setPower(motorPower);
        leftMotor.setPower(motorPower);
    }
    
    /**
     * Move the robot backward continuously.
     */
    public void moveBackward() {
        int motorPower = (int) (speed * -10); // Reverse the motor power based on speed
        rightMotor.setPower(motorPower);
        leftMotor.setPower(motorPower);
    }

    /**
     * Turn the robot 90 degrees to the left.
     */
    public void turn90DegreesLeft() {
    	if(isTurning == true) return;
    	isTurning = true;
    	Delay.msDelay(100);
    	moveBackward();
    	Delay.msDelay(250);
    	rightMotor.setPower(0);
    	rightMotor.setPower(0);
    	Delay.msDelay(50);
        int motorPower = -50;
        rightMotor.setPower(motorPower);
        leftMotor.setPower(motorPower * -1);
        Delay.msDelay(485);
        isTurning = false;
    }

    /**
     * This method moves the robot forward at the specified speed.
     */
    public void run() {
        rightMotor.resetTachoCount();
        leftMotor.resetTachoCount();
        Sound.beepSequenceUp();
        Thread.currentThread().setPriority(MAX_PRIORITY);

		while (!Button.ESCAPE.isDown()) {
			moveForward();
		}
		
        rightMotor.close();
        leftMotor.close();
    }
    
}
