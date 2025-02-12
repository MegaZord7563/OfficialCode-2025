// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.score;


import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

//WPI Imports

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
//Robot imports
import frc.robot.RobotContainer;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.lift.LiftSubsystem;




// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class HomingPosition extends SequentialCommandGroup 
{

  //Declare commands
  private static LiftSubsystem liftSubsystem  = RobotContainer.liftSubsystem;
  private static ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;

  

  

  /** Creates a new ScoreLevel. */
  public HomingPosition() 
  {
    //encoderAcoral = aCoralSubsystem.getPosition();
    //encoderLift = liftSubsystem.getPosition();
    System.out.println("Homing initialized");
    
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands
    (
      //new InstantCommand(()->liftSubsystem.getPosition()),
      //new InstantCommand(()->aCoralSubsystem.getPosition()),
      new InstantCommand(()->aCoralSubsystem.runMotor(0.0))
                                            .beforeStarting(aCoralSubsystem::getPosition)
                                            .until(()->aCoralSubsystem.isHomed().get()), 
                                            //.andThen(new WaitCommand(2.0)),
     
      new InstantCommand(()->liftSubsystem.runMotor(0.0, false))
                                          .beforeStarting(()->liftSubsystem.getPosition())
                                          .onlyIf(() -> aCoralSubsystem.isHomed().get())
                                          .until(()->liftSubsystem.isHomed().get())
  )
  ;
    System.out.println("Homing Done!!!");

  }
}
