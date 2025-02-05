// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.score;

//WPI Imports
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.commands.coral.Articulador1Cmd;
import frc.robot.commands.lift.LiftCommand;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.lift.LiftSubsystem;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ScoreLevel extends SequentialCommandGroup 
{

  //Declare commands
  public static final LiftCommand liftCmd = RobotContainer.liftCmd;
  public static final Articulador1Cmd articulador1Cmd = RobotContainer.articulador1Cmd;

  //Declare subsystems
  public static final LiftSubsystem liftSubsystem = RobotContainer.liftSubsystem;
  public static final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;


  /** Creates a new ScoreLevel. */
  public ScoreLevel() 
  {
   
    
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands
    (
        liftCmd,
        new WaitCommand(0.5),
        articulador1Cmd,
        new WaitCommand(0.20),
        new InstantCommand(()->aCoralSubsystem.runMotor(0.0)),
        new WaitCommand(0.20),
        new InstantCommand(()->liftSubsystem.runMotor(0.0,true))
    );

  }
}
