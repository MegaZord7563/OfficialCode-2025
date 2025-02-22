// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoScoreTeleop;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.coral.Articulador1Cmd;
import frc.robot.commands.coral.CoralStationCmd;
import frc.robot.commands.lift.LiftCommand;
import frc.robot.commands.score.HomingPosition;
import frc.robot.commands.swerve.PathCoralStationCmd;
import frc.robot.commands.swerve.PathScoreReefCmd;
import frc.robot.subsystems.LedControl;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.CoralSubsystem;
import frc.robot.subsystems.Coral.RampSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoScoreCmd extends SequentialCommandGroup 
{

  //Declare commands
  


  private static CoralStationCmd coralStationCmd = RobotContainer.coralStationCmd;
  private static PathCoralStationCmd pathCoralStationCmd = RobotContainer.pathCoralStationCmd;
  private static PathScoreReefCmd pathScoreReefCmd = RobotContainer.pathScoreReefCmd;
  private static LiftCommand liftCommand = RobotContainer.liftCmd;
  private static Articulador1Cmd articulador1Cmd = RobotContainer.articulador1Cmd;
  private static HomingPosition homingPosition = RobotContainer.homingPosition;

  private static CoralSubsystem coralSubsystem = RobotContainer.coralSubsystem;



  /** Creates a new InitialiazeRamp. */
  public AutoScoreCmd() 
  {
    System.out.println("Initialize Command Group initialized");
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands
    (
    pathCoralStationCmd,
    coralStationCmd,
    pathScoreReefCmd,

    new ParallelCommandGroup(
      liftCommand,
      articulador1Cmd
    ),
      new InstantCommand(() -> coralSubsystem.runMotor(coralSubsystem.getPosition()+30.0)),
      homingPosition
    );
    System.out.println("Initialize Command Group Done!!!");
  }
}
