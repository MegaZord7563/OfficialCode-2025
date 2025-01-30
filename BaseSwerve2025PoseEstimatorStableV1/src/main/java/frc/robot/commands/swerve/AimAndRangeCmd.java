// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;


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
  private  Translation2d targetPosition = new Translation2d(0, 0);
  private static String limelightName = "limelight-front";

  // Variables
  private final int kId1, kId2; // Replace with the correct class ID for the game piece
  private static boolean finish = false; 



  /** Creates a new AimAndRangeCmd. */
  public AimAndRangeCmd(int Id1, int Id2) 
  {
    //AimingUtils.trackReefPosition(limelightName,Id1, Id2);

    // Set the class ID
    kId1 = Id1;
    kId2 = Id2;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      // Switch to pipeline 1
    LimelightHelpers.setPipelineIndex(limelightName, 1);

    // Set variables
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // Get raw AprilTag/Fiducial data
    RawFiducial[] fiducials = LimelightHelpers.getRawFiducials(limelightName);
    
    // Variables
    boolean alvoValido = LimelightHelpers.getTV(limelightName);
   // double distToRobot = 0.1;    // Distance to robot
    RawFiducial bestDetection = null;

    // Iterate through all fiducial to find the best one
    for (RawFiducial fiducial : fiducials) 
    {
        if ((fiducial.id == kId1 || fiducial.id == kId2) && alvoValido && fiducial.ta > 0.5)   
        {
          
            
            bestDetection = fiducial;
        }
    }
    

   // Check if a valid detection was found
   if (bestDetection != null && !finish) 
   {
    
    // Get the angular velocity needed to aim at the target
        double angularVelocity = AimingUtils.aimAtTarget(limelightName);

        // Get the forward speed needed to range to the target
        double forwardSpeed = AimingUtils.rangeToTarget(limelightName);

        // Drive the swerve subsystem with the calculated speeds
        swerveDrive.driveRobotOriented(()->forwardSpeed,()-> 0.0, ()->angularVelocity,()->true);

        robotPosition();
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
  public Translation2d robotPosition()
  {

    // Get the current robot pose from the pose estimator
    Pose2d currentPose = swerveDrive.getPoseEstimator();

    // Get the robot's current position
    Translation2d robotPosition = currentPose.getTranslation();
    targetPosition = robotPosition;
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
        double distanceToTarget = AimingUtils.estimateDistanceToTargetFromPose(swerveDrive, targetPosition);
        
        return finish || distanceToTarget < 0.1; // Example threshold distance in meters
  }

}
