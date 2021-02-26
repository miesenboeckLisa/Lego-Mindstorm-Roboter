package kwm2020_roboter;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;


public class InfraredSignalCheckerThread extends Thread {
	private EV3IRSensor infraredSensor;
    private RegulatedMotor leftMotor,rightMotor;

    public InfraredSignalCheckerThread(final EV3IRSensor infraredSensor, RegulatedMotor left, RegulatedMotor right){
        this.infraredSensor = infraredSensor;
        this.leftMotor = left;
        this.rightMotor = right;
    }

    public void run(EV3TouchSensor touchleft, EV3TouchSensor touchright, EV3LargeRegulatedMotor motorA, EV3LargeRegulatedMotor motorB) {
        while(true){
            final int remoteCommand = infraredSensor.getRemoteCommand(0);
            System.out.println("Got remote command " + remoteCommand);
            switch (remoteCommand){
                case 0: //Checkt nix
                	leftMotor.startSynchronization();	    
            		leftMotor.stop();
            		rightMotor.stop();
            		leftMotor.endSynchronization();
                    break;
                case 1: //Nach links
                    //pilot.rotateLeft();
                	System.out.println("Should turn left");
                    break;
                case 2: //Nach rechts
                    //pilot.rotateRight();
                    System.out.println("Should turn right");
                    break;
                case 3: //nach vorne
    				motorA.setAcceleration(1000);
    				motorB.setAcceleration(1000);
    				boolean drive = true;
    				while(drive) {
    					motorA.forward();
    					motorB.forward();
    					int sampleSize1 = touchleft.sampleSize();
    					int sampleSize2 = touchright.sampleSize();
    					float[] sample1 = new float[sampleSize1];
    					float[] sample2 = new float[sampleSize2];
    					touchleft.fetchSample(sample1, 0);
    					touchright.fetchSample(sample2, 0);
    					if(sample1[0]!=1 && sample2[0]!=1) {
    						drive = false;
    					}
    				}
    				motorA.stop();
    				motorB.stop();
                	leftMotor.startSynchronization();	    
            		leftMotor.forward();
            		rightMotor.forward();
            		leftMotor.endSynchronization();
                    break;
                    
                case 4: //zurück
                    //pilot.backward();
                	leftMotor.startSynchronization();	    
            		leftMotor.backward();
            		rightMotor.backward();
            		leftMotor.endSynchronization();
                    break;
                default:
                    System.out.println("not sure what to do...");
                    System.exit(0);
            }
        }
    }
}
