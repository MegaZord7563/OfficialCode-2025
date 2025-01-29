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
import frc.robot.Constants.SubsystemsConstants.AlgaeConstants;

/** Add your docs here. */
public class AlgaeSubsystem extends SubsystemBase 
{
  // The Algae motor
  private final SparkMax algaeLeaderMotor;
  private SparkMaxConfig globalConfig = new SparkMaxConfig();
  
  //private final SparkMax algaeFollowerMotor;
  //private SparkMaxConfig followerConfig = new SparkMaxConfig();

  // The Algae encoder
  private final RelativeEncoder algaeLeaderEncoder;
  //private final RelativeEncoder algaeFollowerEncoder;

  // The alga PID controller
  private final SparkClosedLoopController algaeLeaderPidController;
  //private final SparkClosedLoopController algaeFollowerPidController;

  // Create a PID controller whose setpoint's change is subject to maximum
  // velocity and acceleration constraints.
  private final TrapezoidProfile.Constraints m_constraints = new TrapezoidProfile.Constraints(14.0, 7.0);
  private final ProfiledPIDController m_controller = new ProfiledPIDController(2.3, 0.0, 0.7, m_constraints, 0.02);


  /** Creates a new algaeSubsytem. */
  public AlgaeSubsystem() 
  {
    // Create the algae motor
    algaeLeaderMotor = new SparkMax(AlgaeConstants.kAlgaeMotorId,MotorType.kBrushless);
    //algaeFollowerMotor = new SparkMax(LiftConstants.kLiftRightMotorId,MotorType.kBrushless);

    //Create PID controller
    algaeLeaderPidController = algaeLeaderMotor.getClosedLoopController();
    //algaeRightPidController = algaeLeftMotor.getClosedLoopController();

    // Create the algae encoder
    algaeLeaderEncoder = algaeLeaderMotor.getEncoder();
    //algaeRightEncoder = algaeRightMotor.getEncoder();

    //Calls motors config
    initializeMotors();
    algaeLeaderEncoder.setPosition(0);
    

    
  }

  
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    // Configure the algae motor
     globalConfig
    .idleMode(AlgaeConstants.kAlgaeIdleMode) //LiftConstants.kLiftIdleMode
    .smartCurrentLimit(AlgaeConstants.kAlgaeCurrentLimit) //LiftConstants.kLiftCurrentLimit
    .inverted(false);

    //Config follower motor
    /*followerConfig
    .apply(globalConfig)
    //.inverted(true)
    .follow(algaeLeaderMotor, true);*/
    

    // Configure the algae encoder 
    globalConfig.encoder
    .positionConversionFactor(AlgaeConstants.kAlgaePositionConversionFactor)
    .velocityConversionFactor(AlgaeConstants.kAlgaeVelocityConversionFactor);

    globalConfig.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the algae PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(AlgaeConstants.kPAlgaeUp, 
          AlgaeConstants.kIAlgaeUp, 
          AlgaeConstants.kDAlgaeUp, 
          AlgaeConstants.kAlgaeUpFF)

    //slot 1
    .pidf(AlgaeConstants.kPAlgaeDown, 
          AlgaeConstants.kIAlgaeDown, 
          AlgaeConstants.kDAlgaeDown, 
          AlgaeConstants.kAlgaeDownFF,
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
    

    

    // Configure the algae motor - like burn flash
    algaeLeaderMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
    /*algaeFollowerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);*/

  }

  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Algae Position", getAlgaePosition());
  }

  /**
   * Set the algae motor speed
   * @param algaeMotorSpeed
   */
  public void moveAlgae(double targetPosition, boolean direction)
  {
    
    if (direction)
    { 
      m_controller.setGoal(targetPosition);
      System.out.println("Switching to Position Control UP");
      //algaeLeaderPidController.setReference(targetPosition, ControlType.kPosition);
     
      algaeLeaderMotor.setVoltage(m_controller.calculate(algaeLeaderEncoder.getPosition()));
      System.out.println("Current Position: " + algaeLeaderEncoder.getPosition());
    }
    else
    {
      System.out.println("Switching to Position Control DOWN");
      /*algaeLeaderPidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      algaeLeaderPidController.setReference( targetPosition, 
                                            ControlType.kMAXMotionPositionControl,
                                            ClosedLoopSlot.kSlot2);
      System.out.println("Current Position: " + algaeLeaderEncoder.getPosition());
    }
    
  }

  /**
   * Stop the algae motor
   * @ Stop the algae motor
   */
  public void stopAlgae()
  {
    algaeLeaderPidController.setReference(0, ControlType.kDutyCycle);
    
  }

  /**
   * Get algae position
   * @return
   */
  public double getAlgaePosition()
  {
    return algaeLeaderEncoder.getPosition();
  }
 
  /**
   * Set the algae encoder position
   * @param position
   */
  public void resetAlgaeEncoder()
  {
    algaeLeaderEncoder.setPosition(0);
    
  }

  
}

