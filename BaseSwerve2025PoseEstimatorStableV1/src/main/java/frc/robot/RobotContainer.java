// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import java.io.IOException;
import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//WPI Imports

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.PathPlannerConstants;
//LOCAL Imports
import frc.robot.Constants.OIConstants.JoystickDriverConstants;
import frc.robot.commands.AutoMundoSenai;
import frc.robot.commands.AutoQuadrado;
import frc.robot.commands.LiftCommand;
import frc.robot.commands.LiftCommandAuto;
import frc.robot.commands.PathAfterFind;
import frc.robot.commands.PathFindingCommand;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.Limelight.LimelightHelpers;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer 
{
  // The robot's subsystems and commands are defined here...
  public static LimelightHelpers limelightHelpers = new LimelightHelpers();
  public static SwerveSubsystem swerveDrive = new SwerveSubsystem();
  public static LiftSubsystem liftSubsystem = new LiftSubsystem();

/**
 * 
 */
  public static LiftCommand liftCmd = new LiftCommand();
  public static LiftCommandAuto liftAuto = new LiftCommandAuto();
  public static AutoMundoSenai autoMundoSenai = new AutoMundoSenai();
  public static AutoQuadrado autoQuadrado = new AutoQuadrado();
  public static PathFindingCommand pathFindingCmd = new PathFindingCommand(swerveDrive, 
  1, 0.7, 2.83, 0);

  public static PathFindingCommand pathFindingCmd2 = new PathFindingCommand(swerveDrive, 
  1, 2.198, 4.921, 180);

  public static PathAfterFind pathAfterFind = new PathAfterFind(swerveDrive, 1, "PathAfterFind");
  public static PathAfterFind pathAfterFindRed = new PathAfterFind(swerveDrive, 1, "PathAfterFindRed");


  // Replace with CommandPS4Controller or CommandJoystick if needed
  //public static XboxController driverJoystick;
  public static CommandXboxController driverJoystick;
  
  private static Command pathFindingCommand;

  private static PathPlannerPath pathToFollow;
  private static Command findToPath;
  /**
  * Declare class SendableChoser()  
  *  Pop up selection of option on the SmartDashBoard   
  */
  SendableChooser<Command> chooserAuto;
 

  
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() 
  {

     // Register Named Commands
    NamedCommands.registerCommand("lift", liftAuto);

  
    
    liftCmd.addRequirements(liftSubsystem);
    liftAuto.addRequirements(liftSubsystem);
   
      
    // Build an auto chooser. This will use Commands.none() as the default option.
    chooserAuto = AutoBuilder.buildAutoChooser();
    
    PathPlannerAuto autoPath = new PathPlannerAuto("FirstPathAutoNovo");
    PathPlannerAuto autoTest = new PathPlannerAuto("AutoTest");
    PathPlannerAuto autoField = new PathPlannerAuto("NewFieldAuto");
    PathPlannerAuto autoLift = new PathPlannerAuto("AutoLift"); 
    
   
    
    //autoLift.event("Lift").onTrue(new LiftCommandAuto());

    chooserAuto.addOption("NovoAuto", autoPath);

    chooserAuto.addOption("AutoTest", autoTest);

    chooserAuto.addOption("NewFieldAuto", autoField);

    chooserAuto.addOption("AutoLift", autoLift);

    //Instantiate subsystems
    //swerveDrive 

    // Create the constraints to use while pathfinding
    PathConstraints constraints = new PathConstraints(
            1.0, PathPlannerConstants.maxAccelerationPath,
            PathPlannerConstants.maxAngularVelocityRadPerSec, PathPlannerConstants.maxAngularAccelerationRadPerSecSq);


    try {
      pathToFollow = PathPlannerPath.fromPathFile("PathAfterFind");
    } catch (FileVersionException | IOException | ParseException e) {
      e.printStackTrace();
    }

    findToPath = AutoBuilder.pathfindThenFollowPath(pathToFollow, constraints);

    autoMundoSenai.addRequirements(swerveDrive);
    autoQuadrado.addRequirements(swerveDrive);
    
    // Instantiate Xbox Controller
    driverJoystick= new CommandXboxController(JoystickDriverConstants.kDriverControllerPort);
     //Add to choices to smart dashboard
    SmartDashboard.putData("Autonomous", chooserAuto);
    
    //Default option
    /*chooserAuto.setDefaultOption("Autonomous 1", autoMundoSenai);
    chooserAuto.addOption("Autonomous 2", autoQuadrado);*/
   

    configureBindings();

    //*******set default command to drive with joystick************/
     //The left stick controls translation of the robot.
     // Turning is controlled by the X axis of the right stick.

    
    swerveDrive.setDefaultCommand
                                  (new RunCommand
                                    (()-> swerveDrive.driveRobotOriented
                                      ( ()->-driverJoystick.getLeftY(),
                                        ()->-driverJoystick.getLeftX(),
                                        ()->-driverJoystick.getRightX(),
                                        ()->!driverJoystick.getHID().getAButton()
                                      ), 
                                      swerveDrive
                                    )
                                  ); 
                                                  

    if(driverJoystick.getHID().getYButton()){
      System.out.println("Y Pressed Container");
    }

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
    
    ///*Zeroing NavX2 It changes robot heading*/
     //JoystickButton robotChangeHeading = new JoystickButton (driverJoystick,XboxController.Button.kStart.value); 
     //robotChangeHeading.onTrue(new InstantCommand(swerveDrive::zeroHeading)); //reset gyroscope  

     driverJoystick.start().onTrue(new InstantCommand(swerveDrive::zeroHeading));
     

     ///*Changes robot speed - slowing down
     //JoystickButton throttle1 = new JoystickButton (driverJoystick,XboxController.Button.kLeftBumper.value); 
     //throttle1.onTrue(new InstantCommand(swerveDrive::robotSlower)); 

     //driverJoystick.y().whileTrue(new InstantCommand(swerveDrive::addPoseVision));
     driverJoystick.leftBumper().onTrue(new InstantCommand(swerveDrive::robotSlower)); 
     driverJoystick.rightBumper().onTrue(new InstantCommand(swerveDrive::robotFast)); 
    // driverJoystick.b().onTrue(new InstantCommand(swerveDrive::robotMaxSpeed)); 
     
     //driverJoystick.a().whileTrue(new RunCommand(() -> pathFindingCommand.schedule()));

     //driverJoystick.b().onTrue(liftAuto);
     //driverJoystick.b().onFalse(new InstantCommand(()-> pathFindingCmd.endCommand()));

     //driverJoystick.x().onTrue(pathFindingCmd2);
     //driverJoystick.x().onFalse(new InstantCommand(()-> pathFindingCmd2.endCommand()));

     //driverJoystick.y().onTrue(new InstantCommand(() -> findToPath.schedule()));

     driverJoystick.y().onTrue(pathAfterFind);
     driverJoystick.y().onFalse(new InstantCommand(()-> pathAfterFind.endCommand()));

     //driverJoystick.y().onTrue(pathAfterFindRed);
     //driverJoystick.y().onFalse(new InstantCommand(()-> pathAfterFindRed.endCommand()));
     
        
     ///*Changes robot speed - speeding up
     /*JoystickButton throttle2 = new JoystickButton (driverJoystick,XboxController.Button.kRightBumper.value); 
     throttle2.onTrue(new InstantCommand(swerveDrive::robotFast)); 

     JoystickButton throttleMax = new JoystickButton (driverJoystick,XboxController.Button.kB.value); 
     throttleMax.onTrue(new InstantCommand(swerveDrive::robotMaxSpeed)); */
     
     


   
  }
  

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() 
  {
    // An example command will be run in autonomous
    return chooserAuto.getSelected();
  }

}
