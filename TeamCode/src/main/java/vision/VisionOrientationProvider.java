package vision;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.SwitchableCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.SwitchableCameraName;

import java.util.ArrayList;

import hardware.Hardware;

public class VisionOrientationProvider {
    WebcamName[] webcams;
    SwitchableCameraName switchableName;
    Hardware hardware;
    VuforiaLocalizer vuforia;
    ArrayList<VuforiaTrackable> allTargets;
    OpenGLMatrix RightWebcam, LeftWebcam, location;
    protected final String license = "TOTALLY REAL VUFORIA LICENSE KEY";
    protected final String trackablesName = "TRACKABLES I DUNNO WHAT THEY WILL BE CALLED NEXT YEAR LOL";
    public VisionOrientationProvider(Hardware hardware, WebcamName... cams){
        webcams = cams;
        this.hardware = hardware;
    }

    public void init(){
        switchableName = ClassFactory.getInstance().getCameraManager().nameForSwitchableCamera(webcams);
        int cameraMonitorViewId = hardware.getOpMode().hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardware.getOpMode().hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = license;
        parameters.cameraName = switchableName;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        VuforiaTrackables targets = vuforia.loadTrackablesFromAsset(trackablesName);
        allTargets.addAll(targets);
        float mmPerInch = 25.4f;
        float mmFTCFieldWidth = (12*12 - 2) * mmPerInch;
        OpenGLMatrix testLocation = OpenGLMatrix.translation(mmFTCFieldWidth/2, 6.12f * mmPerInch, 0).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZX, AngleUnit.DEGREES, 90, 90, 0));
        allTargets.get(0).setLocation(testLocation);
        float botWidthMM = 18 * mmPerInch;
        RightWebcam = OpenGLMatrix.translation(botWidthMM/2, 0, 0).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES, 90, 90, 0));
        LeftWebcam = OpenGLMatrix.translation(-botWidthMM/2, 0, 0).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES, 90, -90, 0));
        ((VuforiaTrackableDefaultListener)allTargets.get(0)).setCameraLocationOnRobot(webcams[0], RightWebcam);
        ((VuforiaTrackableDefaultListener)allTargets.get(0)).setCameraLocationOnRobot(webcams[1], LeftWebcam);
    }

    public void start(){
        ((SwitchableCamera)vuforia.getCamera()).setActiveCamera(webcams[0]);
        vuforia.loadTrackablesFromAsset(trackablesName).activate();
    }

    public void update(){
        for(VuforiaTrackable target : allTargets){
            if(((VuforiaTrackableDefaultListener)target.getListener()).isVisible()){
                location = ((VuforiaTrackableDefaultListener)target.getListener()).getUpdatedRobotLocation();
            }
        }
    }

    public void setCamera(WebcamName camera){
        ((SwitchableCamera)vuforia.getCamera()).setActiveCamera(camera);
    }

    public OpenGLMatrix getPosition(){
        return location;
    }


}
