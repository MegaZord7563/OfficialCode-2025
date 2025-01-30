// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;


// WPI imports
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

// Robot imports
import frc.robot.RobotContainer;
import frc.robot.Limelight.AimingUtils;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AimAndRangeCmd extends Command 
{
  // Instantiate the object
  private final SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
  private final Translation2d targetPosition = new Translation2d(0, 0);
  private static String limelightName = "limelight-front";

  /** Creates a new AimAndRangeCmd. */
  public AimAndRangeCmd() 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      // Switch to pipeline 0
    LimelightHelpers.setPipelineIndex(limelightName, 1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // Get the angular velocity needed to aim at the target
        double angularVelocity = AimingUtils.aimAtTarget(limelightName);

        // Get the forward speed needed to range to the target
        double forwardSpeed = AimingUtils.rangeToTarget(limelightName);

        // Drive the swerve subsystem with the calculated speeds
        swerveDrive.driveRobotOriented(()->forwardSpeed,()-> 0.0, ()->angularVelocity,()->true);
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
    // Optionally, add a condition to end the command
        // For example, you can check if the robot is close enough to the target
        double distanceToTarget = AimingUtils.estimateDistanceToTargetFromPose(swerveDrive, targetPosition);
        return distanceToTarget < 0.1; // Example threshold distance in meters
  }

}
