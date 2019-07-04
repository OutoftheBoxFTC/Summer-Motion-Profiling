import com.qualcomm.robotcore.hardware.DcMotor;

import org.openftc.revextensions2.ExpansionHubMotor;

public class SmartMotor {
    private ExpansionHubMotor motor;

    private double previousPower, power;
    private DcMotor.RunMode previousRunMode, runMode;
    private DcMotor.ZeroPowerBehavior previousZeroPowerBehavior, zeroPowerBehavior;

    public SmartMotor(ExpansionHubMotor motor){
        this.motor = motor;
        if(motor != null){
            previousRunMode = runMode = motor.getMode();
            previousZeroPowerBehavior = zeroPowerBehavior = motor.getZeroPowerBehavior();
            previousPower = power = motor.getPower();
        }
    }

    public void setPower(double power){
        this.power = power;
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behaviour){
        zeroPowerBehavior = behaviour;
    }

    public void setMode(DcMotor.RunMode mode){
        runMode = mode;
    }

    public ExpansionHubMotor getMotor() {
        return motor;
    }

    public void flushPower(){
        if(power != previousPower){
            previousPower = power;
            motor.setPower(power);
        }
    }

    public void flushStates(){
        if(!runMode.equals(previousRunMode)){
            previousRunMode = runMode;
            motor.setMode(runMode);
        }
        if(!zeroPowerBehavior.equals(previousZeroPowerBehavior)){
            previousZeroPowerBehavior = zeroPowerBehavior;
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }
}
