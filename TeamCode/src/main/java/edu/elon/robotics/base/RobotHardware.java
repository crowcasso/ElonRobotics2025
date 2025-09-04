package edu.elon.robotics.base;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class RobotHardware {

    // drive motors
    public DcMotor motorLeft;
    public DcMotor motorRight;
    public DcMotor motorAux;

    public final KiwiDriveRatio ratio;

    public RobotHardware(HardwareMap hardwareMap, boolean isAuto) {
        // define the drive motors
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
        motorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.FORWARD);
        motorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorAux = hardwareMap.dcMotor.get("motorAux");
        motorAux.setDirection(DcMotor.Direction.FORWARD);
        motorAux.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // reset the drive encoders to zero
        resetDriveEncoders();

        // setup the motor ratio
        ratio = new KiwiDriveRatio(isAuto);

    }

    public void resetDriveEncoders() {
        /*
         * This code resets the encoder values back to 0 for
         * each of the three drive motors.
         */
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorAux.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorAux.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void startMove(double drive, double strafe, double turn, double speedModifier) {
        /*
         * How much power should we apply to the left,
         * right, and aux motor?
         *
         * If all 3 motors apply the same power in the
         * same direction, the robot will turn in place.
         */
        ratio.computeRatio(turn, turn, turn);

        /*
         * Limit the modifier.
         */
        speedModifier = Range.clip(speedModifier, 0.0, 1.0);

        /*
         * Apply the power to the motors.
         */
        motorLeft.setPower(ratio.powerLeft * speedModifier);
        motorRight.setPower(ratio.powerRight * speedModifier);
        motorAux.setPower(ratio.powerAux * speedModifier);
    }

    public void startMove(double drive, double strafe, double turn) {
        startMove(drive, strafe, turn, 1.0);
    }
}
