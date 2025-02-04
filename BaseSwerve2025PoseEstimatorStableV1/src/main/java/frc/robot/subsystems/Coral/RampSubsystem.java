// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Coral;



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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//WPILIB Imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Robot Imports
import frc.robot.Constants.SubsystemsConstants.RampConstants;

/** Add your docs here. */
public class RampSubsystem extends SubsystemBase 
{
  // The Ramp motor
  private final SparkMax rampMotor;
  private SparkMaxConfig config = new SparkMaxConfig();
  
  // The Ramp encoder
  private final RelativeEncoder rampEncoder;
  
  // The alga PID controller
  private final SparkClosedLoopController rampPidController;

  /** Creates a new rampSubsytem. */
  public RampSubsystem() 
  {
    // Create the ramp motor
    rampMotor = new SparkMax(RampConstants.kRampMotorId,MotorType.kBrushless);

    //Create PID controller
    rampPidController = rampMotor.getClosedLoopController();

    // Create the ramp encoder
    rampEncoder = rampMotor.getEncoder();

    //Calls motors config
    initializeMotors();
    rampEncoder.setPosition(0);
    

    
  }

  
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    // Configure the ramp motor
     config
    .idleMode(RampConstants.kRampIdleMode) //LiftConstants.kLiftIdleMode
    .smartCurrentLimit(RampConstants.kRampCurrentLimit) //LiftConstants.kLiftCurrentLimit
    .inverted(false);

    // Configure the ramp encoder 
    config.encoder
    .positionConversionFactor(RampConstants.kRampPositionConversionFactor)
    .velocityConversionFactor(RampConstants.kRampVelocityConversionFactor);

    config.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the ramp PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(RampConstants.kPRampUp, 
          RampConstants.kIRampUp, 
          RampConstants.kDRampUp, 
          RampConstants.kRampUpFF);
     

    // Configure the ramp motor - like burn flash
    rampMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
   

  }

  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Ramp Position", getRampPosition());
  }

  /**
   * Set the ramp motor speed
   * @param rampMotorSpeed
   */
  public void moveRamp(double targetPosition)
  {
    
      System.out.println("Ramp Position Control Enabled");
      /*rampPidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      rampPidController.setReference(targetPosition, 
                                            ControlType.kPosition);
      System.out.println("Current Position: " + rampEncoder.getPosition());
    
    
  }

  /**
   * Stop the ramp motor
   * @ Stop the ramp motor
   */
  public void stopRamp()
  {
    rampPidController.setReference(0, ControlType.kDutyCycle);
    
  }

  /**
   * Get ramp position
   * @return
   */
  public double getRampPosition()
  {
    return rampEncoder.getPosition();
  }
 
  /**
   * Set the ramp encoder position
   * @param position
   */
  public void resetRampEncoder()
  {
    rampEncoder.setPosition(0);
    
  }

  
}

