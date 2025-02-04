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


//WPILIB Imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SubsystemsConstants.CoralIntakeConstants;

//Robot Imports


/** Add your docs here. */
public class CoralSubsystem extends SubsystemBase 
{
  // The lift motor
  private final SparkMax coralMotor;
  private SparkMaxConfig globalConfig = new SparkMaxConfig();
  
  

  // The lift encoder
  private final RelativeEncoder coralEncoder;
  

  // The lift PID controller
  private SparkClosedLoopController coralPidController;
  


  /** Creates a new LiftSubsytem. */
  public CoralSubsystem() 
  {
    // Create the  motor
    coralMotor = new SparkMax(CoralIntakeConstants.kCoralMotorId, MotorType.kBrushless);
    

    // Create the  encoder
    coralEncoder = coralMotor.getEncoder();
    //liftRightEncoder = liftRightMotor.getEncoder();

    //Create PID controller
    coralPidController = coralMotor.getClosedLoopController();
    

    //Calls motors config
    motorsConfig();
    

    
  }

  
  public void motorsConfig()
  {
    globalConfig.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

    // Configure the lift motor
     globalConfig
    .idleMode(CoralIntakeConstants.kCoralIdleMode)
    .smartCurrentLimit(CoralIntakeConstants.kCoralCurrentLimit)
    .inverted(false);

    // Configure the lift encoder 
    globalConfig.encoder
    .velocityConversionFactor(CoralIntakeConstants.kCoralVelocityConversionFactor);

    // Configure the lift PID controller
    globalConfig.closedLoop
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(CoralIntakeConstants.kP, 
          CoralIntakeConstants.kI, 
          CoralIntakeConstants.kD, 
          CoralIntakeConstants.kFF);

    // Configure the lift motor - like burn flash
    coralMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
    
  }

  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }

  /**
   * Set the coral motor speed
   * @param CoralMotorSpeed
   */
  public void runCoral(double targetSpeed)
  {
     // Set the lift motor speed
      coralPidController.setReference(targetSpeed, ControlType.kVelocity);
    
    
  }

  /**
   * Stop the lift motor
   * 
   */
  public void stopMotor()
  {
    coralMotor.set(0);
    
  }

  public double getCoralSpeed()
  {
    return coralEncoder.getVelocity();
  }
 
  

  
}

