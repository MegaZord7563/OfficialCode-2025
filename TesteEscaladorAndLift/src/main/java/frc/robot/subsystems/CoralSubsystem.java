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

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//WPILIB Imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Robot Imports
import frc.robot.Constants.SubsystemsConstants.CoralConstants;

/** Add your docs here. */
public class CoralSubsystem extends SubsystemBase 
{
  // The Coral motor
  private final SparkMax coralLeaderMotor;
  private SparkMaxConfig globalConfig = new SparkMaxConfig();
  
  //private final SparkMax coralFollowerMotor;
  //private SparkMaxConfig followerConfig = new SparkMaxConfig();

  // The Coral encoder
  private final RelativeEncoder coralLeaderEncoder;
  //private final RelativeEncoder coralFollowerEncoder;

  // The alga PID controller
  private final SparkClosedLoopController coralLeaderPidController;
  //private final SparkClosedLoopController coralFollowerPidController;

  // Create a PID controller whose setpoint's change is subject to maximum
  // velocity and acceleration constraints.
  private final TrapezoidProfile.Constraints m_constraints = new TrapezoidProfile.Constraints(14.0, 7.0);
  private final ProfiledPIDController m_controller = new ProfiledPIDController(2.3, 0.0, 0.7, m_constraints, 0.02);


  /** Creates a new coralSubsytem. */
  public CoralSubsystem() 
  {
    // Create the coral motor
    coralLeaderMotor = new SparkMax(CoralConstants.kCoralMotorId,MotorType.kBrushless);
    //coralFollowerMotor = new SparkMax(LiftConstants.kLiftRightMotorId,MotorType.kBrushless);

    //Create PID controller
    coralLeaderPidController = coralLeaderMotor.getClosedLoopController();
    //coralRightPidController = coralLeftMotor.getClosedLoopController();

    // Create the coral encoder
    coralLeaderEncoder = coralLeaderMotor.getEncoder();
    //coralRightEncoder = coralRightMotor.getEncoder();

    //Calls motors config
    initializeMotors();
    coralLeaderEncoder.setPosition(0);
    

    
  }

  
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    // Configure the coral motor
     globalConfig
    .idleMode(CoralConstants.kCoralIdleMode) //LiftConstants.kLiftIdleMode
    .smartCurrentLimit(CoralConstants.kCoralCurrentLimit) //LiftConstants.kLiftCurrentLimit
    .inverted(false);

    //Config follower motor
    /*followerConfig
    .apply(globalConfig)
    //.inverted(true)
    .follow(coralLeaderMotor, true);*/
    

    // Configure the coral encoder 
    globalConfig.encoder
    .positionConversionFactor(CoralConstants.kCoralPositionConversionFactor)
    .velocityConversionFactor(CoralConstants.kCoralVelocityConversionFactor);

    globalConfig.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the coral PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(CoralConstants.kPCoralUp, 
          CoralConstants.kICoralUp, 
          CoralConstants.kDCoralUp, 
          CoralConstants.kCoralUpFF)

    //slot 1
    .pidf(CoralConstants.kPCoralDown, 
          CoralConstants.kICoralDown, 
          CoralConstants.kDCoralDown, 
          CoralConstants.kCoralDownFF,
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
    .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal,ClosedLoopSlot.kSlot2)
    // Set MAXMotion parameters for position control. We need to pass
    // a closed loop slot to the maxMotion method to set the parameters for that slot.
    // In this case, we are setting the parameters for slot 2.
    // Set MAXMotion parameters for velocity control in slot 2
    .maxAcceleration(3.5, ClosedLoopSlot.kSlot2) //4.0
    .maxVelocity(7.0, ClosedLoopSlot.kSlot2)         //100
    .allowedClosedLoopError(0.05, ClosedLoopSlot.kSlot2);
    

    

    // Configure the coral motor - like burn flash
    coralLeaderMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
    /*coralFollowerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);*/

  }

  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Coral Position", getCoralPosition());
  }

  /**
   * Set the coral motor speed
   * @param coralMotorSpeed
   */
  public void moveCoral(double targetPosition, boolean direction)
  {
    
    if (direction)
    { 
      m_controller.setGoal(targetPosition);
      System.out.println("Switching to Position Control UP");
      //coralLeaderPidController.setReference(targetPosition, ControlType.kPosition);
     
      coralLeaderMotor.setVoltage(m_controller.calculate(coralLeaderEncoder.getPosition()));
      System.out.println("Current Position: " + coralLeaderEncoder.getPosition());
    }
    else
    {
      System.out.println("Switching to Position Control DOWN");
      /*coralLeaderPidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      coralLeaderPidController.setReference( targetPosition, 
                                            ControlType.kMAXMotionPositionControl,
                                            ClosedLoopSlot.kSlot2);
      System.out.println("Current Position: " + coralLeaderEncoder.getPosition());
    }
    
  }

  /**
   * Stop the coral motor
   * @ Stop the coral motor
   */
  public void stopCoral()
  {
    coralLeaderPidController.setReference(0, ControlType.kDutyCycle);
    
  }

  /**
   * Get coral position
   * @return
   */
  public double getCoralPosition()
  {
    return coralLeaderEncoder.getPosition();
  }
 
  /**
   * Set the coral encoder position
   * @param position
   */
  public void resetCoralEncoder()
  {
    coralLeaderEncoder.setPosition(0);
    
  }

  
}

