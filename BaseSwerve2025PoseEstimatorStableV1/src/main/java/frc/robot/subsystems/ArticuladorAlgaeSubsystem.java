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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//WPILIB Imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Robot Imports
import frc.robot.Constants.SubsystemsConstants.ArticuladorAlgaeConstants;

/** Add your docs here. */
public class ArticuladorAlgaeSubsystem extends SubsystemBase 
{
  // The Algae motor
  private final SparkMax algaeMotor;
  private SparkMaxConfig config = new SparkMaxConfig();
  
  // The Algae encoder
  private final RelativeEncoder algaeEncoder;


  // The alga PID controller
  private final SparkClosedLoopController algaePidController;

  /** Creates a new algaeSubsytem. */
  public ArticuladorAlgaeSubsystem() 
  {
    // Create the algae motor
    algaeMotor = new SparkMax(ArticuladorAlgaeConstants.kArticuladorAlgaeMotorId,MotorType.kBrushless);
  
    //Create PID controller
    algaePidController = algaeMotor.getClosedLoopController();
    

    // Create the algae encoder
    algaeEncoder = algaeMotor.getEncoder();
    

    //Calls motors config
    initializeMotors();
    algaeEncoder.setPosition(0);
    

    
  }

  
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    // Configure the algae motor
     config
    .idleMode(ArticuladorAlgaeConstants.kArticuladorAlgaeIdleMode) 
    .smartCurrentLimit(ArticuladorAlgaeConstants.kArticuladorAlgaeCurrentLimit) 
    .inverted(false);
  
    // Configure the algae encoder 
    config.encoder
    .positionConversionFactor(ArticuladorAlgaeConstants.kArticuladorAlgaePositionConversionFactor);
    

    config.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the algae PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(ArticuladorAlgaeConstants.kPArticuladorAlgaeUp, 
          ArticuladorAlgaeConstants.kIArticuladorAlgaeUp, 
          ArticuladorAlgaeConstants.kDArticuladorAlgaeUp, 
          ArticuladorAlgaeConstants.kArticuladorAlgaeUpFF);
              
    
    // Configure the algae motor - like burn flash
    algaeMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
    

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
  public void moveAlgae(double targetPosition)
  {
      System.out.println("Algae Position Control Enabled");
      /*algaePidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      algaePidController.setReference(targetPosition, 
                                            ControlType.kPosition);
      System.out.println("Current Position: " + algaeEncoder.getPosition());
    
    
  }

  /**
   * Stop the algae motor
   * @ Stop the algae motor
   */
  public void stopAlgae()
  {
    algaePidController.setReference(0, ControlType.kDutyCycle);
    
  }

  /**
   * Get algae position
   * @return
   */
  public double getAlgaePosition()
  {
    return algaeEncoder.getPosition();
  }
 
  /**
   * Set the algae encoder position
   * @param position
   */
  public void resetAlgaeEncoder()
  {
    algaeEncoder.setPosition(0);
    
  }

  
}

