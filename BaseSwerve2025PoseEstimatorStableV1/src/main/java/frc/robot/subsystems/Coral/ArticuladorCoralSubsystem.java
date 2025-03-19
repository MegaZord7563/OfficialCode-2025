// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Coral;



import java.util.function.Supplier;

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
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
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

  // The sensor
  private final DigitalInput sensor = new DigitalInput(0);
  

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

    // Set the soft limits to stop the motor at -50 and 50 rotations
    config.softLimit
    .forwardSoftLimit(ArticuladorCoralConstants.kSoftLimitMax)
    .forwardSoftLimitEnabled(true)
    .reverseSoftLimit(ArticuladorCoralConstants.kSoftLimitMin)
    .reverseSoftLimitEnabled(false);
  
    // Configure the coral encoder 
    config.encoder
    .positionConversionFactor(ArticuladorCoralConstants.kArticuladorCoralPositionConversionFactor)
    .velocityConversionFactor(ArticuladorCoralConstants.kArticuladorCoralVelocityConversionFactor);
    

    config.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the coral PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(ArticuladorCoralConstants.kPslot0, 
          ArticuladorCoralConstants.kIslot0, 
          ArticuladorCoralConstants.kDslot0, 
          ArticuladorCoralConstants.kFFslot0)
    
    .pidf(ArticuladorCoralConstants.kPslot1,
          ArticuladorCoralConstants.kIslot1, 
          ArticuladorCoralConstants.kDslot1, 
          ArticuladorCoralConstants.kFFslot1,ClosedLoopSlot.kSlot1)

    .pidf(ArticuladorCoralConstants.kPslot2,
          ArticuladorCoralConstants.kIslot2, 
          ArticuladorCoralConstants.kDslot2, 
          ArticuladorCoralConstants.kFFslot2,ClosedLoopSlot.kSlot2);   
          
    config.closedLoop.maxMotion
    // Set MAXMotion parameters for position control. We need to pass
    // a closed loop slot to the maxMotion method to set the parameters for that slot.
    // In this case, we are setting the parameters for slot 2.
    .maxVelocity(20000, ClosedLoopSlot.kSlot1)
    .maxAcceleration(40000, ClosedLoopSlot.kSlot1)
    .allowedClosedLoopError(0.5, ClosedLoopSlot.kSlot1)
    
    // Set MAXMotion parameters for velocity control in slot 2
    .maxAcceleration(500, ClosedLoopSlot.kSlot2)
    .maxVelocity(1000, ClosedLoopSlot.kSlot2)
    .allowedClosedLoopError(0.5, ClosedLoopSlot.kSlot2);

    // Configure the coral motor - like burn flash
    coralMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//


  }

  

  @Override
  public void periodic() 
  {
    boolean sensorValue = sensor.get();
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Articulardor Coral Position", getPosition());


    SmartDashboard.putBoolean("Sensor value", sensorValue);
    /* 
    if (sensorValue)
    {
      SmartDashboard.putBoolean("Sensor value", sensorValue);
    }
    else
    {
      SmartDashboard.putBoolean("Sensor value", sensorValue);
    }
      */
  }

  /**
   * Set the artic coral motor speed
   * @param targetPosition
   */
  public void runMotor(double targetPosition)
  {
    
      System.out.println(" Artic Coral Position Control Enabled");
      /*coralPidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
     */  
      /*coralPidController.setReference(targetPosition, 
                                            ControlType.kPosition);*/

      coralPidController.setReference(targetPosition, 
                                      ControlType.kMAXMotionPositionControl,
                                      ClosedLoopSlot.kSlot1);
      System.out.println("Artic Coral Current Position: " + coralEncoder.getPosition());
    
    
  }

  /**
   * Stop the coral motor
   * @ Stop the coral motor
   */
  public void stopMotor()
  {
    coralPidController.setReference(0, ControlType.kPosition);
    
  }

  /**
   * Get coral position
   * @return
   */
  public double getPosition()
  {
    return coralEncoder.getPosition();
  }
 
  /**
   * Set the coral encoder position
   * @param position
   */
  public void resetEncoder()
  {
    coralEncoder.setPosition(0);
    
  }

  public void setVelocity(double speed)
  {
    coralMotor.set(speed);
  }

  public Supplier<Boolean> isHomed()
  {
    if (coralEncoder.getPosition() <=3)
    {
      return ()->true;
    }else 
    {
      return ()->false;
    }
  }

  public Supplier<Boolean> isOnPosition()
  {
    if (coralEncoder.getPosition() <= 44)
    {
      return ()->true;
    }else 
    {
      return ()->false;
    }
  }

  public Supplier<Boolean> isOnPositionAlgae(double position)
  {
    if (coralEncoder.getPosition() > position)
    {
      return ()->true;
    }else 
    {
      return ()->false;
    }
  }

  public boolean getAngle(double angle)
  {
    if (coralEncoder.getPosition() >= angle)
    {
      return true;
    }else 
    {
      return false;
    }
  }

  /**
   * Detect if the coral is in the intake position
   * @return sensor state
   */
  public boolean sensorCoral ()
  {
    return sensor.get();
  }
}
