// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;



//Java Imports

//WPILIB Imports
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OIConstants.JoystickDriverConstants;

import frc.robot.subsystems.EscaladorSubsystem;
import frc.robot.subsystems.LiftSubsystem;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer 
{
  // The robot's subsystems and commands are defined here...
  
  //Subsystems
  //Put all subsystems here
  
  public static LiftSubsystem liftSubsystem = new LiftSubsystem();
  public static EscaladorSubsystem escaladorSubsystem = new EscaladorSubsystem();
  
  


  //Commands
  
  

  // Replace with CommandPS4Controller or CommandJoystick if needed
  //public static XboxController driverJoystick;
  //Controllers
  public static CommandXboxController driverJoystick;
  
  
  /**
  * Declare class SendableChoser()  
  *  Pop up selection of option on the SmartDashBoard   
  */

  //Declare SendableChooser
  SendableChooser<Command> chooserAuto;
 

  
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() 
  {

     // Register Named Commands
    

  
    // Add commands to the subsystem
    

    // Add the path to the auto chooser
   
    
    // Instantiate Xbox Controller
    driverJoystick= new CommandXboxController(JoystickDriverConstants.kDriverControllerPort);
     //Add to choices to smart dashboard
    
    
    //Default option
    /*chooserAuto.setDefaultOption("Autonomous 1", autoMundoSenai);
    chooserAuto.addOption("Autonomous 2", autoQuadrado);*/
   

    // Configure the button bindings
    configureBindings();

    //*******set default command to drive with joystick************/
     //The left stick controls translation of the robot.
     // Turning is controlled by the X axis of the right stick.
    
  
    //Runnable command
    
                                                 
    
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() 
  {
    //X positions of the Wheels 
    //new JoystickButton(driverJoystick, Button.kX.value).whileTrue(new RunCommand(() -> swerveDrive.setX(),swerveDrive));
    //driverJoystick.x().whileTrue(new RunCommand(() -> swerveDrive.setX(),swerveDrive));
         
     //driverJoystick.start().onTrue(new InstantCommand(swerveDrive::zeroHeading));
     

     ///*Changes robot speed - slowing down
     //JoystickButton throttle1 = new JoystickButton (driverJoystick,XboxController.Button.kLeftBumper.value); 
     //throttle1.onTrue(new InstantCommand(swerveDrive::robotSlower)); 

    //driverJoystick.y().onFalse((new RunCommand(()->escaladorSubsystem.stopMotors())));
    //driverJoystick.y().onTrue(new RunCommand(()->escaladorSubsystem.runMotors(1000.0, 
    //                                                                            true)));
    //driverJoystick.x().onTrue(new RunCommand(()->escaladorSubsystem.runMotors(1000.0, 
    //                                                                            false)));
    //driverJoystick.x().onFalse((new RunCommand(()->escaladorSubsystem.stopMotors())));

    //driverJoystick.a().onTrue((new RunCommand(()->liftSubsystem.moveLift(1000, false))));
    //driverJoystick.a().onFalse((new RunCommand(()->liftSubsystem.stopLift())));

    driverJoystick.b().whileTrue((new RunCommand(()->liftSubsystem.moveLift(1, false))));
    //driverJoystick.b().onFalse((new RunCommand(()->liftSubsystem.stopLift())));

    driverJoystick.x().whileTrue((new RunCommand(()->liftSubsystem.moveLift(0, false))));

                                                                               
     //driverJoystick.y().onFalse(new InstantCommand(()-> pathAfterFindRed.endCommand()));
     

   
  }
  

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() 
  {
    // An example command will be run in autonomous
    return null;
  }

}
