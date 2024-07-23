import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

public class App {
		
	public static void main(String[] args) {
	    GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
	    final int SW = g.getWidth();
	    final int SH = g.getHeight();
	    g.setFont(Font.getLargeFont());
	    g.drawString("Start", SW/2, 40, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
		    
	    g.clear();
	    
	    Robot robot = new Robot();
	    
	    //ColorSensor csc = new ColorSensor(robot);
	    //csc.start();
	
		TouchSensor tsc= new TouchSensor(robot);
		tsc.start();
		
		g.setFont(Font.getLargeFont());
		g.drawString("Starting", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
		while(tsc.getReady() == false) {}// || tsc.getReady() == false) {}

		g.clear();
		g.setFont(Font.getLargeFont());
		g.drawString("Fight!", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
		
		Button.waitForAnyPress();
		
        robot.start();
	}
}