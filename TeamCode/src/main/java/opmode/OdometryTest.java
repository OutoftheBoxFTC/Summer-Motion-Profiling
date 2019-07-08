package opmode;

import drivetrain.HolonomicDrive;

public class OdometryTest extends BasicOpmode {
    public OdometryTest() {
        super(new HolonomicDrive(1), 0.1, true);
    }

    @Override
    protected void setup() {

    }
}
