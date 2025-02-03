// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
// WPI imports
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

// Robot imports
import frc.robot.RobotContainer;
import frc.robot.Limelight.AimingUtils;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Limelight.LimelightHelpers.RawFiducial;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AimAndRangeCmd extends Command 
{
  // Instantiate the object
  private final SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
  private  Pose2d targetPosition = new Pose2d(0, 0, null);
  private static String limelightName = "limelight-front";

  // Variables
  private final double kX, kY, kRot; // Replace with the correct class ID for the game piece
  private static boolean finish = false; 

  //Max speed variables
  private final double kMaxSpeed2Drive = 0.3;                   // Simple speed limit so we don't drive too fast
  private final double kMaxSpeed2Steer = 0.3; 
  private final double kMaxSpeed2Rot = 0.3; 

  //PID offset variables
  private final double kPsteer = 0.03;                    // how hard to turn toward the target
  private final double kPdrive = 0.1; //0.26 
  private final double kProt = 0.0; //0.26 
  //PID controller
  private PIDController pidDrive =  new PIDController(kPdrive, 0, 0);
  private PIDController pidSteer = new PIDController(kPsteer,0,0);
  private PIDController pidRot = new PIDController(kProt,0,0);



  /** Creates a new AimAndRangeCmd. */
  public AimAndRangeCmd(double kX, double kY , double kRot) 
  {
    //AimingUtils.trackReefPosition(limelightName,Id1, Id2);

    // Set the class ID
    this.kX = kX;
    this.kY = kY;
    this.kRot = kRot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      // Switch to pipeline 1
    LimelightHelpers.setPipelineIndex(limelightName, 0);

    // Set variables
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

    robotPosition();
    // Get raw AprilTag/Fiducial data
    

   // Check if a valid detection was found
   if (!finish) 
   {
    
    double steer_cmd = kMaxSpeed2Steer * pidSteer.calculate(targetPosition.getY(), kY);
    // try to drive forward until the target area reaches our desired area
    //double drive_cmd = MathUtil.clamp(pidDrive.calculate(distanceToTarget, kDistanceSetpoint),-kMaxSpeed2Drive,kMaxSpeed2Drive);
    double drive_cmd = kMaxSpeed2Drive *pidDrive.calculate(targetPosition.getX(), kX);

   double rot_cmd = kMaxSpeed2Rot *pidRot.calculate(targetPosition.getRotation().getDegrees(), kRot);
    // Drive the swerve subsystem with the calculated speeds
    swerveDrive.driveToTarget(() -> drive_cmd, () -> steer_cmd, () -> rot_cmd, () -> true);
  

        
    }
    else
    {
        finish = true;
    }
      
  }

  //Helper method to calculate robot position

  /**
   * Method to get the robot's current position
   * @return robotPosition
   */
  public Pose2d robotPosition()
  {

    // Get the current robot pose from the pose estimator
    Pose2d currentPose = swerveDrive.getPoseEstimator();

    // Get the robot's current position
    //Translation2d robotPosition = currentPose.getTranslation();
    targetPosition = currentPose;
    return targetPosition;
  }
 /* 
  // Helper method to calculate distance to a fiducial
  private double calculateDistance(RawFiducial fiducial) 
  {
      // Get the robot's current pose
      Pose2d currentPose = swerveDrive.getPoseEstimator();

      // Get the fiducial's position (assuming fiducial.x and fiducial.y are in meters)
      Translation2d fiducialPosition = new Translation2d(fiducial.txnc, fiducial.tync);

      // Calculate the distance between the robot and the fiducial
      double distance = currentPose.getTranslation().getDistance(fiducialPosition);

      return distance;
  }*/

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    swerveDrive.stopModules();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    // Optionally, add a condition to end the command
        // For example, you can check if the robot is close enough to the target
        Pose2d distanceToTarget = AimingUtils.estimateDistanceToTargetFromPose(swerveDrive, targetPosition);
        
        return finish || (distanceToTarget.getX() < 0.1 && distanceToTarget.getY() < 0.1 && distanceToTarget.getRotation().getDegrees() < 5); // Example threshold distance in meters
  }

}
