// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Ramp;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;

import frc.robot.subsystems.Coral.RampSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class InitializeRamp extends SequentialCommandGroup 
{

  //Declare commands
  
  private static ArticuladorAlgaeSubsystem aAlgaeSubsystem = RobotContainer.articuladorAlgaeSubsystem;
  private static RampSubsystem rampSubsystem = RobotContainer.rampSubsystem;  

  /** Creates a new InitialiazeRamp. */
  public InitializeRamp() 
  {
    System.out.println("Initialize Command Group initialized");
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands
    (
      //Move the Algae to the 45 degrees position
      new RunCommand(()->aAlgaeSubsystem.runMotor(50))
                                        //.onlyIf(()-> aAlgaeSubsystem.isHomed().get())
                                        .until(() -> aAlgaeSubsystem.inPosition(50).get()),
      //Move the Ramp to the 105 degrees position
      new RunCommand(()->rampSubsystem.runMotor(105))
                                      //.onlyIf(() -> aAlgaeSubsystem.inPosition(38).get())
                                      .until(()->rampSubsystem.inPosition(103).get()),
       //Move the Algae to the 7 degrees position                         
      new RunCommand(()->aAlgaeSubsystem.runMotor(1.8))
                                      //.onlyIf(()->rampSubsystem.inPosition(103).get())
                                      .until(() -> aAlgaeSubsystem.inPosition(1.8).get())
      
    );
    
    System.out.println("Initialize Command Group Done!!!");
  }
}
