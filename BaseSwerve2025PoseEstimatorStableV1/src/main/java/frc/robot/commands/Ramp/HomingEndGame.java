// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Ramp;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LedControl;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;

import frc.robot.subsystems.Coral.RampSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class HomingEndGame extends SequentialCommandGroup 
{

  //Declare commands
  
  private static ArticuladorAlgaeSubsystem aAlgaeSubsystem = RobotContainer.articuladorAlgaeSubsystem;
  private static RampSubsystem rampSubsystem = RobotContainer.rampSubsystem;  
  private static LedControl ledControl = RobotContainer.ledControl;  

  /** Creates a new InitialiazeRamp. */
  public HomingEndGame() 
  {
    System.out.println("Initialize Command Group initialized");
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands
    (
      //Move the Algae to the 45 degrees position
      new RunCommand(()->aAlgaeSubsystem.runMotor(45))
                                        .onlyIf(() -> !rampSubsystem.isHomed().get())
                                        .until(() -> aAlgaeSubsystem.inPosition(45).get()),
      //Move the Ramp to the 0 degrees position
      new RunCommand(()->rampSubsystem.runMotor(0))
                                      .onlyIf(() -> aAlgaeSubsystem.inPosition(45).get())
                                      .until(()->rampSubsystem.isHomed().get()),
      //Move the Algae to the 0 degrees position                         
      new RunCommand(()->aAlgaeSubsystem.runMotor(0))
                                      .onlyIf(()->rampSubsystem.isHomed().get())
                                      .until(() -> aAlgaeSubsystem.isHomed().get()),
      new InstantCommand(ledControl::setBlackLed)

      

    );
    System.out.println("Initialize Command Group Done!!!");
  }
}
