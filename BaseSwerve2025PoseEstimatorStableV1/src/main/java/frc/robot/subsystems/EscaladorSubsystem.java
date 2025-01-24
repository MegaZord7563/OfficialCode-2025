// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SubsystemsConstants.EscaladorConstants;

public class EscaladorSubsystem extends SubsystemBase 
{

  private final TalonFX escaladorLeaderMotor;
  private final TalonFX escaladorFollowerMotor;

  /* Start at velocity 0, use slot 0 */
  private final VelocityVoltage m_velocityVoltage = new VelocityVoltage(0).withSlot(0);

  /* Start at velocity 0, use slot 1 */
  private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(1);

  private TalonFXConfiguration globalConfig = new TalonFXConfiguration();

  /** Creates a new EscaladorSubsystem. */
  public EscaladorSubsystem() 
  {
    escaladorLeaderMotor = new TalonFX (EscaladorConstants.kMotor1Id);
    escaladorFollowerMotor = new TalonFX(EscaladorConstants.kMotor2Id);
    configMotor();
    motorPidControllerGains();

  }

  /**https://v6.docs.ctr-electronics.com/en/stable/docs/hardware-reference/talonfx/improving-performance-with-current-limits.html
   * Config drive motors
   * @category TalonFx settings for drive motors
   */

  private void configMotor()
  { 
    escaladorLeaderMotor.clearStickyFaults();
    //Factory Default
    escaladorLeaderMotor.getConfigurator().apply(new TalonFXConfiguration());      
    //driveMotor.setInverted(driveMotorReversed);
    escaladorLeaderMotor.setNeutralMode(EscaladorConstants.kNeutralMode);
   
    
    /* Configure a stator limit of 20 amps */
   
    CurrentLimitsConfigs currentLimitConfigs = globalConfig.CurrentLimits;
    currentLimitConfigs.StatorCurrentLimit = EscaladorConstants.kCurrentThreshold;
    currentLimitConfigs.StatorCurrentLimitEnable = EscaladorConstants.kEnableCurrentLimit; 
    
    /*MNL 10/28/2024 precisa testar as 3 configs */
    /* Gear Ratio Config */
    globalConfig.Feedback.SensorToMechanismRatio=1;
    globalConfig.ClosedLoopGeneral.ContinuousWrap = true;
    /* Closed Loop Ramping */
    globalConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = EscaladorConstants.kClosedLoopRamp;
    
    /* User can optionally change the configs or leave it alone to perform a factory default */
    globalConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    

    /* Apply the config */
    escaladorFollowerMotor.getConfigurator().apply(globalConfig);
        
    escaladorLeaderMotor.getConfigurator().apply(globalConfig);

     /* Set up followers to follow leaders */
    escaladorFollowerMotor.setControl(new Follower(escaladorLeaderMotor.getDeviceID(), false));

    escaladorLeaderMotor.setSafetyEnabled(true);

    /* And initialize encoder position to 0 */
    escaladorLeaderMotor.getConfigurator().setPosition(0);


  }

  /**
   * Tunning TalonFx PIDController
   * @see https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/device-specific/talonfx/basic-pid-control.html
   * 
   */
  public void motorPidControllerGains()
  {
    // in init function, set slot 0 gains
    var slot0Configs = new Slot0Configs();
    var slot1Configs = new Slot1Configs();
    
    /* Voltage-based velocity requires a velocity feed forward to account for the back-emf of the motor */
    slot0Configs.kS = EscaladorConstants.KS; //0.1; // Add 0.1 V output to overcome static friction
    slot0Configs.kV = EscaladorConstants.KV; //0.12; // A velocity target of 1 rps results in 0.12 V output
    slot0Configs.kP = EscaladorConstants.kP; //0.11; // An error of 1 rps results in 0.11 V output
    slot0Configs.kI = EscaladorConstants.kI; //0; // no output for integrated error
    slot0Configs.kD = EscaladorConstants.kD; //0; // no output for error derivative
    // Peak output of 10 volts
    globalConfig.Voltage.withPeakForwardVoltage(Volts.of(10))
                        .withPeakReverseVoltage(Volts.of(-10));

    //Voltage-based position does not require a feed forward 
    slot1Configs.kP = EscaladorConstants.kP1; //0.11; // An error of 1 rps results in 0.11 V output
    slot1Configs.kI = EscaladorConstants.kI1; //0; // no output for integrated error
    slot1Configs.kD = EscaladorConstants.kD1; //0; // no output for error derivative
    // Peak output of 10 volts
    globalConfig.Voltage.withPeakForwardVoltage(Volts.of(10))
                        .withPeakReverseVoltage(Volts.of(-10));

    //escaladorLeaderMotor.getConfigurator().apply(slot0Configs);
  }


  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }

}
