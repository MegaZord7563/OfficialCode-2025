// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix6.StatusCode;
//CTRE imports
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.Slot2Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//WPILib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static edu.wpi.first.units.Units.*;
//Robot imports
import frc.robot.Constants.SubsystemsConstants.EscaladorConstants;

public class EscaladorSubsystem extends SubsystemBase 
{

  //TalonFX motors
  private final TalonFX escaladorLeaderMotor;
  //private final TalonFX escaladorFollowerMotor;

  /* Start at velocity 0, use slot 0 */
   //private final MotionMagicVoltage m_mmReq = new MotionMagicVoltage(0).withSlot(2);

  //private final VelocityVoltage m_velocityVoltage = new VelocityVoltage(0).withSlot(1);

  /* Start at velocity 0, use slot 1 */
  private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);

  /* Keep a brake request so we can disable the motor */
  //private final NeutralOut m_brake = new NeutralOut();
  
  //Global config
  //private TalonFXConfiguration globalConfig = new TalonFXConfiguration();

  /** Creates a new EscaladorSubsystem. */
  public EscaladorSubsystem() 
  {
    //Instantiate TalonFX motors
    escaladorLeaderMotor = new TalonFX (EscaladorConstants.kMotor1Id);
    //escaladorFollowerMotor = new TalonFX(EscaladorConstants.kMotor2Id);

    //Configure motors
    configMotors();
    resetEncoder();

  }

  /**
   * @see https://v6.docs.ctr-electronics.com/en/stable/docs/hardware-reference/talonfx/improving-performance-with-current-limits.html
   * Tunning TalonFx PIDController
   * @see https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/device-specific/talonfx/basic-pid-control.html
   * Config drive motors
   * @category TalonFx settings for drive motors
   * 
   */

  /**
   * Config motor
   */
  private void configMotors()
  { 
    TalonFXConfiguration globalConfig = new TalonFXConfiguration();

    escaladorLeaderMotor.clearStickyFaults();
    //Factory Default
    escaladorLeaderMotor.getConfigurator().apply(new TalonFXConfiguration());      
    
    escaladorLeaderMotor.setNeutralMode(EscaladorConstants.kNeutralMode);
   
    
    /* Configure a stator limit of 20 amps */
   
    CurrentLimitsConfigs currentLimitConfigs = globalConfig.CurrentLimits;
    currentLimitConfigs.StatorCurrentLimit = EscaladorConstants.kCurrentThreshold;
    currentLimitConfigs.StatorCurrentLimitEnable = EscaladorConstants.kEnableCurrentLimit; 
    
    /*MNL 10/28/2024 precisa testar as 3 configs */
    /* Gear Ratio Config */
    globalConfig.Feedback.SensorToMechanismRatio = 1;
    globalConfig.ClosedLoopGeneral.ContinuousWrap = true;
    /* Closed Loop Ramping */
    globalConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = EscaladorConstants.kClosedLoopRamp;
    
    /* User can optionally change the configs or leave it alone to perform a factory default */
    //globalConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    
   
    
    /* Configure Motion Magic */
    /*
    MotionMagicConfigs mm = globalConfig.MotionMagic;
    mm.withMotionMagicCruiseVelocity(RotationsPerSecond.of(5)) // 5 (mechanism) rotations per second cruise
      .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(10)) // Take approximately 0.5 seconds to reach max vel
      // Take approximately 0.1 seconds to reach max accel 
      .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(100));
    
    //*/
    // in init function, set slot 0 gains
    //var slot0Configs = new Slot0Configs();
        
    /* Voltage-based VELOCITY requires a velocity feed forward to account for the back-emf of the motor 
    globalConfig.Slot1.kS = EscaladorConstants.KS; //0.1; // Add 0.1 V output to overcome static friction
    globalConfig.Slot1.kV = EscaladorConstants.KV; //0.12; // A velocity target of 1 rps results in 0.12 V output
    globalConfig.Slot1.kP = EscaladorConstants.kP; //0.11; // An error of 1 rps results in 0.11 V output
    globalConfig.Slot1.kI = EscaladorConstants.kI; //0; // no output for integrated error
    globalConfig.Slot1.kD = EscaladorConstants.kD; //0; // no output for error derivative
    // Peak output of 10 volts
    globalConfig.Voltage.withPeakForwardVoltage(Volts.of(8))
                        .withPeakReverseVoltage(Volts.of(-8));*/

    

    // in init function, set slot 1 gains
    //var slot1Configs = new Slot1Configs();
    //Voltage-based POSITION does not require a feed forward 
    globalConfig.Slot0.kP = EscaladorConstants.kP1; //0.11; // An error of 1 rps results in 0.11 V output
    globalConfig.Slot0.kI = EscaladorConstants.kI1; //0; // no output for integrated error
    globalConfig.Slot0.kD = EscaladorConstants.kD1; //0; // no output for error derivative
    


    // in init function, set slot 2 gains to apply motion magic
    //var slot2Configs = new Slot2Configs();
    /*globalConfig.Slot2.kS = 0.25; // Add 0.25 V output to overcome static friction
    globalConfig.Slot2.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    globalConfig.Slot2.kP = 4.8; // A position error of 2.5 rotations results in 12 V output
    globalConfig.Slot2.kI = 0; // no output for integrated error
    globalConfig.Slot2.kD = 0.1; // A velocity error of 1 rps results in 0.1 V output
  */
    /* Retry config apply up to 5 times, report if failure */
    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for (int i = 0; i < 5; ++i) {
      status = escaladorLeaderMotor.getConfigurator().apply(globalConfig);
      if (status.isOK()) break;
    }
    if (!status.isOK()) {
      System.out.println("Could not apply configs, error code: " + status.toString());
    }
     /* Set up followers to follow leaders */
    //escaladorFollowerMotor.setControl(new Follower(escaladorLeaderMotor.getDeviceID(), false));

    //Enable safety
    //escaladorLeaderMotor.setSafetyEnabled(true);

    /* And initialize encoder position to 0 */
    resetEncoder();



  }

  public void resetEncoder()
  {
    escaladorLeaderMotor.setPosition(0);
  }

  
  @Override
  public void periodic() 
  {
    escaladorLeaderMotor.getPosition().getValueAsDouble();
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Escalador Velocity", getEscaladorVelocity());
    SmartDashboard.putNumber("Escalador Position", getEscaladorPosition());
  }

  /**
   * Run motors
   * @param targetSetpoint
   * @param isPositionControl
   */
  public void runMotors( double targetSetpoint, boolean isPositionControl)
  {
    if(isPositionControl)
    {
      //Set position control is slot 1
      System.out.println("Switching to Position Control");
      escaladorLeaderMotor.setControl(m_positionVoltage.withPosition(targetSetpoint));
      
      //Motion Magic - Set position control is slot 1
      //escaladorLeaderMotor.setControl(m_mmReq.withPosition(targetSetpoint).withSlot(2));
      System.out.println("Current Position: " + escaladorLeaderMotor.getPosition().getValue());
    }
    else
    {
      //Set velocity control is slot 0  
      System.out.println("Switching to Velocity Control");
      //escaladorLeaderMotor.setControl(m_velocityVoltage.withSlot(1).withVelocity(targetSetpoint));
      System.out.println("Target Velocity: " + targetSetpoint);
      System.out.println("Current Velocity: " + escaladorLeaderMotor.getVelocity().getValue());
    }

    

  }

  /**
   * Stop motors
   */
  public void stopMotors()
  {
    /* Disable the motor instead */
    escaladorLeaderMotor.set(0);
  }

  public double getEscaladorPosition()
  {
    return escaladorLeaderMotor.getPosition().getValueAsDouble();
  }
  
  public double getEscaladorVelocity()
  {
    return escaladorLeaderMotor.getVelocity().getValueAsDouble();
  }

}
