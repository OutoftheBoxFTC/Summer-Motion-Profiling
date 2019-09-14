package RevExtensions2;

import com.qualcomm.hardware.lynx.LynxController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

public class ExpansionHubMotor extends DcMotorImplEx
{
    private ExpansionHubEx expansionHubEx;

    /*
     * Don't use this constructor in user-code; this object will be
     * hotswapped into the hardwareMap at runtime.
     */
    ExpansionHubMotor(DcMotor motor)
    {
        super(motor.getController(), motor.getPortNumber(), motor.getDirection(), motor.getMotorType());
        expansionHubEx = new ExpansionHubEx(Utils.getLynxFromController((LynxController) motor.getController()));
    }

    /**
     * Get the amount of current this motor is pulling from its H-bridge
     *
     * @return the current draw in milliamps
     */
    public double getCurrentDraw(ExpansionHubEx.CurrentDrawUnits units)
    {
        return expansionHubEx.getMotorCurrentDraw(units, getPortNumber());
    }

    /***
     * Query as to whether the H-bridge for this motor is over-temp
     *
     * @return boolean indicating the H-bridge is over-temp
     */
    public boolean isBridgeOverTemp()
    {
        return expansionHubEx.isMotorBridgeOverTemp(getPortNumber());
    }

    /***
     * Query as to whether this motor has lost (encoder?) counts
     * @deprecated because I have no idea what this actually does
     *
     * @return boolean indicating whether the motor has lost (encoder?) counts
     */
    @Deprecated
    public boolean hasLostCounts()
    {
        return expansionHubEx.hasMotorLostCounts(getPortNumber());
    }
}