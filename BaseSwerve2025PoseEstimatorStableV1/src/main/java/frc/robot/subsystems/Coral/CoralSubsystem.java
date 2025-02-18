// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Coral;

//CTRE Imports
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.Rev2mDistanceSensor.Port;
import com.revrobotics.Rev2mDistanceSensor.RangeProfile;
import com.revrobotics.Rev2mDistanceSensor.Unit;

import edu.wpi.first.wpilibj.DigitalInput;
//WPILIB Imports
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import static edu.wpi.first.units.Units.*;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Robot Imports
import frc.robot.Constants.SubsystemsConstants.CoralIntakeConstants;




/** Add your docs here. */
public class CoralSubsystem extends SubsystemBase 
{
  // The lift motor
  //private final SparkMax coralMotor;
  //private SparkMaxConfig globalConfig = new SparkMaxConfig();

  private final TalonFX coralMotor;
  


  // The distance sensor
  /**
   * pin-out
   * red = V+ 3.3V
   * black = GND
   * white = connect to SCL MXP port
   * blue = connect to SDA MXP port
   * 
   */
  private Rev2mDistanceSensor distOnboard = new Rev2mDistanceSensor(Port.kMXP);
  

  /* Start at motion 0, use slot 2 */
   //private final MotionMagicVoltage m_mmReq = new MotionMagicVoltage(0).withSlot(2);

 // private final VelocityVoltage m_velocityVoltage = new VelocityVoltage(0).withSlot(0);

  private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);

  /* Keep a brake request so we can disable the motor */
  private final NeutralOut m_brake = new NeutralOut();
  
  // The lift encoder
  //private final RelativeEncoder coralEncoder;
  

  // The lift PID controller
 // private SparkClosedLoopController coralPidController;
  

  /** Creates a new LiftSubsytem. */
  public CoralSubsystem() 
  {
    // Create the  motor
    coralMotor = new TalonFX (CoralIntakeConstants.kCoralMotorId,"rio");
 
    SmartDashboard.putBoolean("L1", false);
    SmartDashboard.putBoolean("L2", false);
    SmartDashboard.putBoolean("L3", false);
    SmartDashboard.putBoolean("L4", false);
    /**
     * Before measurements can be read from the sensor, setAutomaticMode(true)
     * must be called. This starts a background thread which will periodically
     * poll all enabled sensors and store their measured range.
     */
    // Create the distance sensor
    distOnboard.setAutomaticMode(true);

    //// Set the distance sensor units
    distOnboard.setDistanceUnits(Unit.kMillimeters);
    
    distOnboard.setMeasurementPeriod(0.1);
    //
    ////
    distOnboard.setEnabled(true);
    
    distOnboard.setRangeProfile(RangeProfile.kDefault);
  
    // Create the  encoder
    //coralEncoder = coralMotor.getEncoder();
    //liftRightEncoder = liftRightMotor.getEncoder();

    //Create PID controller
    //coralPidController = coralMotor.getClosedLoopController();
    

    //Calls motors config
   initializeMotors();
    

    
  }

  /**
   * @see https://v6.docs.ctr-electronics.com/en/stable/docs/hardware-reference/talonfx/improving-performance-with-current-limits.html
   * Tunning TalonFx PIDController
   * @see https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/device-specific/talonfx/basic-pid-control.html
   * Config drive motors
   * @category TalonFx settings for drive motors
   * 
   */
  private void initializeMotors()
  { 
    //
    TalonFXConfiguration globalConfig = new TalonFXConfiguration();

    coralMotor.clearStickyFaults();
    //Factory Default
    coralMotor.getConfigurator().apply(new TalonFXConfiguration());      
    
    coralMotor.setNeutralMode(CoralIntakeConstants.kNeutralMode);
   
    
    /* Configure a stator limit of 20 amps */
   
    CurrentLimitsConfigs currentLimitConfigs = globalConfig.CurrentLimits;
    currentLimitConfigs.StatorCurrentLimit = CoralIntakeConstants.kCurrentThreshold;
    currentLimitConfigs.StatorCurrentLimitEnable = CoralIntakeConstants.kEnableCurrentLimit; 
    
    
    /* Gear Ratio Config */
    //globalConfig.Feedback.SensorToMechanismRatio = CoralIntakeConstants.kGearRatio; 
    //globalConfig.ClosedLoopGeneral.ContinuousWrap = true;//keep default = FALSE - MUST NOT TOUCH IT
    /* Closed Loop Ramping */
    //globalConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = CoralIntakeConstants.kClosedLoopRamp;
    
    /* User can optionally change the configs or leave it alone to perform a factory default */
    globalConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    
               
    // Voltage-based VELOCITY requires a velocity feed forward to account for the back-emf of the motor 
    //globalConfig.Slot0.kS = CoralIntakeConstants.KS; //0.1; // Add 0.1 V output to overcome static friction
    //globalConfig.Slot0.kV = CoralIntakeConstants.KV; //0.12; // A velocity target of 1 rps results in 0.12 V output
    globalConfig.Slot0.kP = CoralIntakeConstants.kP; //0.11; // An error of 1 rps results in 0.11 V output
    globalConfig.Slot0.kI = CoralIntakeConstants.kI; //0; // no output for integrated error
    globalConfig.Slot0.kD = CoralIntakeConstants.kD; //0; // no output for error derivative
    // Peak output of 10 volts
    globalConfig.Voltage.withPeakForwardVoltage(Volts.of(2))
                        .withPeakReverseVoltage(Volts.of(-2));

    
     
    /* Retry config apply up to 5 times, report if failure */
    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for (int i = 0; i < 5; ++i) 
    {
      status = coralMotor.getConfigurator().apply(globalConfig);
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

  /*// 
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
    
  }*/

  /**
     * Get the distance measured by the sensor.
     * 
     * @return The distance in millimeters.
     */
  public double getDistance() 
  {
    return distOnboard.getRange();
  }

  @Override
  public void periodic() 

  {
   
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Coral Velocity m/s", getVelocity());

    /**
     * Range returned from the distance sensor is valid if isRangeValid()
     * returns true.
     */
    
    boolean isValid = distOnboard.isRangeValid();

    SmartDashboard.putBoolean("Range Valid", isValid);

    if(isValid) 
    {
      double distance = distOnboard.getRange();
      SmartDashboard.putNumber("Range Onboard", distOnboard.getRange());
      SmartDashboard.putNumber("Timestamp Onboard", distOnboard.getTimestamp());
      // Check if the distance is within the operational range
      if (distance >= 50 && distance <= 150) 
      {
        SmartDashboard.putString("Operational Range", "Within Range");
    
      } 
      else 
      {
        SmartDashboard.putString("Operational Range", "Out of Range");
    
      }
      
    }
    else 
    {
      SmartDashboard.putString("Operational Range", "Invalid Measurement");
    }  

    
  }

  

  /**
   * Set the coral motor speed
   * @param CoralMotorSpeed
   */
  public void runMotor(double targetSpeed)
  {
     // Set the lift motor speed
      coralMotor.setControl( m_positionVoltage.withPosition(targetSpeed));

      System.out.println("Current Positiom: " + coralMotor.getPosition().getValue());
    
  }

  /**
   * Stop the intake/outake
   * 
   */
  public void stopMotor()
  {
    coralMotor.setControl(m_brake);
    
  }

  /**
   * 
   * @return velocity m/s
   */
  public double getVelocity()
  {
    return coralMotor.getVelocity().getValueAsDouble();
  }

  public void setVelocity(double speed)
  {
    coralMotor.set(speed);
  }


  /**
   * @ reset encoder
   */
  public void resetEncoder()
  {
    coralMotor.setPosition(0);

  }

  /**
   * 
   * @return true if the coral is homed
   */
  public Supplier<Boolean> isHomed()
  {
    if (coralMotor.getPosition().getValueAsDouble() < 0.05)
    {
      return ()->true;
    }
      return ()->false;
  }

  
  
 
  

  
}

