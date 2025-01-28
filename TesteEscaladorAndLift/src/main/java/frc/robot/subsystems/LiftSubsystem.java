// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



//REV Imports
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//WPILIB Imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Robot Imports
import frc.robot.Constants.SubsystemsConstants.LiftConstants;

/** Add your docs here. */
public class LiftSubsystem extends SubsystemBase 
{
  // The lift motor
  private final SparkMax liftLeaderMotor;
  private SparkMaxConfig globalConfig = new SparkMaxConfig();
  
  private final SparkMax liftFollowerMotor;
  private SparkMaxConfig followerConfig = new SparkMaxConfig();

  // The lift encoder
  private final RelativeEncoder liftLeaderEncoder;
  //private final RelativeEncoder liftFollowerEncoder;

  // The lift PID controller
  private final SparkClosedLoopController liftLeaderPidController;
  //private final SparkClosedLoopController liftFollowerPidController;


  /** Creates a new LiftSubsytem. */
  public LiftSubsystem() 
  {
    // Create the lift motor
    liftLeaderMotor = new SparkMax(LiftConstants.kLiftLeftMotorId,MotorType.kBrushless);
    liftFollowerMotor = new SparkMax(LiftConstants.kLiftRightMotorId,MotorType.kBrushless);

    // Create the lift encoder
    liftLeaderEncoder = liftLeaderMotor.getEncoder();
    //liftRightEncoder = liftRightMotor.getEncoder();

    //Create PID controller
    liftLeaderPidController = liftLeaderMotor.getClosedLoopController();
    //liftRightPidController = liftLeftMotor.getClosedLoopController();

    //Calls motors config
    initializeMotors();
    liftLeaderEncoder.setPosition(0);
    

    
  }

  
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    
    

    // Configure the lift motor
     globalConfig
    .idleMode(LiftConstants.kLiftIdleMode)
    .smartCurrentLimit(LiftConstants.kLiftCurrentLimit)
    .inverted(false);

    //Config follower motor
    followerConfig
    .apply(globalConfig)
    //.inverted(true)
    .follow(liftLeaderMotor, true);
    

    // Configure the lift encoder 
    globalConfig.encoder
    .positionConversionFactor(LiftConstants.kLiftPositionConversionFactor)
    .velocityConversionFactor(LiftConstants.kLiftVelocityConversionFactor);

    globalConfig.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the lift PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(LiftConstants.kPLiftUp, 
          LiftConstants.kILiftUp, 
          LiftConstants.kDLiftUp, 
          LiftConstants.kLiftUpFF)

    //slot 1
    .pidf(LiftConstants.kPLiftDown, 
          LiftConstants.kILiftDown, 
          LiftConstants.kDLiftDown, 
          LiftConstants.kLiftDownFF,
          ClosedLoopSlot.kSlot2);
     
    // Enable PID wrap around for the turning motor. 
    //This will allow the PID controller to go through 0 to get to the setpoint i.e. 
    //going from 350 degrees to 10 degrees will go through 0 rather than the other direction which is a longer route.
    /*.positionWrappingEnabled(true)  //01/28 MNL - it was true before                                         
    .positionWrappingMinInput(LiftConstants.kLiftEncoderPositionPIDMinInput) 
    .positionWrappingMaxInput(LiftConstants.kLiftEncoderPositionPIDMaxInput)  
    .outputRange( LiftConstants.kLiftMinOutput,// Set the output range of the PID controller.                                 
                  LiftConstants.kLiftMaxOutput,
                  ClosedLoopSlot.kSlot1);// Set the output range of the PID controller.*/
  
    //Max motion config
    globalConfig.closedLoop.maxMotion
    // Set MAXMotion parameters for position control. We need to pass
    // a closed loop slot to the maxMotion method to set the parameters for that slot.
    // In this case, we are setting the parameters for slot 2.
    // Set MAXMotion parameters for velocity control in slot 2
    .maxAcceleration(4.0, ClosedLoopSlot.kSlot2)
    .maxVelocity(100, ClosedLoopSlot.kSlot2)
    .allowedClosedLoopError(0.1, ClosedLoopSlot.kSlot2);
    //.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal,ClosedLoopSlot.kSlot2);

    

    // Configure the lift motor - like burn flash
    liftLeaderMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
    liftFollowerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//

  }

  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Lift Position", getLiftPosition());
  }

  /**
   * Set the lift motor speed
   * @param LiftMotorSpeed
   */
  public void moveLift(double targetPosition, boolean direction)
  {
    if (direction)
    {
      System.out.println("Switching to Position Control UP");
      liftLeaderPidController.setReference(targetPosition, ControlType.kPosition);
    }
    else
    {
      System.out.println("Switching to Position Control DOWN");
      /*liftLeaderPidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      liftLeaderPidController.setReference( targetPosition, 
                                            ControlType.kMAXMotionPositionControl,
                                            ClosedLoopSlot.kSlot2);
    }
    
  }

  /**
   * Stop the lift motor
   * @ Stop the lift motor
   */
  public void stopLift()
  {
    liftLeaderPidController.setReference(0, ControlType.kDutyCycle);
    
  }

  /**
   * Get lift position
   * @return
   */
  public double getLiftPosition()
  {
    return liftLeaderEncoder.getPosition();
  }
 
  /**
   * Set the lift encoder position
   * @param position
   */
  public void resetLiftEncoder()
  {
    liftLeaderEncoder.setPosition(0);
    
  }

  
}

