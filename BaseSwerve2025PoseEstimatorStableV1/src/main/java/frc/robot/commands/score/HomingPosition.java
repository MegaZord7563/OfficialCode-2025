// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.score;



//CommandBase Imports
import edu.wpi.first.wpilibj2.command.RunCommand;

//WPI Imports
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.commands.swerve.LeaveCmd;
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
  
  //private static LeaveCmd leaveCmd = RobotContainer.leaveCmd;

  

  

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
      new LeaveCmd().alongWith(
                                new RunCommand(()->aCoralSubsystem.runMotor(0.0))
                                .onlyIf(() -> !aCoralSubsystem.isHomed().get())
                                .until(() -> aCoralSubsystem.isHomed().get())),
      /*leaveCmd,
      new RunCommand(()->aCoralSubsystem.runMotor(0.0))
                                        .onlyIf(() -> !aCoralSubsystem.isHomed().get())
                                        .until(() -> aCoralSubsystem.isHomed().get()),*/
     
      new RunCommand(()->liftSubsystem.runMotor(0.0, false))
                                      .onlyIf(() -> aCoralSubsystem.isHomed().get())
                                      .until(()->liftSubsystem.isHomed().get())
     
      
    );
    System.out.println("Homing Done!!!");

  }
}
