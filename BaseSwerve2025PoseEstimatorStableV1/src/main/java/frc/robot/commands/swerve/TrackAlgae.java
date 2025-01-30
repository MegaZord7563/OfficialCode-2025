// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.RobotContainer;
import frc.robot.Limelight.AimingUtils;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Limelight.LimelightHelpers.RawDetection;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TrackAlgae extends Command 
{

  // Instantiate the object
  public final SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
  private final Translation2d targetPosition = new Translation2d(0, 0);
  
  // Limelight name
  private static String limelightName = "limelight-front";

  // Variables
  private final int expectedClassId = 1; // Replace with the correct class ID for the game piece
  public static boolean finish = false;

  /** Creates a new TrackAlgae. */
  public TrackAlgae() 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    // Set pipeline
    LimelightHelpers.setPipelineIndex(limelightName, 0);

    // Set variables
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // Get raw neural detector results
    RawDetection[] detections = LimelightHelpers.getRawDetections(limelightName);
    RawDetection bestDetection = null;

    // Variables
    double maxArea = 0.5;
    boolean alvoValido = LimelightHelpers.getTV(limelightName);

    // Iterate through all detections to find the best one
    for (RawDetection detection : detections) 
    {
        if (detection.classId == expectedClassId && detection.ta > maxArea && alvoValido) 
        {
            maxArea = detection.ta;
            bestDetection = detection;
        }
    }

    // Check if a valid detection was found
    if (bestDetection != null && bestDetection.ta > 0.5 && !finish) 
    {
       
        // Get the angular velocity needed to aim at the target
        double angularVelocity = AimingUtils.aimAtTarget(limelightName);
        // Get the forward speed needed to range to the target
        double forwardSpeed = AimingUtils.rangeToTarget(limelightName);
        // Drive the swerve subsystem with the calculated speeds
        swerveDrive.driveRobotOriented(() -> forwardSpeed, () -> 0.0, () -> angularVelocity, () -> true);
    } 
    else
    {
      
      finish = true;
    }

  }

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
    double distanceToTarget = AimingUtils.estimateDistanceToTargetFromPose(swerveDrive, targetPosition);
    return finish || distanceToTarget < 0.1; // Example threshold distance in meters
  }
}
