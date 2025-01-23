// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//REV Imports
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkMaxConfig;

//WPILIB Imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Robot Imports
import frc.robot.Constants.SubsystemsConstants.LiftConstants;

/** Add your docs here. */
public class LiftSubsystem extends SubsystemBase 
{
  // The lift motor
  private final SparkMax liftMotor;
  private SparkMaxConfig config = new SparkMaxConfig();

  // The lift encoder
  private final RelativeEncoder liftEncoder;

  // The lift PID controller
  private final SparkClosedLoopController liftPidController;


  /** Creates a new LiftSubsytem. */
  public LiftSubsystem() 
  {
    // Create the lift motor
    liftMotor = new SparkMax(LiftConstants.kLiftMotorId,MotorType.kBrushless);

    // Create the lift encoder
    liftEncoder = liftMotor.getEncoder();
     liftPidController = liftMotor.getClosedLoopController(); 
    config.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

    // Configure the lift motor
     config
    .idleMode(LiftConstants.kLiftIdleMode)
    .smartCurrentLimit(LiftConstants.kLiftCurrentLimit);

    // Configure the lift encoder 
    config.encoder
    .positionConversionFactor(LiftConstants.kLiftPositionConversionFacotr)
    .velocityConversionFactor(LiftConstants.kLiftVelocityConversionFactor);

    // Configure the lift PID controller
    config.closedLoop
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    .pidf(LiftConstants.kPLift, LiftConstants.kILift, LiftConstants.kDLift, LiftConstants.kLiftFF)
      //This is a velocity feed-forward. This is unique to each type of motor, 
      //and can be calculated by taking the reciprocal of the motor's velocity 
      //constant (Kv), in other words 1/Kv.


                            //WRAPING

    // Enable PID wrap around for the turning motor. 
    //This will allow the PID controller to go through 0 to get to the setpoint i.e. 
    //going from 350 degrees to 10 degrees will go through 0 rather than the other direction which is a longer route.
    .positionWrappingEnabled(true)                                           
    .positionWrappingMinInput(LiftConstants.kLiftEncoderPositionPIDMinInput) 
    .positionWrappingMaxInput(LiftConstants.kLiftEncoderPositionPIDMaxInput)  
    .outputRange(LiftConstants.kLiftMinOutput,// Set the output range of the PID controller.                                 
    LiftConstants.kLiftMaxOutput,LiftConstants.pidSparkSlot)// Set the output range of the PID controller.
    ;

    // Configure the lift motor
    liftMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//

    /*boolean isInverted = liftMotor.configAccessor.getInverted();
    double positionFactor = liftMotor.configAccessor.encoder.getPositionConversionFactor();
    double velocityFactor = liftMotor.configAccessor.encoder.getVelocityConversionFactor();*/

    //Reset encoder
    liftEncoder.setPosition(0);
  }

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }

  /**
   * Set the lift motor speed
   * @param LiftMotorSpeed
   */
  public void startLift(double LiftMotorSpeed)
  {
    liftPidController.setReference(LiftMotorSpeed, ControlType.kVelocity);
  }

  /**
   * Stop the lift motor
   * @
   */
  public void stopLift()
  {
    //liftPidController.setReference(0, ControlType.kVelocity);
    liftMotor.set(0);
  }

 
  /**
   * Set the lift encoder position
   * @param position
   */
  public void resetLiftEncoder()
  {
    liftEncoder.setPosition(0);
  }
}

