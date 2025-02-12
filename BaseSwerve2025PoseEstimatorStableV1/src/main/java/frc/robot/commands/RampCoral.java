package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

//WPI Imports

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.SubsystemsConstants.AlgaIntakeConstants;
import frc.robot.Constants.SubsystemsConstants.RampConstants;
//Robot imports
import frc.robot.RobotContainer;
import frc.robot.Algae.ArtiAlgaeCmd;
import frc.robot.commands.Ramp.RampPickCoralCmd;
import frc.robot.commands.coral.Articulador1Cmd;
import frc.robot.commands.coral.CoralPositionCmd;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.RampSubsystem;
import frc.robot.subsystems.lift.LiftSubsystem;




// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RampCoral extends SequentialCommandGroup 
{

  //Declare commands

  private static CoralPositionCmd aCoralSubsystem = RobotContainer.coralPositionCmd;
  private static RampPickCoralCmd aPickRampCoralCmd= RobotContainer.rampPickCoralCmd;
  

  /** Creates a new ScoreLevel. */
  public RampCoral() 
  {

    System.out.println("Coral Ramp initialized");
    
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands
    (

    new ArtiAlgaeCmd(17, 15),
    aPickRampCoralCmd,
    aCoralSubsystem,
    new ArtiAlgaeCmd(0, 1)

  )
  ;
    System.out.println("Homing Done!!!");

  }
}
