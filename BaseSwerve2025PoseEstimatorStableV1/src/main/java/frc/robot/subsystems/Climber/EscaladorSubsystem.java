// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Climber;

//WPILib imports
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Java imports

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


//Robot imports
import frc.robot.Constants.SubsystemsConstants.EscaladorConstants;

public class EscaladorSubsystem extends SubsystemBase 
{

  //TalonFX motors
 
  //private final TalonFX escaladorFollowerMotor;

  /* Start at motion 0, use slot 2 */
   //private final MotionMagicVoltage m_mmReq = new MotionMagicVoltage(0).withSlot(2);

  //private final VelocityVoltage m_velocityVoltage = new VelocityVoltage(0).withSlot(1);

  //
  //private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);

  /* Keep a brake request so we can disable the motor */
  //private final NeutralOut m_brake = new NeutralOut();
  // The Coral motor
  private final SparkMax escaladorMotor;
  private SparkMaxConfig config = new SparkMaxConfig();
  
  // The Coral encoder
  private final RelativeEncoder escaladorEncoder;


  // The alga PID controller
  private final SparkClosedLoopController escaladorPidController;
  //Global config
  //private TalonFXConfiguration globalConfig = new TalonFXConfiguration();

  /** Creates a new EscaladorSubsystem. */
  public EscaladorSubsystem() 
  {
    //Instantiate TalonFX motors
    escaladorMotor = new SparkMax (EscaladorConstants.kMotor1Id, MotorType.kBrushless);
    //escaladorFollowerMotor = new TalonFX(EscaladorConstants.kMotor2Id, "rio");

    //Create PID controller
    escaladorPidController = escaladorMotor.getClosedLoopController();
    

    // Create the coral encoder
    escaladorEncoder = escaladorMotor.getEncoder();

    //Initialize motors
    initializeMotors();
    //resetEncoder();

  }
 
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    // Configure the coral motor
     config
    .idleMode(EscaladorConstants.kIdleMode) //LiftConstants.kLiftIdleMode
    .smartCurrentLimit(EscaladorConstants.kCurrentLimit) //LiftConstants.kLiftCurrentLimit
    .inverted(true);

    // Set the soft limits to stop the motor at -50 and 50 rotations
    config.softLimit
    .forwardSoftLimit(EscaladorConstants.kSoftLimitMax)
    .forwardSoftLimitEnabled(false)
    .reverseSoftLimit(EscaladorConstants.kSoftLimitMin)
    .reverseSoftLimitEnabled(false);
  
    // Configure the coral encoder 
    config.encoder
    .positionConversionFactor(EscaladorConstants.kPositionConversionFactor)
    .velocityConversionFactor(EscaladorConstants.kVelocityConversionFactor);
    

    config.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    // Configure the coral PID controller
   
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(EscaladorConstants.kP, 
          EscaladorConstants.kI, 
          EscaladorConstants.kD, 
          EscaladorConstants.kFF);      
          
  config.closedLoop.maxMotion
    // Set MAXMotion parameters for position control. We need to pass
    // a closed loop slot to the maxMotion method to set the parameters for that slot.
    // In this case, we are setting the parameters for slot 0.
    .maxVelocity(500.0)//m/s
    .maxAcceleration(250)//m/s
    .allowedClosedLoopError(0.05)
    // Set MAXMotion parameters for velocity control in slot 2
    .maxAcceleration(500, ClosedLoopSlot.kSlot1)
    .maxVelocity(1000, ClosedLoopSlot.kSlot1)
    .allowedClosedLoopError(0.01, ClosedLoopSlot.kSlot1);
    

    // Configure the coral motor - like burn flash
    escaladorMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//


  }
  

  /**
   * @ reset encoder
   */
  public void resetEncoder()
  {
    escaladorEncoder.setPosition(0);
  }

  
  @Override
  public void periodic() 
  {
    
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Escalador Velocity", getVelocity());
    SmartDashboard.putNumber("Escalador Position", getPosition());
    SmartDashboard.putNumber("EscalatorCurrent", escaladorMotor.getOutputCurrent());
    SmartDashboard.putNumber("EscalatorVoltage", escaladorMotor.getAppliedOutput());
    SmartDashboard.putNumber("EscalatorTemperature",escaladorMotor.getMotorTemperature());
  }

  /**
   * Run motors
   * @param targetSetpoint
   * @param isPositionControl
   */
  public void runMotors( double targetSetpoint,  boolean direction)
  {
    
    
    if(direction)
    {
      escaladorPidController.setReference(targetSetpoint, ControlType.kPosition);

      System.out.println("Escaldor  Current Position: " + getPosition());
    }
    else
    {
      escaladorPidController.setReference(targetSetpoint, ControlType.kMAXMotionPositionControl);
    }

    
      
      

    

  }

  /**
   * Stop motors
   */
  public void stopMotors()
  {
    /* Disable the motor instead */
    escaladorPidController.setReference(0.0, ControlType.kPosition);
  }

  public void setVelocity(double speed)
  {
    escaladorMotor.set(speed);
  }

  /**
   * 
   * @return position in rotations as double
   */
  public double getPosition()
  {
    return escaladorEncoder.getPosition();
  }
  
  /**
   * 
   * @return velocity in rotations/sec
   */
  public double getVelocity()
  {
    return escaladorEncoder.getVelocity();
  }

}
