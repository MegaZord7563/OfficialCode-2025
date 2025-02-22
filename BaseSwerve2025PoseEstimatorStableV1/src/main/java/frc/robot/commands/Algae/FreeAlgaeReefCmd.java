// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Algae;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.swerve.LeaveCmd;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.CoralSubsystem;
import frc.robot.subsystems.lift.LiftSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FreeAlgaeReefCmd extends SequentialCommandGroup 
{

  private final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;
  private final LiftSubsystem liftSubsystem = RobotContainer.liftSubsystem;
  private final CoralSubsystem coralSubsystem = RobotContainer.coralSubsystem;

  /** Creates a new FreeAlgaeReffCmd. */
  public FreeAlgaeReefCmd(double angle , double position) 
  {
    // Create the subsystem
    
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelCommandGroup(
        new RunCommand(() -> coralSubsystem.setVelocity(0.5)),

        new RunCommand(() ->aCoralSubsystem.runMotor(angle)),

        new RunCommand(() -> liftSubsystem.runMotor(position, false))
      ), 
      new RunCommand(() -> coralSubsystem.stopMotor())
/* 
                new RunCommand(() -> aCoralSubsystem.runMotor(angle))
                .alongWith(new RunCommand(() -> liftSubsystem.runMotor(position, false)))
*/
  
                //new LeaveCmd()
              );
  }
}

