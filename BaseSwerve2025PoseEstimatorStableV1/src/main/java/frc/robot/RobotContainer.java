// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;



//Java Imports
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.Joystick;
//WPILIB Imports
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

//Robot Imports
import frc.robot.Constants.OIConstants.JoystickDriverConstants;
import frc.robot.Constants.OIConstants.JoystickOperatorConstants;
import frc.robot.Constants.OIConstants.MesinhaJoy1Constants;
import frc.robot.Constants.OIConstants.MesinhaJoy2Constants;
import frc.robot.Constants.SubsystemsConstants.AlgaIntakeConstants;
import frc.robot.Constants.SubsystemsConstants.EscaladorConstants;
import frc.robot.Constants.SubsystemsConstants.RampConstants;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Utils.OperatorHub;
import frc.robot.Utils.TouchScreenInterface;
import frc.robot.commands.AlgaeCoral.AlgaeCoralManualCmd;
import frc.robot.commands.Ramp.HomingEndGame;
import frc.robot.commands.Ramp.InitializeRamp;
import frc.robot.commands.Ramp.RampPickCoralCmd;
import frc.robot.commands.coral.Articulador1Cmd;
import frc.robot.commands.coral.CoralStationCmd;
import frc.robot.commands.coral.PickL1Cmd;
import frc.robot.commands.lift.LiftCommand;
import frc.robot.commands.lift.LiftCommandAuto;
import frc.robot.commands.lift.LiftManualCmd;
import frc.robot.commands.pathPlanner.PathAfterFind;
import frc.robot.commands.pathPlanner.PathFindingCommand;
import frc.robot.commands.score.HomingPosition;
import frc.robot.commands.score.ScoreLevel;
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
  //Operator Controller
  public static TouchScreenInterface touchScreenInterface = new TouchScreenInterface();
  
  public static CommandXboxController driverJoystick = 
                                      new CommandXboxController(JoystickDriverConstants.kDriverControllerPort);
  
  public static Joystick mesinhaJoy1 = 
                          new Joystick(MesinhaJoy1Constants.kJoy1Port);
  public static Joystick mesinhaJoy2 = 
                          new Joystick(MesinhaJoy2Constants.kJoy2Port);

  public static final OperatorHub operatorHub = 
                                  new OperatorHub(mesinhaJoy1, mesinhaJoy2);
  /**
   * SUBSYSTEMS
   */
  //Put all subsystems here
  public static LimelightHelpers limelightHelpers = new LimelightHelpers();
  
  //Swerve Drive
  public static SwerveSubsystem swerveDrive = new SwerveSubsystem();
  
  //Lift Subsystem
  public static  LiftSubsystem liftSubsystem = new LiftSubsystem();
  
  //Climber Subsystem
  public static EscaladorSubsystem escaladorSubsystem = new EscaladorSubsystem();
  
  //Alga Subsystem
  public static AlgaSubsystem algaSubsystem = new AlgaSubsystem();
  public static ArticuladorAlgaeSubsystem articuladorAlgaeSubsystem = new ArticuladorAlgaeSubsystem();
  
  
  //Coral Subsystem
  public static CoralSubsystem coralSubsystem = new CoralSubsystem();
  public static ArticuladorCoralSubsystem articuladorCoralSubsystem = new ArticuladorCoralSubsystem();
  public static RampSubsystem rampSubsystem = new RampSubsystem();
  
/**
 * COMMANDS
 */

  //Lift Commands
  public  static LiftCommand liftCmd =new LiftCommand();
  public static LiftCommandAuto liftAuto = new LiftCommandAuto();
  public static LiftManualCmd liftManualCmd = new LiftManualCmd(operatorHub);

  public static AlgaeCoralManualCmd algaeCoralManualCmd = new AlgaeCoralManualCmd(operatorHub);

  
  //Coral Commands
  public static Articulador1Cmd articulador1Cmd = new Articulador1Cmd();
  public static RampPickCoralCmd rampCoral = new RampPickCoralCmd();
  public static CoralStationCmd coralStationCmd = new CoralStationCmd();  
 
  //Algae Coral Command
  //
  
  //public static AlgaeCoralManualCmd algaeCoralManual = new AlgaeCoralManualCmd(operatorHub);

  /**
   * SEQUENTIAL COMMANDS
   */
  public static ScoreLevel scoreLevel = new ScoreLevel();
  public static HomingPosition homingPosition = new HomingPosition();
  public static InitializeRamp initializeRamp = new InitializeRamp(); 
  public static HomingEndGame homingEndGame = new HomingEndGame();
  public static PickL1Cmd pickL1Cmd = new PickL1Cmd();


  //Swerve commands
  public static TrackAlgae trackAlgaeCmd = new TrackAlgae(); 
  public static AimAndRangeCmd aimAndRangeCmd = new AimAndRangeCmd( 0, 0, 0);
  
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

  
  
  
  /**
  * Declare class SendableChoser()  
  *  Pop up selection of option on the SmartDashBoard   
  */
  SendableChooser<Command> chooserAuto;
 

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() 
  {
    //liftCmd = new LiftCommand();
    //articulador1Cmd = new Articulador1Cmd();
    //scoreLevel = new ScoreLevel();

     // First of All - Register Named Commands
    NamedCommands.registerCommand("lift", liftAuto);
    
  
    // Add commands to the subsystem
    

    //Swerve Commands
    trackAlgaeCmd.addRequirements(swerveDrive);
    aimAndRangeCmd.addRequirements(swerveDrive);


    //Lift Commands
    liftCmd.addRequirements(liftSubsystem);
    
    //liftAuto.addRequirements(liftSubsystem);
    
    liftManualCmd.addRequirements(liftSubsystem);
    liftSubsystem.setDefaultCommand(liftManualCmd);

    algaeCoralManualCmd.addRequirements(articuladorAlgaeSubsystem);
    articuladorAlgaeSubsystem.setDefaultCommand(algaeCoralManualCmd);
  


    //Coral Commands
    articulador1Cmd.addRequirements(articuladorCoralSubsystem);

    //algaSubsystem.setDefaultCommand(algaeCoralManual);


    // Build an auto chooser. This will use Commands.none() as the default option.
    chooserAuto = AutoBuilder.buildAutoChooser();
    
    //Instantiate PathPlannerAuto
    PathPlannerAuto autoPath = new PathPlannerAuto("FirstPathAutoNovo");
    PathPlannerAuto autoTest = new PathPlannerAuto("AutoTest");
    PathPlannerAuto autoField = new PathPlannerAuto("NewFieldAuto");
    PathPlannerAuto autoLift = new PathPlannerAuto("AutoLift"); 
    
   


    //Add options to the chooser
    chooserAuto.addOption("NovoAuto", autoPath);

    chooserAuto.addOption("AutoTest", autoTest);

    chooserAuto.addOption("NewFieldAuto", autoField);

    chooserAuto.addOption("AutoLift", autoLift);

    //Instantiate subsystems
    //swerveDrive 
    // Add the path to the auto chooser
   
    
    // Instantiate Xbox Controller
    
    
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
    
  
    /**
    * DEFAULT COMMANDS
    */
    //liftSubsystem.setDefaultCommand(liftCmd);

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
         
    
     /**
      * XBOX CONTROLLER BUTTONS
      */

     //Changes robot speed - speeding up
     //driverJoystick.leftBumper().whileTrue(new InstantCommand(swerveDrive::robotSlower)); 
     //driverJoystick.rightBumper().whileTrue(new InstantCommand(swerveDrive::robotFast)); 
     //driverJoystick.leftBumper().and(driverJoystick.rightBumper()).whileFalse(new InstantCommand(swerveDrive::robotMaxSpeed)); 
     
     //set command to homing position
     driverJoystick.start().whileTrue(homingPosition);
     
     //set command to track algae
     //driverJoystick.y().whileTrue(trackAlgaeCmd);

     

     //set command to score level
     driverJoystick.a().whileTrue(scoreLevel);
     
     driverJoystick.b().onTrue(coralStationCmd);
     
     driverJoystick.y().whileTrue(homingEndGame);

     //driverJoystick.b().whileTrue(new InstantCommand(()->rampSubsystem.runMotor(105)));
     //driverJoystick.b().whileFalse(new InstantCommand(()->rampSubsystem.runMotor(0)));

     //lift manual control up and down	
     //driverJoystick.povUp().whileTrue(new InstantCommand(() -> liftSubsystem.setSpeed(0.3)));
     //driverJoystick.povUp().whileFalse(new InstantCommand(() -> liftSubsystem.holdMotor()));

     //driverJoystick.povDown().whileTrue(new InstantCommand(() -> liftSubsystem.setSpeed(-0.3)));
     //driverJoystick.povDown().whileFalse(new InstantCommand(() -> liftSubsystem.holdMotor()));
     
     //driverJoystick.rightBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(EscaladorConstants.escaladorClimbPosition,true))));
     //driverJoystick.leftBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(0,false))));

     

     /**
      * OPERATOR CONTROLS
      */

    //Set command to climber
    JoystickButton climb = new JoystickButton (mesinhaJoy2, operatorHub.kFive);
    climb.whileTrue(new InstantCommand(()->escaladorSubsystem.runMotors(EscaladorConstants.escaladorClimbPosition,true)));
    
    JoystickButton climbHoming = new JoystickButton (mesinhaJoy2, operatorHub.kSix);
    climbHoming.whileTrue(new InstantCommand(()->escaladorSubsystem.runMotors(0,false)));
    //driverJoystick.rightBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(EscaladorConstants.escaladorClimbPosition,true))));
    //driverJoystick.leftBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(0,false))));



     //outTakeTrapForward.whileTrue(new RunCommand (()-> outTakeCmd.selectForward()));
     //outTakeTrapForward.whileFalse(new RunCommand(() -> outTakeCmd.selectStopForward()));

     //set command to shoot coral
     



/* 
     operatorJoystick.x().whileTrue(new InstantCommand(()->articuladorAlgaeSubsystem.setVelocity(AlgaIntakeConstants.articulatorVelocity)));
     operatorJoystick.x().whileFalse(new InstantCommand(()->articuladorAlgaeSubsystem.runMotor(articuladorAlgaeSubsystem.getPosition())));

     operatorJoystick.y().whileTrue(new InstantCommand(()->articuladorAlgaeSubsystem.setVelocity(AlgaIntakeConstants.articulatorVelocity * -1)));
     operatorJoystick.y().whileFalse(new InstantCommand(()->articuladorAlgaeSubsystem.runMotor(articuladorAlgaeSubsystem.getPosition())));

     operatorJoystick.b().whileTrue(new InstantCommand(()->articuladorAlgaeSubsystem.runMotor(AlgaIntakeConstants.pickAlgaePIDPosition)));
     operatorJoystick.b().whileFalse(new InstantCommand(()->articuladorAlgaeSubsystem.runMotor(articuladorAlgaeSubsystem.getPosition())));

     operatorJoystick.a().whileTrue(new InstantCommand(()->articuladorAlgaeSubsystem.runMotor(AlgaIntakeConstants.startAlgaePIDPosition)));
     operatorJoystick.a().whileFalse(new InstantCommand(()->articuladorAlgaeSubsystem.runMotor(articuladorAlgaeSubsystem.getPosition())));

     operatorJoystick.povDown().whileTrue(new InstantCommand(()->algaSubsystem.setVelocity(AlgaIntakeConstants.shooterAlgaeVelocity *-1)));
     operatorJoystick.povDown().whileFalse(new InstantCommand(()->algaSubsystem.setVelocity(0)));

     operatorJoystick.povUp().whileTrue((new InstantCommand(()->algaSubsystem.setVelocity(AlgaIntakeConstants.shooterAlgaeVelocity))));
     operatorJoystick.povUp().whileFalse((new InstantCommand(()->algaSubsystem.setVelocity(0))));

     operatorJoystick.povLeft().whileTrue((new InstantCommand(()->escaladorSubsystem.setVelocity(EscaladorConstants.escaladorVelocity))));
     operatorJoystick.povLeft().whileFalse((new InstantCommand(()->escaladorSubsystem.setVelocity(0))));

     operatorJoystick.povRight().whileTrue((new InstantCommand(()->escaladorSubsystem.setVelocity(EscaladorConstants.escaladorVelocity*-1))));
     operatorJoystick.povRight().whileFalse((new InstantCommand(()->escaladorSubsystem.setVelocity(0))));

     //operatorJoystick.rightBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(EscaladorConstants.escaladorClimbPosition))));
     //operatorJoystick.leftBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(0))));


     operatorJoystick.back().whileTrue((new InstantCommand(()->rampSubsystem.runMotor(RampConstants.startPosition))));
     operatorJoystick.start().whileTrue((new InstantCommand(()->rampSubsystem.runMotor(RampConstants.pickPosition))));

     operatorJoystick.leftBumper().whileTrue(rampCoral);*/
     
    //find path
     //driverJoystick.y().onTrue(pathAfterFind);
     //driverJoystick.y().onFalse(new InstantCommand(()-> pathAfterFind.endCommand()));

     //drive to goal
     //driverJoystick.x().whileTrue(new AimAndRangeCmd(1, 2, 3));


     //driverJoystick.b().whileTrue(new PathFindingCommand(swerveDrive, 1, SelectScore.poseToScore(touchScreenInterface).getX(),
     //        SelectScore.poseToScore(touchScreenInterface).getY(), SelectScore.poseToScore(touchScreenInterface).getRotation().getDegrees()));
     
     
     
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
