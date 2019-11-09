package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous
public class GyroAutonomous extends LinearOpMode {
    private BNO055IMU imu;
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor gripMotor;
    private DcMotor armMotor;
    private Servo leftServo;
    private Servo rightServo;

    int ticksPerRev         = 1440;
    double timePerUnit      = 1075;
    double timePerDegreeCW  = 7.95;
    double timePerDegreeCCW = 8;
    Orientation angles;

    @Override
    public void runOpMode() {
        imu         = hardwareMap.get(BNO055IMU.class, "imu");
        leftMotor   = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor  = hardwareMap.get(DcMotor.class, "rightMotor");
        gripMotor   = hardwareMap.get(DcMotor.class, "gripMotor");
        armMotor    = hardwareMap.get(DcMotor.class, "armMotor");
        leftServo   = hardwareMap.get(Servo.class, "leftServo");
        rightServo  = hardwareMap.get(Servo.class, "rightServo");

        // initialize imu
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode                 = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.loggingEnabled       = false;
        imu.initialize(parameters);

        // set the left motor direction to "forward"
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // set the grip motor direction to "forward"
        gripMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        
        // set the arm motor zero power behavior to "brake"
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // set the grip motor zero power behavior to "brake"
        gripMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // set both motors zero power behavior to "brake"
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // set both motors mode to "run without encoder"
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        // set the servo position to "hook off"
        hookOff();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // wait for the game to start
        waitForStart();

        telemetry.addData("Status", "Running");
        telemetry.update();

        // **************************************************
        // autonomous path 
        // 
        // forward 2
        // ccw 90
        // forward 1
        // ccw 90
        // forward 1
        // cw 90
        // forward 1
        // cw 90
        // forward 0.5
        // hook on
        // backward 1.5
        // hook off
        // cw 90
        // forward 1.5
        // stop
        //
        // **************************************************

        // forward 2
        drive(1);
        driveFor(2);
        pause();
        // ccw 90
        turn(-1);
        turnForCCW(90);
        pause();
        // forward 1
        drive(1);
        driveFor(1);
        pause();
        // ccw 90
        turn(-1);
        turnForCCW(90);
        pause();
        // forward 1
        drive(1);
        driveFor(1);
        pause();
        // cw 90
        turn(1);
        turnForCW(90);
        pause();
        // forward 1
        drive(1);
        driveFor(1);
        pause();
        // cw 90
        turn(1);
        turnForCW(90);
        pause();
        // forward 0.5
        drive(1);
        driveFor(0.5);
        pause();
        // hook on
        hookOn();
        // backward 1.5
        drive(-1);
        driveFor(1.5);
        pause();
        // hook off
        hookOff();
        // cw 90
        turn(1);
        turnForCW(90);
        pause();
        // forward 1.5
        drive(1);
        driveFor(1.5);
        pause();
        // stop
        drive(0);

    }
    public void drive (double power) {
        // default: forward
        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }
    public void driveFor (double unit) {
        int time = (int)(timePerUnit * unit);
        sleep(time);
    }
    public void turn (double power) {
        // default: cw
        leftMotor.setPower(power);
        rightMotor.setPower(-power);
    }
    public void turnForCW (double degree) {
        int time = (int)(timePerDegreeCW * degree);
        sleep(time);
    }
    public void turnForCCW (double degree) {
        int time = (int)(timePerDegreeCCW * degree);
        sleep(time);
    }
    public void hookOn () {
        leftServo.setPosition(1);
        rightServo.setPosition(0);
    }
    public void hookOff () {
        leftServo.setPosition(0);
        rightServo.setPosition(1);
    }
    public void pause () {
        drive(0);
         sleep(100);
    }
    public double getCurrentAngle(){
        angles = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        double currentAngle = angles.thirdAngle;

        return currentAngle;
    }
    /*
    public int drivePos (double distance) {
        double circumference = Math.PI * 4.0; // pi * diameter
        double inPerRev = circumference / ticksPerRev;
        int targetPos = (int)(distance / inPerRev);

        return targetPos;
    }
    public void turnPos (double angle) {
        angles = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialAngle = angles.firstAngle;
        double robotAngle = angles.firstAngle;
        double previousAngle = angles.firstAngle;
        double minMotorPower = 0.3 ;
        double powerScaleFactor;

        // calculate target angle
        double targetAngle = initialAngle + angle;
        double diffAngle = Math.abs(targetAngle - robotAngle);
    }
    */
}