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
import frc.robot.Constants.SubsystemsConstants.ArticuladorCoralConstants;

/** Add your docs here. */
public class ArticuladorCoralSubsystem extends SubsystemBase 
{
  // The Coral motor
  private final SparkMax coralMotor;
  private SparkMaxConfig config = new SparkMaxConfig();
  
  // The Coral encoder
  private final RelativeEncoder coralEncoder;


  // The alga PID controller
  private final SparkClosedLoopController coralPidController;
  

  /** Creates a new coralSubsytem. */
  public ArticuladorCoralSubsystem() 
  {
    // Create the coral motor
    coralMotor = new SparkMax(ArticuladorCoralConstants.kArticuladorCoralMotorId,MotorType.kBrushless);
  
    //Create PID controller
    coralPidController = coralMotor.getClosedLoopController();
    

    // Create the coral encoder
    coralEncoder = coralMotor.getEncoder();
    

    //Calls motors config
    initializeMotors();
    coralEncoder.setPosition(0);
    
    
  }

  
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    // Configure the coral motor
     config
    .idleMode(ArticuladorCoralConstants.kArticuladorCoralIdleMode) //LiftConstants.kLiftIdleMode
    .smartCurrentLimit(ArticuladorCoralConstants.kArticuladorCoralCurrentLimit) //LiftConstants.kLiftCurrentLimit
    .inverted(false);
  
    // Configure the coral encoder 
    config.encoder
    .positionConversionFactor(ArticuladorCoralConstants.kArticuladorCoralPositionConversionFactor);
    

    config.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the coral PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(ArticuladorCoralConstants.kPArticuladorCoralUp, 
    ArticuladorCoralConstants.kIArticuladorCoralUp, 
    ArticuladorCoralConstants.kDArticuladorCoralUp, 
    ArticuladorCoralConstants.kArticuladorCoralUpFF);          

    // Configure the coral motor - like burn flash
    coralMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//


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
  public void moveCoral(double targetPosition)
  {
    
      System.out.println("Coral Position Control Enabled");
      /*coralPidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      coralPidController.setReference(targetPosition, 
                                            ControlType.kPosition);
      System.out.println("Current Position: " + coralEncoder.getPosition());
    
    
  }

  /**
   * Stop the coral motor
   * @ Stop the coral motor
   */
  public void stopCoral()
  {
    coralPidController.setReference(0, ControlType.kDutyCycle);
    
  }

  /**
   * Get coral position
   * @return
   */
  public double getCoralPosition()
  {
    return coralEncoder.getPosition();
  }
 
  /**
   * Set the coral encoder position
   * @param position
   */
  public void resetCoralEncoder()
  {
    coralEncoder.setPosition(0);
    
  }

  
}
