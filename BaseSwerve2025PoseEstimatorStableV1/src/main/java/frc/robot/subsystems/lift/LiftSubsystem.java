// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.lift;



import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

//WPILIB Imports
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static edu.wpi.first.units.Units.*;

import frc.robot.Constants.ModuleConstants;
//Robot Imports
import frc.robot.Constants.SubsystemsConstants.LiftConstants;
import frc.robot.Utils.Conversions;

/** Add your docs here. */
public class LiftSubsystem extends SubsystemBase 
{
  // The lift motor
  //private final SparkMax liftLeaderMotor;
 // private SparkMaxConfig globalConfig = new SparkMaxConfig();
  private final TalonFX liftMotor;

  /* Start at motion 0, use slot 2 */
   //private final MotionMagicVoltage m_mmReq = new MotionMagicVoltage(0).withSlot(1);

  private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);

  //private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);

  /* Keep a brake request so we can disable the motor */
  private final NeutralOut m_brake = new NeutralOut();
  
  
  //private final SparkMax liftFollowerMotor;
  //private SparkMaxConfig followerConfig = new SparkMaxConfig();

  // The lift encoder
  //private final RelativeEncoder liftLeaderEncoder;
  //private final RelativeEncoder liftFollowerEncoder;

  // The lift PID controller
  //private final SparkClosedLoopController liftLeaderPidController;
  //private final SparkClosedLoopController liftFollowerPidController;


  /** Creates a new LiftSubsytem. */
  public LiftSubsystem() 
  {
    // Create the  motor
    liftMotor = new TalonFX (LiftConstants.kLiftLeftMotorId,"rio");
    // Create the lift motor
    /*liftLeaderMotor = new SparkMax(LiftConstants.kLiftLeftMotorId,MotorType.kBrushless);
    //liftFollowerMotor = new SparkMax(LiftConstants.kLiftRightMotorId,MotorType.kBrushless);

    // Create the lift encoder
    liftLeaderEncoder = liftLeaderMotor.getEncoder();
    //liftRightEncoder = liftRightMotor.getEncoder();

    //Create PID controller
    liftLeaderPidController = liftLeaderMotor.getClosedLoopController();
    //liftRightPidController = liftLeftMotor.getClosedLoopController();*/

    //Calls motors config
    //initializeMotors();
   
    
    
    // Configure the lift motor
    SmartDashboard.putBoolean("Homing Position", false);

    

    
  }

  /**
   * @see https://v6.docs.ctr-electronics.com/en/stable/docs/hardware-reference/talonfx/improving-performance-with-current-limits.html
   * Tunning TalonFx PIDController
   * @see https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/device-specific/talonfx/basic-pid-control.html
   * Config drive motors
   * @category TalonFx settings for drive motors
   * 
   */
  public void initializeMotors()
  { 
    
    

    liftMotor.clearStickyFaults();

    //Factory Default
    liftMotor.getConfigurator().apply(new TalonFXConfiguration());      
    
    liftMotor.setNeutralMode(LiftConstants.kNeutralMode);
   
    TalonFXConfiguration globalConfig = new TalonFXConfiguration();
    /* Configure a stator limit of 20 amps */
   
    CurrentLimitsConfigs currentLimitConfigs = globalConfig.CurrentLimits;
    currentLimitConfigs.StatorCurrentLimit = LiftConstants.kCurrentThreshold;
    currentLimitConfigs.StatorCurrentLimitEnable = LiftConstants.kEnableCurrentLimit; 
    
    
    /* Gear Ratio Config */
    globalConfig.Feedback.SensorToMechanismRatio =1;///LiftConstants.kLiftGearRatio; 
    //globalConfig.Feedback.RotorToSensorRatio=1;
    globalConfig.ClosedLoopGeneral.ContinuousWrap = false;//keep default = FALSE - MUST NOT TOUCH IT
    /* Closed Loop Ramping */
    globalConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = LiftConstants.kClosedLoopRamp;
    
    /* User can optionally change the configs or leave it alone to perform a factory default */
    globalConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    
               
    // Voltage-based VELOCITY requires a velocity feed forward to account for the back-emf of the motor 
    //globalConfig.Slot0.kS = LiftConstants.KS; //0.1; // Add 0.1 V output to overcome static friction
    //globalConfig.Slot0.kV = LiftConstants.KV; //0.12; // A velocity target of 1 rps results in 0.12 V output
    globalConfig.Slot0.kP = LiftConstants.kP; //0.11; // An error of 1 rps results in 0.11 V output
    globalConfig.Slot0.kI = LiftConstants.kI; //0; // no output for integrated error
    globalConfig.Slot0.kD = LiftConstants.kD; //0; // no output for error derivative*/
    // Peak output of 10 volts
    //globalConfig.Voltage.withPeakForwardVoltage(Volts.of(10))
                        //.withPeakReverseVoltage(Volts.of(-10));

    /* Configure Motion Magic */
    MotionMagicConfigs mm = globalConfig.MotionMagic;
    mm.withMotionMagicCruiseVelocity(RotationsPerSecond.of(5)) // 5 (mechanism) rotations per second cruise
      .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(10)) // Take approximately 0.5 seconds to reach max vel
      // Take approximately 0.1 seconds to reach max accel 
      .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(100));

    globalConfig.Slot1.kP = LiftConstants.kP1; //0.11; // An error of 1 rps results in 0.11 V output
    globalConfig.Slot1.kI = LiftConstants.kI1; //0; // no output for integrated error
    globalConfig.Slot1.kD = LiftConstants.kD1; //0; // no output for error derivative*/
     
    /* Retry config apply up to 5 times, report if failure */
    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for (int i = 0; i < 5; ++i) 
    {
      status = liftMotor.getConfigurator().apply(globalConfig);
      if (status.isOK()) break;
    }
    if (!status.isOK()) 
    {
      System.out.println("Could not apply escalador configs, error code: " + status.toString());
    }
     

    //Enable safety
    //escaladorLeaderMotor.setSafetyEnabled(true);

    /* And initialize encoder position to 0 */
    resetEncoder();
    

  }

  
  /**
   * @ motors initialization
   */
  /*public void initializeMotors()
  {
    
    globalConfig.closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

    // Configure the lift motor
     globalConfig
    .idleMode(LiftConstants.kLiftIdleMode)
    .smartCurrentLimit(LiftConstants.kLiftCurrentLimit)
    .inverted(false);

    // Set the soft limits to stop the motor at -50 and 50 rotations
    globalConfig.softLimit
    .forwardSoftLimit(LiftConstants.kSoftLimitMax)
    .forwardSoftLimitEnabled(true)
    .reverseSoftLimit(LiftConstants.kSoftLimitMin)
    .reverseSoftLimitEnabled(true);


    //Config follower motor
    /*followerConfig
    .apply(globalConfig)
    //.inverted(true)
    .follow(liftLeaderMotor, true);
    

    // Configure the lift encoder 
    globalConfig.encoder
    .positionConversionFactor(LiftConstants.kLiftPositionConversionFactor)
    .velocityConversionFactor(LiftConstants.kLiftVelocityConversionFactor);

    // Configure the lift PID controller
    globalConfig.closedLoop
    // Set the PID gains for the turning motor. Note these are example gains, and you may need to tune them for your own robot!
    // slot, as it will default to slot 0.
    .pidf(LiftConstants.kPLiftUp, 
          LiftConstants.kILiftUp, 
          LiftConstants.kDLiftUp, 
          LiftConstants.kLiftUpFF)

    //slot 1
    .pidf(LiftConstants.kPLiftDown, 
          LiftConstants.kILiftDown, 
          LiftConstants.kDLiftDown, 
          LiftConstants.kLiftDownFF,
          ClosedLoopSlot.kSlot2)
     
    // Enable PID wrap around for the turning motor. 
    //This will allow the PID controller to go through 0 to get to the setpoint i.e. 
    //going from 350 degrees to 10 degrees will go through 0 rather than the other direction which is a longer route.
    .positionWrappingEnabled(false)  //01/28 MNL - it was true before                                         
    .positionWrappingMinInput(LiftConstants.kLiftEncoderPositionPIDMinInput) 
    .positionWrappingMaxInput(LiftConstants.kLiftEncoderPositionPIDMaxInput)  
    .outputRange( LiftConstants.kLiftMinOutput,// Set the output range of the PID controller.                                 
                  LiftConstants.kLiftMaxOutput,
                  LiftConstants.pidSparkSlot);// Set the output range of the PID controller.
  
    //Max motion config
    globalConfig.closedLoop.maxMotion
    // Set MAXMotion parameters for position control. We need to pass
    // a closed loop slot to the maxMotion method to set the parameters for that slot.
    // In this case, we are setting the parameters for slot 2.
    .maxVelocity(5000)
    .maxAcceleration(1000)
    .allowedClosedLoopError(0.01)
    // Set MAXMotion parameters for velocity control in slot 2
    .maxAcceleration(500, ClosedLoopSlot.kSlot2)
    .maxVelocity(6000, ClosedLoopSlot.kSlot2)
    .allowedClosedLoopError(0.01, ClosedLoopSlot.kSlot2);

    

    // Configure the lift motor - like burn flash
    liftLeaderMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//
   // liftFollowerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);//

  }*/

   /**
   * 
   * @return driveMotor position as double
   */
  public double getPositionMeters() 
  {
      return Conversions.rotationsToMeters(liftMotor.getPosition().getValueAsDouble(),LiftConstants.kLiftTuboDiam);
  }
  

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Lift Position", getPosition());
    SmartDashboard.putNumber( "Lift Velocity rad/s",liftMotor.getVelocity().getValueAsDouble());
  }

 /**
  * lift position and direction
  * @param targetPosition
  * @param direction
  */
  public void runMotor(double targetPosition, boolean direction)
  {

    // Set the lift motor speed slot0
      liftMotor.setControl( m_positionVoltage.withPosition(targetPosition));

      //Motion Magic - Set position control is slot 1
      //liftMotor.setControl(m_mmReq.withPosition(targetPosition).withSlot(1));

      System.out.println("Lift Current Position: " + this.getPosition());
    
    
    /* 
    
    if (direction)
    {
      System.out.println("Switching to Position Control UP");
      liftLeaderPidController.setReference(targetPosition,  ControlType.kMAXMotionPositionControl,
                                                            ClosedLoopSlot.kSlot2);
    }
    else
    {
      System.out.println("Switching to Position Control DOWN");
      /*liftLeaderPidController.setReference(targetPosition, 
                                            ControlType.kPosition,
                                            ClosedLoopSlot.kSlot1);
       liftLeaderPidController.setReference(targetPosition, 
                                            ControlType.kMAXMotionPositionControl,
                                            ClosedLoopSlot.kSlot2);*/
  }
    
  

  /**
   * Stop the lift motor
   * @
   */
  public void stopMotor()
  {
    liftMotor.setControl( m_brake);
    
  }

  /**
   * 
   * @return
   */
  public double getPosition()
  {
    
    return liftMotor.getPosition().getValueAsDouble()*LiftConstants.kLiftGearRatio;
  }
 
  /**
   * 
   * @ reset encoder
   */
  public void resetEncoder()
  {
    liftMotor.setPosition(0);
    
  }

}
  



