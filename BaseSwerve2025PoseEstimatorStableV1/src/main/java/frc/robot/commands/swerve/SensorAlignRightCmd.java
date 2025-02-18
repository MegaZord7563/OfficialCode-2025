// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SensorAlignRightCmd extends Command {
  private final SwerveSubsystem swerveDrive;
  private boolean finish = false;

  public SensorAlignRightCmd(SwerveSubsystem swerveDrive) {
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
    if (!swerveDrive.getSensorLeft() && !swerveDrive.getSensorRight()) 
    {
        swerveDrive.driveToTarget(() -> 0.0, () -> 0.05, () -> 0.0, () -> true);
      }
      else if(!swerveDrive.getSensorLeft() && swerveDrive.getSensorRight())
      {
        System.out.println("Parar Robo");
        //swerveDrive.driveToTarget(() -> 0.0, () -> 0.0, () -> 0.0, () -> true);
        
             
        //steerPosition = robotPosition().getY();
        finish = true;
    }

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
