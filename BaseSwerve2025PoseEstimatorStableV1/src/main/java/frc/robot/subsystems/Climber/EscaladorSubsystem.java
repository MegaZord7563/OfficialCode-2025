// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.DigitalInput;
//WPILib imports
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//Java imports

import com.revrobotics.RelativeEncoder;
import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.Rev2mDistanceSensor.Port;
import com.revrobotics.Rev2mDistanceSensor.RangeProfile;
import com.revrobotics.Rev2mDistanceSensor.Unit;
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

  //Difuse Sensor
  private DigitalInput sensorClimber = new DigitalInput(3);


  // The alga PID controller
  private final SparkClosedLoopController escaladorPidController;
  
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
    SmartDashboard.putBoolean("EscalatorSensor", sensorClimber.get());

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
