// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutonomousCmd;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.CoralSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootCoralAutoCmd extends Command {

  public static CoralSubsystem coralSubsystem = RobotContainer.coralSubsystem;
  public static ArticuladorCoralSubsystem articuladorCoralSubsystem = RobotContainer.articuladorCoralSubsystem;
  boolean finish = false;

  /** Creates a new ShootCoralAutoCmd. */
  public ShootCoralAutoCmd() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(coralSubsystem);
    finish = false;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("ShootCoralAutoCmd' command initialized");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    coralSubsystem.runMotor(coralSubsystem.getPosition()+50.0);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    coralSubsystem.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return articuladorCoralSubsystem.sensorCoral();
  }
}
