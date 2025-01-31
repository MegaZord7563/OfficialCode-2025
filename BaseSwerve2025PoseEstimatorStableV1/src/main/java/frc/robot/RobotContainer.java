// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;



//Java Imports
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DriverStation;
//WPILIB Imports
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

//Robot Imports
import frc.robot.Constants.OIConstants.JoystickDriverConstants;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Utils.TouchScreenInterface;
import frc.robot.commands.lift.LiftCommand;
import frc.robot.commands.lift.LiftCommandAuto;
import frc.robot.commands.pathPlanner.PathAfterFind;
import frc.robot.commands.pathPlanner.PathFindingCommand;
import frc.robot.commands.swerve.AimAndRangeCmd;
import frc.robot.commands.swerve.TrackAlgae;
import frc.robot.subsystems.Algae.AlgaSubsystem;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;
import frc.robot.subsystems.Climber.EscaladorSubsystem;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.CoralSubsystem;
import frc.robot.subsystems.Coral.RampSubsystem;
import frc.robot.subsystems.Swerve.SwerveSubsystem;
import frc.robot.subsystems.lift.LiftSubsystem;

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
  public static LimelightHelpers limelightHelpers = new LimelightHelpers();
  
  //Swerve Drive
  public static SwerveSubsystem swerveDrive = new SwerveSubsystem();
  
  //Lift Subsystem
  public static LiftSubsystem liftSubsystem = new LiftSubsystem();
  
  //Climber Subsystem
  public static EscaladorSubsystem escaladorSubsystem = new EscaladorSubsystem();
  
  //Alga Subsystem
  public static AlgaSubsystem algaSubsystem = new AlgaSubsystem();
  public static ArticuladorAlgaeSubsystem articuladorAlgaeSubsystem = new ArticuladorAlgaeSubsystem();
  
  //Coral Subsystem
  //public static CoralSubsystem coralSubsystem = new CoralSubsystem();
  public static ArticuladorCoralSubsystem articuladorCoralSubsystem = new ArticuladorCoralSubsystem();
  public static RampSubsystem rampSubsystem = new RampSubsystem();

  public static TouchScreenInterface touchScreenInterface = new TouchScreenInterface();
  


  //Lift Commands
  public static LiftCommand liftCmd = new LiftCommand();
  public static LiftCommandAuto liftAuto = new LiftCommandAuto();

  //Swerve commands
  public static TrackAlgae trackAlgaeCmd = new TrackAlgae(); 
  public static AimAndRangeCmd aimAndRangeCmd = new AimAndRangeCmd( 0, 0);
  
  //PathFindingCommand
  public static PathFindingCommand pathFindingCmd = new PathFindingCommand(swerveDrive, 
                                                    1, 0.7, 2.83, 0);

  public static PathFindingCommand pathFindingCmd2 = new PathFindingCommand(swerveDrive, 
                                                    1, 2.198, 4.921, 180);

  public static PathAfterFind pathAfterFind = new PathAfterFind(swerveDrive, 1, "PathAfterFind");
  public static PathAfterFind pathAfterFindRed = new PathAfterFind(swerveDrive, 1, "PathAfterFindRed");


  // Replace with CommandPS4Controller or CommandJoystick if needed
  //public static XboxController driverJoystick;
  //Controllers
  public static CommandXboxController driverJoystick;
  
  
  /**
  * Declare class SendableChoser()  
  *  Pop up selection of option on the SmartDashBoard   
  */
  SendableChooser<Command> chooserAuto;
 

  
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() 
  {

     // First of All - Register Named Commands
    NamedCommands.registerCommand("lift", liftAuto);
    
  
    // Add commands to the subsystem
    
    //Lift Commands
    liftCmd.addRequirements(liftSubsystem);
    liftAuto.addRequirements(liftSubsystem);

    //Swerve Commands
    trackAlgaeCmd.addRequirements(swerveDrive);
    aimAndRangeCmd.addRequirements(swerveDrive);
   
      
    // Build an auto chooser. This will use Commands.none() as the default option.
    chooserAuto = AutoBuilder.buildAutoChooser();
    
    //Instantiate PathPlannerAuto
    PathPlannerAuto autoPath = new PathPlannerAuto("FirstPathAutoNovo");
    PathPlannerAuto autoTest = new PathPlannerAuto("AutoTest");
    PathPlannerAuto autoField = new PathPlannerAuto("NewFieldAuto");
    PathPlannerAuto autoLift = new PathPlannerAuto("AutoLift"); 
    
   
    
    //autoLift.event("Lift").onTrue(new LiftCommandAuto());

    //Add options to the chooser
    chooserAuto.addOption("NovoAuto", autoPath);

    chooserAuto.addOption("AutoTest", autoTest);

    chooserAuto.addOption("NewFieldAuto", autoField);

    chooserAuto.addOption("AutoLift", autoLift);

    //Instantiate subsystems
    //swerveDrive 

    

    // Add the path to the auto chooser
   
    
    // Instantiate Xbox Controller
    driverJoystick= new CommandXboxController(JoystickDriverConstants.kDriverControllerPort);
     //Add to choices to smart dashboard
    SmartDashboard.putData("Autonomous", chooserAuto);
    
    //Default option
    /*chooserAuto.setDefaultOption("Autonomous 1", autoMundoSenai);
    chooserAuto.addOption("Autonomous 2", autoQuadrado);*/
   

    // Configure the button bindings
    configureBindings();

    //*******set default command to drive with joystick************/
     //The left stick controls translation of the robot.
     // Turning is controlled by the X axis of the right stick.
    
  
    //Runnable command
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

     
     driverJoystick.leftBumper().onTrue(new InstantCommand(swerveDrive::robotSlower)); 
     driverJoystick.rightBumper().onTrue(new InstantCommand(swerveDrive::robotFast)); 
    // driverJoystick.b().onTrue(new InstantCommand(swerveDrive::robotMaxSpeed)); 
              
    //find path
     driverJoystick.y().onTrue(pathAfterFind);
     driverJoystick.y().onFalse(new InstantCommand(()-> pathAfterFind.endCommand()));

     //drive to goal
     driverJoystick.a().whileTrue(new AimAndRangeCmd(1, 2));
     driverJoystick.b().whileTrue(trackAlgaeCmd);


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
    return chooserAuto.getSelected();
  }

}
