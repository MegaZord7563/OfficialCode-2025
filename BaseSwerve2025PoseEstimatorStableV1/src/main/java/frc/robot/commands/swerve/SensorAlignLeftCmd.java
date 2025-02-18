// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SensorAlignLeftCmd extends Command {
  private final SwerveSubsystem swerveDrive;
  private boolean finish = false;
  private final double kMaxSpeed2Steer = 0.15; 
  private PIDController pidSteer = new PIDController(0.1,0,0);

  double steerPosition;

  public SensorAlignLeftCmd(SwerveSubsystem swerveDrive) {
    this.swerveDrive = swerveDrive;
    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("SensorAlign Initialized");
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!swerveDrive.getSensorLeft() && !swerveDrive.getSensorRight()) {
        swerveDrive.driveToTarget(() -> 0.0, () -> -0.12, () -> 0.0, () -> true);
      }
      else if (swerveDrive.getSensorLeft()) {
        swerveDrive.stopModules();
        swerveDrive.driveToTarget(() -> 0.0, () -> 0.0, () -> 0.0, () -> true);
        finish = true;
        steerPosition = robotPosition().getY();
    }

    double steer_cmd = kMaxSpeed2Steer * pidSteer.calculate(robotPosition().getY(),steerPosition);
    swerveDrive.driveToTarget(() -> 0.0, () -> steer_cmd, () -> 0.0,() -> true);
  }

    public Pose2d robotPosition()
  {

    // Get the current robot pose from the pose estimator
    Pose2d currentPose = swerveDrive.getPoseEstimator();

    // Get the robot's current position
    //Translation2d robotPosition = currentPose.getTranslation();
    //targetPosition = currentPose;
    return currentPose;
  }
    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerveDrive.stopModules();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish;
  }
}
