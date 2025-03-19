// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Swerve;


import java.security.Timestamp;
//JAVA Imports
import java.util.function.Supplier;


//CTRE imports
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.Pigeon2Configurator;
import com.ctre.phoenix6.hardware.Pigeon2;


import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;


//WPI Imports
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.filter.SlewRateLimiter;

//Robot Imports
import frc.robot.Constants;
import frc.robot.Constants.PathPlannerConstants;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Limelight.LimelightHelpers.PoseEstimate;
import frc.robot.subsystems.LedControl;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;




public class SwerveSubsystem extends SubsystemBase 

{
  /**************************
   * Create 4 Swerve Modules*
   **************************/

  public final SwerveModule frontLeftModule = 
  new SwerveModule
                  (
                    DriveConstants.kFrontLeftDriveMotorPort,
                    DriveConstants.kFrontLeftTurningMotorPort,
                    DriveConstants.kFrontLeftDriveAbsoluteEncoderPort,
                    DriveConstants.angleOffsetFLTurning,
                    DriveConstants.kFrontLeftChassisAngularOffset
                  );

  private final SwerveModule frontRightModule = 
  new SwerveModule(
                    DriveConstants.kFrontRightDriveMotorPort,
                    DriveConstants.kFrontRightTurningMotorPort,
                    DriveConstants.kFrontRightDriveAbsoluteEncoderPort,
                    DriveConstants.angleOffsetFRTurning,
                    DriveConstants.kFrontRightChassisAngularOffset
                  );

  private final SwerveModule backLeftModule = 
  new SwerveModule(
                    DriveConstants.kBackLeftDriveMotorPort,
                    DriveConstants.kBackLeftTurningMotorPort,
                    DriveConstants.kBackLeftDriveAbsoluteEncoderPort,
                    DriveConstants.angleOffsetBLTurning,
                    DriveConstants.kBackLeftChassisAngularOffset
                  );

  private final SwerveModule backRightModule = 
  new SwerveModule(
                    DriveConstants.kBackRightDriveMotorPort,
                    DriveConstants.kBackRightTurningMotorPort,
                    DriveConstants.kBackRightDriveAbsoluteEncoderPort,
                    DriveConstants.angleOffsetBRTurning,
                    DriveConstants.kBackRightChassisAngularOffset
                  );

  
  
  /*******************
   * Create gyroscope*
   * ******************/
  //private final AHRS gyro = new AHRS(NavXComType.kMXP_SPI);

  //Create Pigeon2 gyro
  Pigeon2 gyro = new Pigeon2(DriveConstants.kPigeonPort, "rio");
       
  // Create a slew rate limiter to limit the rate of change of the commanded robot velocity
  private final SlewRateLimiter xLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
  private final SlewRateLimiter yLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
  private final SlewRateLimiter turningLimiter= new SlewRateLimiter(DriveConstants.kTeleDriveMaxAngularAccelerationUnitsPerSecond);

  
  ChassisSpeeds chassisSpeeds;
  //private final Field2d field2d;
  //Array of modules
  SwerveModule[] modules = {frontLeftModule,frontRightModule,backLeftModule,backRightModule};

  /** Creates a new SwerveSubsystem. */
  private boolean throttleSlow=false;
  private boolean throttleFast=false;
  private boolean throttleMax=false;
  private boolean throttleLift = false;
  private boolean isBlue = false;
  private boolean blueAlliance =false;

  //PathPlanner Config
  RobotConfig config;

  //Select alliance heading - default is blue
  private double allianceHeading = 0;

      
  private boolean deploy = true;
  private int countDeploy = 0;

  /* Here we use SwerveDrivePoseEstimator so that we can fuse odometry readings. The numbers used
  below are robot specific, and should be tuned. */
  
  // Create a new SwerveDrivePoseEstimator
  private final SwerveDrivePoseEstimator m_poseEstimator;

  // Create a NetworkTable publisher to publish the pose to NetworkTables
  StructPublisher<Pose2d> publisher = NetworkTableInstance.getDefault()
  .getStructTopic("MyPose", Pose2d.struct).publish();
  
  //optical sensors
  private DigitalInput sensorLeft = new DigitalInput(2);
  private DigitalInput sensorRight = new DigitalInput(1);

  private boolean sensorLeftLate;
  private boolean sensorRightLate;


  
  public SwerveSubsystem() 
  {

    //field2d = new Field2d();
    //SmartDashboard.putData("Field", field2d);

    //Robot Front is allways towards to red wall
    var alliance2 = DriverStation.getAlliance();
    if (alliance2.get() == DriverStation.Alliance.Red) 
    {
      allianceHeading = 0;
      blueAlliance = false;
      LimelightHelpers.setCameraPose_RobotSpace(DriveConstants.limelightFront,0.110,0.259,0.410,0,0,0);
      LimelightHelpers.setCameraPose_RobotSpace(DriveConstants.limelightBack,0.130,0.259,0.870,0,25,180);
      System.out.println("limelight settings Red");
       
    }else
    {
      //blue
      allianceHeading = 180;
      blueAlliance = true;
      LimelightHelpers.setCameraPose_RobotSpace(DriveConstants.limelightFront,0.110,0.259,0.410,0,0,0);
      LimelightHelpers.setCameraPose_RobotSpace(DriveConstants.limelightBack,0.130,0.259,0.870,0,25,180);
      System.out.println("limelight settings Blue");

    }

    //Initialize pose estimator ALWAYS towards to red wall
    m_poseEstimator =
                new SwerveDrivePoseEstimator(
                    DriveConstants.kDriveKinematics,
                    Rotation2d.fromDegrees(allianceHeading),//gyro.getRotation2d(),
                    new SwerveModulePosition[] {
                      frontLeftModule.getPosition(),
                      frontRightModule.getPosition(),
                      backLeftModule.getPosition(),
                      backRightModule.getPosition()
                    },
                    new Pose2d(),
                    VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
                    VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30)));
    
    //camera pose set up - to initialize cameras according to its assembling on the robot
    //LimelightHelpers.setCameraPose_RobotSpace(DriveConstants.limelightFront,-0.13,0,0.685,0,0,0);
    //LimelightHelpers.setCameraPose_RobotSpace(DriveConstants.limelightBack,-0.13,0,0.685,0,0,0);

    /* delay 1 s and then reset gyro */
    /*
    new Thread(() -> {

                      try 
                      {
                          Thread.sleep(1000); //avoid to block the rest of our code from running
                          gyro.reset();
                          
                      } catch (Exception e) 
                      {
                      }
                    	}).start();
//*/
    //initialize Pigeon2 gyro
    initializePigeon2(gyro.getConfigurator());
         //gyro.setAngleAdjustment(180);  
         
//*/
  //gyro.reset();
  //gyro.setYaw(0,1); 
  //zeroHeading();
  
  

  
  // Initialize the PathPlanner config and AutoBuilder
    try{ 

      config = RobotConfig.fromGUISettings();
    } catch (Exception e) {
      // Handle exception as needed
      DriverStation.reportError("Failed to load PathPlanner config and configure AutoBuilder", e.getStackTrace());
    }  

    // Configure AutoBuilder last
    AutoBuilder.configure(
            this::getPoseEstimator, // Robot pose supplier
            this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
            this::getChassisSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            this::drive, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
            PathPlannerConstants.AutoConfig,
            config, // The robot configuration
            () -> {
              // Boolean supplier that controls when the path will be mirrored for the red alliance
              // This will flip the path being followed to the red side of the field.
              // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
              var alliance = DriverStation.getAlliance();
              if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
              }
              return false;
            },
            this // Reference to this subsystem to set requirements
      );
    


    }



  

  @Override
  public void periodic() 
  {
    
    
    this.updatePoseEstimator();
  
    this.addPoseVision();
   
    


    // Output  to SmartDashboard
    SmartDashboard.putNumber("PoseEstimator /Pose X (meters)", this.getPoseEstimator().getX());
    SmartDashboard.putNumber("PoseEstimator /rPose Y (meters)", this.getPoseEstimator().getY());
    //SmartDashboard.putNumber("PoseEstimator /BotPose (degrees)", LimelightHelpers.getBotPose(DriveConstants.limelightFront)[5]);
    SmartDashboard.putNumber("PoseEstimator /Rotation (degrees)", this.getPoseEstimator().getRotation().getDegrees());
    SmartDashboard.putBoolean("True Blue", isBlue);
    SmartDashboard.putNumber("Pigeon", gyro.getYaw().getValueAsDouble());
    
    
    SmartDashboard.putBoolean("Sensor Left value", getSensorLeft());
    SmartDashboard.putBoolean("Sensor Right value", getSensorRight());
    

    if(deploy)
    {
      countDeploy++;
      if(countDeploy == 5)
      {
        deploy = false;
      }
      System.out.println("Deploy1");
    }

    // Publish the pose to NetworkTables
    publisher.set(getPoseEstimator());


    for (int i = 0; i < modules.length; i++) 
    {
      SmartDashboard.putNumber("Module " + i + " /Drive Motor Current (Amp)", modules[i].getDriveCurrent());
    //  SmartDashboard.putNumber("Module " + i + " /Drive Motor Temperature (℃)", modules[i].getDriveTemperature());
      SmartDashboard.putNumber("Module " + i + " /Turn Motor Current (Amp)", modules[i].getTurnCurrent());

   //   SmartDashboard.putNumber("Module " + i + " /Drive Motor Temperature (℃)", modules[i].getDriveTemperature());
   //   SmartDashboard.putNumber("Module " + i + " /Turning Motor Heading (Degrees)", modules[i].getTurningHeadingDegrees());
  //    SmartDashboard.putNumber("Module " + i + " /CanCoder Heading (rad)", modules[i].canCoderRad());
  //    SmartDashboard.putNumber("Module " + i + " /CanCoder Heading (Degrees)", modules[i].canCoderDegrees());
  //    SmartDashboard.putNumber("Module " + i + " /CanCoder Angle value (rad)", modules[i].getTurningPosition());
      
  
       if (!modules[i].getCanCoderIsValid())
       {
        System.err.println("Absolute Encoder Error: Returning default angle.");

       }

    }
    

    //Output Module States to SmartDashboard
    SwerveModuleState[] moduleStates = getModuleStates();
    for (int i = 0; i < moduleStates.length; i++) 
    {
      SmartDashboard.putNumber("Module " + i + " /Speed (m/s)", moduleStates[i].speedMetersPerSecond);
      //SmartDashboard.putNumber("Module " + i + " /Angle (rad)", moduleStates[i].angle.getRadians());

      
    }

    //field2d.setRobotPose(getPoseEstimator());
   /* SmartDashboard.putData("Swerve Drive", new Sendable() {
  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("SwerveDrive");

    builder.addDoubleProperty("Front Left Angle", () -> frontLeftModule.getTurningPosition(), null);
    builder.addDoubleProperty("Front Left Velocity", () -> frontLeftModule.getDriveVelocityMPS(), null);

    builder.addDoubleProperty("Front Right Angle", () -> frontRightModule.getTurningPosition(), null);
    builder.addDoubleProperty("Front Right Velocity", () -> frontRightModule.getDriveVelocityMPS(), null);

    builder.addDoubleProperty("Back Left Angle", () -> backLeftModule.getTurningPosition(), null);
    builder.addDoubleProperty("Back Left Velocity", () -> backLeftModule.getDriveVelocityMPS(), null);

    builder.addDoubleProperty("Back Right Angle", () -> backRightModule.getTurningPosition(), null);
    builder.addDoubleProperty("Back Right Velocity", () -> backRightModule.getDriveVelocityMPS(), null);

    builder.addDoubleProperty("Robot Angle", () -> gyro.getRotation2d().getDegrees(), null);
    }
  });*/
    getSensorLeft();
    getSensorRight();

  }

  
  /** Updates the field relative position of the robot. */
  public void updatePoseEstimator() 
  {
    // Update the pose estimator with the latest sensor measurements
    m_poseEstimator.update(
        gyro.getRotation2d(),
        new SwerveModulePosition[]
        {
          frontLeftModule.getPosition(),
          frontRightModule.getPosition(),
          backLeftModule.getPosition(),
          backRightModule.getPosition()
        });
  
  }

  /**
   * update pose estimator using absoule coordenates from limelight
   */
  public void addPoseVision()
  {
   
    
    boolean update=false;//set to true to update pose estimator

   

    boolean doRejectUpdate = false;//set to true to reject update

    LimelightHelpers.PoseEstimate mt1;

    // Determine which measurement to use based on tag count or other criteria
    //LimelightHelpers.PoseEstimate bestMeasurement = null;
    LimelightHelpers.PoseEstimate bestMeasurement=null;//=new LimelightHelpers.PoseEstimate();//initialize best measurement

    boolean useMegaTag2 = true; //set to false to use MegaTag1

   
    
    if(useMegaTag2 == false)
    {
      if (isBlue)
      {
        mt1 = LimelightHelpers.getBotPoseEstimate_wpiBlue(DriveConstants.limelightFront);
      }
      else 
      {
         mt1 = LimelightHelpers.getBotPoseEstimate_wpiRed(DriveConstants.limelightFront);
      }
      
      if(mt1.tagCount == 1 && mt1.rawFiducials.length == 1)
      {
        if(mt1.rawFiducials[0].ambiguity > .7)
        {
          doRejectUpdate = true;
        }
        if(mt1.rawFiducials[0].distToCamera > 3)
        {
          doRejectUpdate = true;
        }
      }
      if(mt1.tagCount == 0)
      {
        doRejectUpdate = true;
      }

      if(!doRejectUpdate)
      {
        update=true;
        m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.5,.5,9999999));
        m_poseEstimator.addVisionMeasurement(
            mt1.pose,
            mt1.timestampSeconds);
        
        
        
      }

      SmartDashboard.putBoolean("UpdatePose", update);
      SmartDashboard.putNumber("Pose mt1/X", mt1.pose.getX());
      SmartDashboard.putNumber("Pose mt1/Y", mt1.pose.getY());

      
      

      
    }
    else if (useMegaTag2 == true)
    {
      
      // Set the robot's orientation using the current estimated position's rotation degrees for each camera.
      LimelightHelpers.SetRobotOrientation(DriveConstants.limelightBack, m_poseEstimator.getEstimatedPosition().getRotation().getDegrees(),0, 0, 0, 0, 0);
      LimelightHelpers.SetRobotOrientation(DriveConstants.limelightFront, m_poseEstimator.getEstimatedPosition().getRotation().getDegrees(),0, 0, 0, 0, 0);
      
      //Pose estimates are obtained from both cameras.
      LimelightHelpers.PoseEstimate frontLimelight = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(DriveConstants.limelightFront);
      
      LimelightHelpers.PoseEstimate rearLimelight = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(DriveConstants.limelightBack);

     // if (frontLimelight==null){doRejectUpdate=true;}
      //if (rearLimelight==null){doRejectUpdate=true;}
      
      if(Math.abs(gyro.getAngularVelocityZDevice().refresh().getValueAsDouble()) > 720) // if our angular velocity is greater than 720 degrees per second, ignore vision updates
      {
        doRejectUpdate = true;
      }
      /**
       * The tagCount variable represents the number of vision tags (fiducial markers) 
       * detected by the Limelight camera.
       * */ 
      //checks if both limelight are disconnected
      if(frontLimelight != null && rearLimelight != null)
      {

          if(frontLimelight.tagCount == 0  && rearLimelight.tagCount == 0 )
          {
            doRejectUpdate = true;
          }
          //
          if(!doRejectUpdate)
          {
                        
              //The best measurement is selected based on the tag count from each camera.
              if (frontLimelight.tagCount > 0 && rearLimelight.tagCount > 0  && rearLimelight.avgTagDist <3 && frontLimelight.avgTagDist <3 ) 
              {
                // Both cameras have valid measurements, choose the one with the higher tag count
                bestMeasurement = (frontLimelight.avgTagArea >= rearLimelight.avgTagArea) ? frontLimelight : rearLimelight;
                m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
                m_poseEstimator.addVisionMeasurement(bestMeasurement.pose,bestMeasurement.timestampSeconds);
              
              } else if (frontLimelight.tagCount > 0  &&  frontLimelight.avgTagDist <3) 
              {
                  bestMeasurement = frontLimelight;
                  m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
                  m_poseEstimator.addVisionMeasurement(bestMeasurement.pose,bestMeasurement.timestampSeconds);
              } else if (rearLimelight.tagCount > 0 && rearLimelight.avgTagDist < 3 ) 
              {
                  bestMeasurement = rearLimelight;
                  m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
                  m_poseEstimator.addVisionMeasurement(bestMeasurement.pose,bestMeasurement.timestampSeconds);
              } 
              

            }
      }

      //checks if only front limelight is connected
      if( rearLimelight == null && frontLimelight != null)
      {

        if(frontLimelight.tagCount == 0 )
        {
          doRejectUpdate = true;
        }

        if(!doRejectUpdate ){
        if (frontLimelight.tagCount > 0  &&  frontLimelight.avgTagDist <3) 
          {
              bestMeasurement = frontLimelight;
              m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
              m_poseEstimator.addVisionMeasurement(bestMeasurement.pose,bestMeasurement.timestampSeconds);
          }
        }
      }

      //checks if only rear limelight is connected

      if( rearLimelight != null && frontLimelight == null)
      {

        if(rearLimelight.tagCount == 0 )
        {
          doRejectUpdate = true;
        }

        if(!doRejectUpdate ){
        if (rearLimelight.tagCount > 0  &&  rearLimelight.avgTagDist <3) 
          {
              bestMeasurement = rearLimelight;
              m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
              m_poseEstimator.addVisionMeasurement(bestMeasurement.pose,bestMeasurement.timestampSeconds);
          }
        }
      }

     
    }
  }

  //
   public boolean getBlueAlliance()
   {
      return blueAlliance;
   }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() 
  {
    gyro.reset();
    
  }   
  
  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() 
  {
    frontLeftModule.resetDriveEncoders();
    frontRightModule.resetDriveEncoders();
    backLeftModule.resetDriveEncoders();
    backRightModule.resetDriveEncoders();
  }

  
  /**
   * Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
   * Path Planner uses
   * @param chassisSpeeds
   */
  public void drive(ChassisSpeeds chassisSpeeds) 
  {
    SwerveModuleState[] swerveModuleStates = Constants.DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
    
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates,Constants.DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
    
    frontLeftModule.setDesiredState(swerveModuleStates[0]);
    frontRightModule.setDesiredState(swerveModuleStates[1]);
    backLeftModule.setDesiredState(swerveModuleStates[2]);
    backRightModule.setDesiredState(swerveModuleStates[3]);
  }

  //Method to get the ROBOT RELATIVE ChassisSpeeds
  public ChassisSpeeds getChassisSpeeds()
  {
    // Relative to robot
    return Constants.DriveConstants.kDriveKinematics.toChassisSpeeds(getModuleStates());
  }

  
   //reset the pose Estimator to a new location
   /**MNL10/23/2024
    * 
    * @param pose The pose  to set the odometry.
    */
   public void resetOdometry(Pose2d pose) 
   {

    m_poseEstimator.resetPosition(
                  gyro.getRotation2d(), //arrow usa gyro.getAngle() ///getGyroYaw()
                  new SwerveModulePosition[] 
                                          {
                                              frontLeftModule.getPosition(),
                                              frontRightModule.getPosition(),
                                              backLeftModule.getPosition(),
                                              backRightModule.getPosition()
                                          },
                                          pose);
}

/**
 * 
 * @return estimated position
 */
public Pose2d getPoseEstimator()
{
  return m_poseEstimator.getEstimatedPosition();
}

 

/**
 * Stop all modules
 */

public void stopModules ()
{
  
  for (int i = 0; i < modules.length; i++) 
  {
      modules[i].stopMotors();
    
  }
  

}

  /**
   * 
   * @return throttle slow down the robot speed
   */
  public void robotSlower()
  {
    
    throttleSlow = true;
    throttleFast = false;
    throttleMax=false;
    
    for (int i = 0; i < 3; i++) 
    {
      System.out.println("Speed up velocity - 30%");
      
    }

    
   

  }

  /**
   * 
   * @return throttle slow down the robot speed
   */
  public void robotFast()
  {
    throttleSlow = false;
    throttleFast = true;
    throttleMax=false;

    for (int i = 0; i < 3; i++) 
    {
      System.out.println("Speed up velocity - 50%");
      
    }
  }

  /**
   * 
   * @return throttle slow down the robot speed
   */
  public void robotMaxSpeed()
  {
    
    throttleSlow=false;
    throttleFast=false;
    throttleMax=true;

    for (int i = 0; i < 3; i++) 
    {
      System.out.println("Velocity 100% Max");
      
    }
  }

  public void robotLiftVelocity()
  {
    
    throttleSlow=false;
    throttleFast=false;
    throttleMax=false;
    throttleLift = true;

    for (int i = 0; i < 3; i++) 
    {
      System.out.println("Velocity 100% Max");
      
    }
  }


  // Improved "setX()" to use `setModuleStates()`
  public void setX() 
  {
    SwerveModuleState[] xStates = new SwerveModuleState[4];
    xStates[0] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
    xStates[1] = new SwerveModuleState(0, Rotation2d.fromDegrees(-45));
    xStates[2] = new SwerveModuleState(0, Rotation2d.fromDegrees(-45));
    xStates[3] = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
    setModuleStates(xStates);
  }

  /**
 * Sets the swerve ModuleStates.
 *
 * @param desiredStates The desired SwerveModule states.
 */
  public void setModuleStates (SwerveModuleState [] desiredStates)
  { 

    //normalize the wheel speeds ;
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, DriveConstants.kTeleDriveMaxSpeedMetersPerSecond); 
    
    // Output Module States to each one
    for (int i = 0; i < modules.length; i++) 
    {
        modules[i].setDesiredState(desiredStates[i]);
      
    }
    
  }

  /**
   * 
   * @return states 
   */
  public SwerveModuleState[] getModuleStates() 
  {
    SwerveModuleState[] states = new SwerveModuleState[modules.length];
    for (int i = 0; i < modules.length; i++) 
    {
      states[i] = modules[i].getState();
    }
    return states;
  }

  /**
   * Method drive with joystick
   * The use of these parameters as suppliers to dynamically provide speed and field orientation values.
   * Suppliers are especially useful in cases where you need to calculate values dynamically based on factors that can change over time.
   * The Supplier interface gives you a powerful mechanism to make your FRC robot code more flexible and adaptable. 
   * Suppliers are incredibly useful in command-based FRC programming because they allow you to:
   * Decouple Logic: You can separate the logic for calculating drive parameters (speeds, orientation) 
   * from the actual drive command. This makes your code cleaner and more maintainable.
   * Dynamic Values: You can easily update the speed and orientation values on-the-fly based on real-time conditions, 
   * such as sensor feedback or joystick input.
   * 
   * @param xSpdFunction Speed of the robot in the x direction (forward).
   * @param ySpdFunction Speed of the robot in the y direction (sideways).
   * @param turningSpdFunction Angular rate of the robot. rad/s
   * @param fieldOrientedFunction Boolean indicating if speeds are relative to the field or to therobot. 
   *                              
   **/

  public void driveRobotOriented( Supplier<Double> xSpdFunction, 
                                  Supplier<Double> ySpdFunction,
                                  Supplier<Double> turningSpdFunction, 
                                  Supplier<Boolean> fieldOrientedFunction)
  {
    
    // 1. Get real-time joystick inputs
    double xSpeed = xSpdFunction.get();
    double ySpeed = ySpdFunction.get();  
    double turningSpeed = turningSpdFunction.get();
    boolean fieldOriented = fieldOrientedFunction.get();
    double throttle = 1.0;
       
    
    //Selects speed
    if (throttleSlow)
    {
      throttle =0.25;

    }
    else if (throttleFast)
    {
    
      throttle =0.65;
      
    }
    else if (throttleMax) 
    { 
      throttle = 0.9;
      
    }
    else if (throttleLift){
      throttle = 0.1;
    }

    // 2. Apply deadband
    xSpeed = Math.abs(xSpeed) > OIConstants.kDeadband ? xSpeed : 0.0;
    ySpeed = Math.abs(ySpeed) > OIConstants.kDeadband ? ySpeed : 0.0;
    turningSpeed = Math.abs(turningSpeed) > OIConstants.kDeadband ? turningSpeed : 0.0;

    // 3. Make the driving smoother
    xSpeed = xLimiter.calculate(xSpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond*throttle;
    ySpeed = yLimiter.calculate(ySpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond*throttle;
    turningSpeed = turningLimiter.calculate(turningSpeed)* DriveConstants.kTeleDriveMaxAngularSpeedRadiansPerSecond*throttle;

    // 4. Construct desired chassis speeds

    var swerveModuleStates =  DriveConstants.
                              kDriveKinematics.
                              toSwerveModuleStates
                              (fieldOriented ? 
                                ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, 
                                                                      ySpeed, 
                                                                      turningSpeed, 
                                                                      gyro.getRotation2d()) 
                                : new ChassisSpeeds(xSpeed, ySpeed, turningSpeed)                               
                              );//Do this if fielOrientation is false 

    
    // 5. Convert chassis speeds to individual module states
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, DriveConstants.kTeleDriveMaxSpeedMetersPerSecond);
    // 6. Output each module states to wheels
    this.setModuleStates(swerveModuleStates);//*/  

    
  }

  /**
     * Initialize Pigeon2 device from the configurator object
     * 
     * @param cfg Configurator of the Pigeon2 device
     */
  private void initializePigeon2(Pigeon2Configurator cfg)
  {
    var toApply = new Pigeon2Configuration();
    /*
    * User can change configs if they want, or leave this blank for factory-default
    */
    cfg.apply(toApply);
    
   
    /* And initialize yaw to 0 */
    //MUST NOT TOUCH
    cfg.setYaw(0); 
    
  }



  /**
   * Method drive to target
   * The use of these parameters as suppliers to dynamically provide speed and field orientation values.
   * Suppliers are especially useful in cases where you need to calculate values dynamically based on factors that can change over time.
   * The Supplier interface gives you a powerful mechanism to make your FRC robot code more flexible and adaptable. 
   * Suppliers are incredibly useful in command-based FRC programming because they allow you to:
   * Decouple Logic: You can separate the logic for calculating drive parameters (speeds, orientation) 
   * from the actual drive command. This makes your code cleaner and more maintainable.
   * Dynamic Values: You can easily update the speed and orientation values on-the-fly based on real-time conditions, 
   * such as sensor feedback or joystick input.
   * 
   * @param xSpdFunction Speed of the robot in the x direction (forward).
   * @param ySpdFunction Speed of the robot in the y direction (sideways).
   * @param turningSpdFunction Angular rate of the robot. rad/s
   * @param fieldOrientedFunction Boolean indicating if speeds are relative to the field or to therobot. 
   *                              
   **/

  public void driveToTarget( Supplier<Double> xSpdFunction, 
                                  Supplier<Double> ySpdFunction,
                                  Supplier<Double> turningSpdFunction, 
                                  Supplier<Boolean> fieldOrientedFunction)
  {
    
    // 1. Get real-time joystick inputs
    double xSpeed = -xSpdFunction.get();
    double ySpeed = -ySpdFunction.get();  
    double turningSpeed = turningSpdFunction.get();
    boolean fieldOriented = fieldOrientedFunction.get();

    // 2. Apply deadband
    //xSpeed = Math.abs(xSpeed) > OIConstants.kDeadband ? xSpeed : 0.0;
   // ySpeed = Math.abs(ySpeed) > OIConstants.kDeadband ? ySpeed : 0.0;
    //turningSpeed = Math.abs(turningSpeed) > OIConstants.kDeadband ? turningSpeed : 0.0;

    // 3. Make the driving smoother
    xSpeed = xLimiter.calculate(xSpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;
    ySpeed = yLimiter.calculate(ySpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;
    turningSpeed = turningLimiter.calculate(turningSpeed)* DriveConstants.kTeleDriveMaxAngularSpeedRadiansPerSecond;

    // 4. Construct desired chassis speeds

    var swerveModuleStates =  DriveConstants.
                              kDriveKinematics.
                              toSwerveModuleStates
                              (fieldOriented ?
                                ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, 
                                                                      ySpeed, 
                                                                      turningSpeed, 
                                                                      gyro.getRotation2d()) 
                              : new ChassisSpeeds(xSpeed, ySpeed, turningSpeed)                               
                              );//Do this if fielOrientation is false 

    
    // 5. Convert chassis speeds to individual module states
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, DriveConstants.kTeleDriveMaxSpeedMetersPerSecond);
    // 6. Output each module states to wheels
    this.setModuleStates(swerveModuleStates);//*/  

    
  }

  /**
   * False if target is on the left side of the robot
   * @return
   */
  public boolean getSensorLeft()
  {
    return sensorLeft.get();
  } 



  /**
   * False if target is on the right side of the robot
   * @return
   */
  public boolean getSensorRight()
  {
    return sensorRight.get();
  }



  /**
   * Get the gyro yaw
   * @return
   */
  public Rotation2d getGyroRotation2d()
  {
    return gyro.getRotation2d();
  }


}