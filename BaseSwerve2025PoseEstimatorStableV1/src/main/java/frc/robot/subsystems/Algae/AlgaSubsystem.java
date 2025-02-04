// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Algae;


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
import frc.robot.Constants.SubsystemsConstants.AlgaIntakeConstants;


//Robot Imports


/** Add your docs here. */
public class AlgaSubsystem extends SubsystemBase 
{
  // The lift motor
  private final SparkMax algaMotor;
  private SparkMaxConfig globalConfig = new SparkMaxConfig();
  
  

  // The lift encoder
  private final RelativeEncoder algaEncoder;
  

  // The lift PID controller
  private SparkClosedLoopController algaPidController;
  


  /** Creates a new LiftSubsytem. */
  public AlgaSubsystem() 
  {
    // Create the  motor
    algaMotor = new SparkMax(AlgaIntakeConstants.kAlgaMotorId, MotorType.kBrushless);
    

    // Create the  encoder
    algaEncoder = algaMotor.getEncoder();
    //liftRightEncoder = liftRightMotor.getEncoder();

    //Create PID controller
    algaPidController = algaMotor.getClosedLoopController();
    

    //Calls motors config
    motorsConfig();
    

  }

  

  
  public void motorsConfig()
  {
    globalConfig.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

    // Configure the lift motor
     globalConfig
    .idleMode(AlgaIntakeConstants.kAlgaIdleMode)
    .smartCurrentLimit(AlgaIntakeConstants.kAlgaCurrentLimit)
    .inverted(false);

    // Configure the lift encoder 
    globalConfig.encoder
    .velocityConversionFactor(AlgaIntakeConstants.kAlgaVelocityConversionFactor);

    // Configure the lift PID controller
    globalConfig.closedLoop
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(AlgaIntakeConstants.kP, 
          AlgaIntakeConstants.kI, 
          AlgaIntakeConstants.kD, 
          AlgaIntakeConstants.kFF);

    // Configure the lift motor - like burn flash
    algaMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
    
  }

  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }

  /**
   * Set the coral motor speed
   * @param AlgaMotorSpeed
   */
  public void startAlga(double targetSpeed)
  {
    
      algaPidController.setReference(targetSpeed, ControlType.kVelocity);
    
    
  }

  /**
   * Stop the lift motor
   * 
   */
  public void stopMotor()
  {
    algaMotor.set(0);
    
  }

  public double getCoralSpeed()
  {
    return algaEncoder.getVelocity();
  }
 
  

  
}

