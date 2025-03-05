// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Climber;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj.DigitalInput;
//WPILib imports
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

//Java imports

//Rev imports
import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.Rev2mDistanceSensor.Port;
import com.revrobotics.Rev2mDistanceSensor.RangeProfile;
import com.revrobotics.Rev2mDistanceSensor.Unit;



//Robot imports
import frc.robot.Constants.SubsystemsConstants.EscaladorConstants;
import static edu.wpi.first.units.Units.*;


public class EscaladorSubsystem extends SubsystemBase 
{

  //TalonFX motors
 
  private final TalonFX escaladorMotor;

  /* Start at motion 0, use slot 2 */
   //private final MotionMagicVoltage m_mmReq = new MotionMagicVoltage(0).withSlot(2);

  //private final VelocityVoltage m_velocityVoltage = new VelocityVoltage(0).withSlot(1);

  //
  private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);

  /* Keep a brake request so we can disable the motor */
  private final NeutralOut m_brake = new NeutralOut();
  
  //Global config



  /* Keep a brake request so we can disable the motor */
  //private final NeutralOut m_brake = new NeutralOut();
  // The Coral motor
  //private final SparkMax escaladorMotor;
  //private SparkMaxConfig config = new SparkMaxConfig();
  
  // The Coral encoder
  //private final RelativeEncoder escaladorEncoder;

  //Difuse Sensor
  private DigitalInput sensorClimber = new DigitalInput(3);


  // The alga PID controller
  //private final SparkClosedLoopController escaladorPidController;
  
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

  UsbCamera usbCamera;

  /** Creates a new EscaladorSubsystem. */
  public EscaladorSubsystem() 
  {
    //Instantiate TalonFX motors
    //escaladorMotor = new SparkMax (EscaladorConstants.kMotor1Id, MotorType.kBrushless);
    escaladorMotor = new TalonFX(EscaladorConstants.kMotor1Id, "rio");


    usbCamera = CameraServer.startAutomaticCapture();
     //Set the resolution
    usbCamera.setResolution(640, 480);
     //Set the frames per second  
    usbCamera.setFPS(30);
     //Set the pixel format
    usbCamera.setPixelFormat(PixelFormat.kMJPEG);

    usbCamera.setExposureAuto();
    usbCamera.setBrightness(10);
    usbCamera.setExposureManual(50);
    usbCamera.setWhiteBalanceAuto();

    

    // Create the coral encoder
    //escaladorEncoder = escaladorMotor.getEncoder();
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

    //Initialize motors
    initializeMotors();
    //resetEncoder();

  }
 
  /**
   * @ motors initialization
   */
  public void initializeMotors()
  {
    TalonFXConfiguration globalConfig = new TalonFXConfiguration();
    escaladorMotor.clearStickyFaults();
    //Factory Default
    escaladorMotor.getConfigurator().apply(new TalonFXConfiguration());      
    
    //escaladorMotor.setNeutralMode(EscaladorConstants.kNeutralMode);


   
    
    /* Configure a stator limit of 20 amps */
   
    CurrentLimitsConfigs currentLimitConfigs = globalConfig.CurrentLimits;
    currentLimitConfigs.StatorCurrentLimit = EscaladorConstants.kCurrentThreshold;
    currentLimitConfigs.StatorCurrentLimitEnable = EscaladorConstants.kEnableCurrentLimit; 
    
    /* Gear Ratio Config */
    globalConfig.Feedback.SensorToMechanismRatio= 1/EscaladorConstants.kPositionConversionFactor;
    globalConfig.MotorOutput.NeutralMode = EscaladorConstants.kNeutralMode;
    //globalConfig.ClosedLoopGeneral.ContinuousWrap = false;
    /* Closed Loop Ramping */
    globalConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = EscaladorConstants.kClosedLoopRamp;
    
    /* User can optionally change the configs or leave it alone to perform a factory default */
    globalConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    
    /* Apply the PID config */
    globalConfig.Slot0.kP = EscaladorConstants.kP; //0.11; // An error of 1 rps results in 0.11 V output
    globalConfig.Slot0.kI = EscaladorConstants.kI; //0; // no output for integrated error
    globalConfig.Slot0.kD = EscaladorConstants.kD; //0; // no output for error derivative
    
    //globalConfig.Voltage.withPeakForwardVoltage(Volts.of(10))
                        //.withPeakReverseVoltage(Volts.of(-10));


    /* Retry config apply up to 5 times, report if failure */
    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for (int i = 0; i < 5; ++i) {
      status = escaladorMotor.getConfigurator().apply(globalConfig);
      if (status.isOK()) break;
    }
    if (!status.isOK()) {
      System.out.println("Could not apply configs, error code: " + status.toString());
    }
     
    //Enable safety
    //escaladorMotor.setSafetyEnabled(true);

    /* And initialize encoder position to 0 */
    escaladorMotor.getConfigurator().setPosition(0);
    
    

  }

  /**
     * Get the distance measured by the sensor.
     * 
     * @return The distance in millimeters.
     */
    public double getDistance() 
    {
      return distOnboard.getRange();
    }


  // Check if the distance is within the operational range
  public boolean isDistanceValid() 
  {
    double distance = getDistance();
    return distance >= 50 && distance <= 150;
  }

  /**
   * @ reset encoder
   */
  public void resetEncoder()
  {
    escaladorMotor.setPosition(0);
  }

  
  @Override
  public void periodic() 
  {
    
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Escalador Velocity", getVelocity());
    SmartDashboard.putNumber("Escalador Position", getPosition());

    SmartDashboard.putNumber("EscalatorCurrent", escaladorMotor.getStatorCurrent().getValueAsDouble());
    SmartDashboard.putNumber("EscalatorVoltage", escaladorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("EscalatorTemperature",escaladorMotor.getDeviceTemp().getValueAsDouble());
    SmartDashboard.putBoolean("EscalatorSensor", sensorClimber.get());

    /**
     * Range returned from the distance sensor is valid if isRangeValid()
     * returns true.
     */
    
     boolean isValid = this.isDistanceValid();

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
   * Run motors
   * @param targetSetpoint
   * @param isPositionControl
   */
  public void runMotors( double setPoint)
  {
    
    
    
      escaladorMotor.setControl(m_positionVoltage.withPosition(setPoint));

      System.out.println("Escaldor  Current Position: " + getPosition());
    

  }

  /**
   * Stop motors
   */
  public void stopMotors()
  {
    /* Disable the motor instead */
    //escaladorMotor.set(0.0);
    escaladorMotor.setControl(m_brake);
  }

  

  public boolean getSensorClimber()
  {
    return sensorClimber.get();
  } 

  /**
   * 
   * @return position in rotations as double
   */
  public double getPosition()
  {
    return escaladorMotor.getPosition().getValueAsDouble();
  }
  
  /**
   * 
   * @return velocity in rotations/sec
   */
  public double getVelocity()
  {
    return escaladorMotor.getVelocity().getValueAsDouble();
  }

}
