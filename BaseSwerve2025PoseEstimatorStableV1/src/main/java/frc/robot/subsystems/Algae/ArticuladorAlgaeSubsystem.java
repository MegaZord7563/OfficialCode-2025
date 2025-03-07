// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Algae;



import java.util.function.Supplier;

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
    
    config.softLimit
    .forwardSoftLimit(ArticuladorAlgaeConstants.kSoftLimitMax)
    .forwardSoftLimitEnabled(true)
    .reverseSoftLimit(ArticuladorAlgaeConstants.kSoftLimitMin)
    .reverseSoftLimitEnabled(true);

    
    config.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the algae PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(ArticuladorAlgaeConstants.kPslot0, 
          ArticuladorAlgaeConstants.kIslot0, 
          ArticuladorAlgaeConstants.kDslot0, 
          ArticuladorAlgaeConstants.kFFslot0);
              


    //Max motion config
    config.closedLoop.maxMotion
    // Set MAXMotion parameters for position control. We need to pass
    // a closed loop slot to the maxMotion method to set the parameters for that slot.
    // In this case, we are setting the parameters for slot 0.
    // Set MAXMotion parameters for velocity control in slot 1
    .maxAcceleration(35000)
    .maxVelocity(35000)
    .allowedClosedLoopError(0.5);
    
    // Configure the algae motor - like burn flash
    algaeMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
    

  }

  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Algae Position", getPosition());
  }

  /**
   * Set the algae motor speed
   * @param algaeMotorSpeed
   */
  public void runMotor(double targetPosition)
  {
      /*algaePidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      algaePidController.setReference(targetPosition, 
                                            ControlType.kMAXMotionPositionControl);

      System.out.println("Current Position: " + algaeEncoder.getPosition());
    
    
  }

  public void setVelocity(double speed){
    algaeMotor.set(speed);
  }

  /**
   * Stop the algae motor
   * @ Stop the algae motor
   */
  public void stopMotor()
  {
    algaePidController.setReference(0, ControlType.kDutyCycle);
    
  }

  /**
   * Get algae position
   * @return
   */
  public double getPosition()
  {
    return algaeEncoder.getPosition();
  }
 


  /**
   * Return true if the algae is homed
   * @return
   */

  public Supplier<Boolean> isHomed()
  {
    if (algaeEncoder.getPosition()<= 8)
    {
      return ()->true;

    }
    else 
    {
      return ()->false;
    }
    
  }

  /**
   * Return true if the algae is in position
   * @param position
   * @return
   */
  public Supplier<Boolean> inPosition(double position)
  {
    if (algaeEncoder.getPosition()>= position)
    {
      return ()->true;

    }
    else 
    {
      return ()->false;
    }
    
  }

  /**
   * Set the algae encoder position
   * @param positionFlag
   */
  public void resetEncoder()
  {
    algaeEncoder.setPosition(0);
    
  }

  
}

