// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Ramp;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.RampSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class RampPickCoralCmd extends Command {
  /** Creates a new ArtiAlgaeCmd. */

  private final RampSubsystem aRampSubsystem = RobotContainer.rampSubsystem;

  private double encoderPosition = 0;
  private double setpointPosition = -4;
  private double setpointRange = -3.9;
  private boolean finish;

  public RampPickCoralCmd() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aRampSubsystem);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Articulador Algae Command Initialized");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    encoderPosition = aRampSubsystem.getPosition();
    aRampSubsystem.runMotor(setpointPosition);

    if (encoderPosition <= setpointRange){
      System.out.println("Ramp Conditional");
      finish = true;
    }
  }



  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Articulador Algae Command Done");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
        return finish;
  }
}
