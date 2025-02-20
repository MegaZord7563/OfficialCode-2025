// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.coral;


//CommandBase Imports
import edu.wpi.first.wpilibj2.command.RunCommand;

//WPI Imports
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;
import frc.robot.subsystems.Coral.RampSubsystem;





// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PickL1Cmd extends SequentialCommandGroup 
{

  //Declare commandsS
  private static RampSubsystem rampSubsystem  = RobotContainer.rampSubsystem;
  private static ArticuladorAlgaeSubsystem aAlgaeSubsystem = RobotContainer.articuladorAlgaeSubsystem;

  /** Creates a new ScoreLevel. */
  public PickL1Cmd() 
  {
    //encoderAcoral = aCoralSubsystem.getPosition();
    //encoderLift = liftSubsystem.getPosition();
    System.out.println("Homing initialized");
    
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands
    (

     new RunCommand(()->aAlgaeSubsystem.runMotor(20))
                        .until(() -> aAlgaeSubsystem.inPosition(20).get()),

    new RunCommand(()->rampSubsystem.runMotor(70))
                        .onlyIf(() -> aAlgaeSubsystem.inPosition(20).get())
                        .until(()-> rampSubsystem.inPosition(70).get())
      
    );
    System.out.println("Pick L1 Done!!!");

  }
}
