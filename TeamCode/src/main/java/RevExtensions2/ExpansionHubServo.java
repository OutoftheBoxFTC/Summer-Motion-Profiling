package RevExtensions2;

import com.qualcomm.hardware.lynx.LynxController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;

/**
 * Extends a ServoImplEx to provide access to new features.
 * Note: servo MUST be attached to an Expansion Hub.
 */

public class ExpansionHubServo extends ServoImplEx
{
    private ExpansionHubEx expansionHubEx;

    /*
     * Don't use this constructor in user-code; this object will be
     * hotswapped into the hardwareMap at runtime.
     */
    ExpansionHubServo(Servo servo)
    {
        super(
                (ServoControllerEx)servo.getController(),
                servo.getPortNumber(),
                servo.getDirection(),
                ServoConfigurationType.getStandardServoType());

        expansionHubEx = new ExpansionHubEx(Utils.getLynxFromController((LynxController) servo.getController()));
    }

    /***
     * Set the pulse width output of this servo port directly, rather than using
     * the -1 to 1 range from the SDK
     *
     * @param uS the pulse width (in uS) to set this servo port to
     */
    public void setPulseWidthUs(int uS)
    {
        expansionHubEx.setServoPulseWidth(getPortNumber(), uS);
    }
}