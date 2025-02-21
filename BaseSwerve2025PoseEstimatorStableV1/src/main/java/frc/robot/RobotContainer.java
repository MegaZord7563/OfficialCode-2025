// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;



//Java Imports
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.cameraserver.CameraServer;
//WPILIB Imports
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj.Joystick;
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
import frc.robot.Constants.OIConstants.MesinhaJoy1Constants;
import frc.robot.Constants.OIConstants.MesinhaJoy2Constants;
import frc.robot.Constants.SubsystemsConstants.EscaladorConstants;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Utils.OperatorHub;
import frc.robot.Utils.TouchScreenInterface;
import frc.robot.commands.Algae.AlgaeManualCmd;
import frc.robot.commands.Algae.FreeAlgaeReefCmd;
import frc.robot.commands.Ramp.HomingEndGame;
import frc.robot.commands.Ramp.InitializeRamp;
import frc.robot.commands.Ramp.RampPickCoralCmd;
import frc.robot.commands.coral.Articulador1Cmd;
import frc.robot.commands.coral.AcoralManualCmd;

import frc.robot.commands.coral.CoralStationCmd;
import frc.robot.commands.coral.PickL1Cmd;
import frc.robot.commands.lift.LiftCommand;

import frc.robot.commands.lift.LiftManualCmd;
import frc.robot.commands.pathPlanner.PathAfterFind;
import frc.robot.commands.score.HomingPosition;
import frc.robot.commands.score.ScoreLevel;
import frc.robot.commands.swerve.AimAndRangeCmd;
import frc.robot.commands.swerve.LeaveCmd;
import frc.robot.commands.swerve.PathCoralStationCmd;
import frc.robot.commands.swerve.PathScoreReefCmd;
import frc.robot.commands.swerve.SensorAlignLeftCmd;
import frc.robot.commands.swerve.SensorAlignRightCmd;
import frc.robot.commands.swerve.TrackAlgae;
import frc.robot.subsystems.LedControl;
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
  public static LedControl ledControl = new LedControl();
  
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
 
  public static LiftManualCmd liftManualCmd = new LiftManualCmd(operatorHub);

  //public static AlgaeCoralManualCmd algaeCoralManualCmd = new AlgaeCoralManualCmd(operatorHub);

  
  //Coral Commands
  public static Articulador1Cmd articulador1Cmd = new Articulador1Cmd();
  public static RampPickCoralCmd rampCoral = new RampPickCoralCmd();
  public static CoralStationCmd coralStationCmd = new CoralStationCmd();  
  
  public static AcoralManualCmd aCoralManualCmd = new AcoralManualCmd(operatorHub);
 
  //Algae Coral Command
  //
  
  public static AlgaeManualCmd algaeManual = new AlgaeManualCmd(operatorHub);

  /**
   * SEQUENTIAL COMMANDS
   */
  public static ScoreLevel scoreLevel = new ScoreLevel();
  public static HomingPosition homingPosition = new HomingPosition();
  public static InitializeRamp initializeRamp = new InitializeRamp(); 
  public static HomingEndGame homingEndGame = new HomingEndGame();
  public static PickL1Cmd pickL1Cmd = new PickL1Cmd();
  public static FreeAlgaeReefCmd  freeAlgaeReefCmd = new FreeAlgaeReefCmd();
  public static PathCoralStationCmd pathCoralStationCmd = new PathCoralStationCmd(1.0);


  //Swerve commands
  public static TrackAlgae trackAlgaeCmd = new TrackAlgae(); 
  public static AimAndRangeCmd aimAndRangeCmd = new AimAndRangeCmd( );
  public static LeaveCmd leaveCmd = new LeaveCmd();
  public static PathScoreReefCmd pathScoreReefCmd = new PathScoreReefCmd(1.0);
  public static SensorAlignLeftCmd sensorAlignLeft = new SensorAlignLeftCmd(swerveDrive);
  public static SensorAlignRightCmd sensorAlignRight = new SensorAlignRightCmd(swerveDrive);
  
  //PathFindingCommand


  public static PathAfterFind pathAfterFind = new PathAfterFind(swerveDrive, 1, "PathAfterFind");
  public static PathAfterFind pathAfterFindRed = new PathAfterFind(swerveDrive, 1, "PathAfterFindRed");


  // Replace with CommandPS4Controller or CommandJoystick if needed
  //public static XboxController driverJoystick;
  //Controllers

  
  // Creates UsbCamera 
  UsbCamera usbCamera;
  
  
  /**
  * Declare class SendableChoser()  
  *  Pop up selection of option on the SmartDashBoard   
  */
  SendableChooser<Command> chooserAuto;
 

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() 
  {
   //Configure camera 
  
    usbCamera = CameraServer.startAutomaticCapture();
    // Set the resolution
    usbCamera.setResolution(640, 480);
    // Set the frames per second  
    usbCamera.setFPS(30);
    // Set the pixel format
    usbCamera.setPixelFormat(PixelFormat.kMJPEG);
    
    //usbCamera.setExposureAuto();
    //usbCamera.setExposureManual(0);
    
    
    //liftCmd = new LiftCommand();
    //articulador1Cmd = new Articulador1Cmd();
    //scoreLevel = new ScoreLevel();

    NamedCommands.registerCommand("PathScoreReef", pathScoreReefCmd);
    NamedCommands.registerCommand("lift", liftCmd);
  
    // Add commands to the subsystem
    

    //Swerve Commands
    trackAlgaeCmd.addRequirements(swerveDrive);
    aimAndRangeCmd.addRequirements(swerveDrive);
    leaveCmd.addRequirements(swerveDrive);
    pathScoreReefCmd.addRequirements(swerveDrive);
    sensorAlignLeft.addRequirements(swerveDrive);
    sensorAlignRight.addRequirements(swerveDrive);


    //Lift Commands
    liftCmd.addRequirements(liftSubsystem,ledControl,articuladorCoralSubsystem);

    homingPosition.addRequirements(liftSubsystem,articuladorCoralSubsystem);
    
    //liftAuto.addRequirements(liftSubsystem);
    
    liftManualCmd.addRequirements(liftSubsystem);
    liftSubsystem.setDefaultCommand(liftManualCmd);

    algaeManual.addRequirements(articuladorAlgaeSubsystem);
    articuladorAlgaeSubsystem.setDefaultCommand(algaeManual);
  
    aCoralManualCmd.addRequirements(articuladorCoralSubsystem);
    coralStationCmd.addRequirements(articuladorCoralSubsystem);

    articuladorCoralSubsystem.setDefaultCommand(aCoralManualCmd);

    pickL1Cmd.addRequirements(rampSubsystem,articuladorAlgaeSubsystem);
    homingEndGame.addRequirements(rampSubsystem,articuladorAlgaeSubsystem);
    initializeRamp.addRequirements(rampSubsystem,articuladorAlgaeSubsystem);
    rampCoral.addRequirements(rampSubsystem,articuladorAlgaeSubsystem);
    scoreLevel.addRequirements(rampSubsystem,articuladorAlgaeSubsystem);

        
    freeAlgaeReefCmd.addRequirements(articuladorCoralSubsystem,liftSubsystem);


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
                                      ( ()->driverJoystick.getLeftY(),
                                        ()->driverJoystick.getLeftX(),
                                        ()->-driverJoystick.getRightX(),
                                        ()->true
                                      ), 
                                      swerveDrive
                                    )//.onlyIf(()-> !driverJoystick.getHID().getXButton())
                          )
                                  ; 
    
    
    
                                                 
    
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
     /**
      * XBOX CONTROLLER BUTTONS
      */

     //Changes robot speed - speeding up
      driverJoystick.leftBumper().whileTrue(
                                            new InstantCommand(() -> {swerveDrive.robotSlower();
                                                                      ledControl.setWhiteLed();
                                                                      })); 
     driverJoystick.rightBumper().whileTrue(
                                            new InstantCommand(() -> {swerveDrive.robotFast();
                                                                      ledControl.setPinkLed();
                                                                      })); 



     driverJoystick.leftBumper().and(driverJoystick.rightBumper())  
                                .whileFalse(new InstantCommand(()->{swerveDrive.robotMaxSpeed();
                                                                    ledControl.setRedLed();
                                                                    })); 
     
     //set command to homing position
     driverJoystick.y().whileTrue(homingPosition);
     //set position to Score
     driverJoystick.x().whileTrue(pathScoreReefCmd);
     driverJoystick.x().whileFalse(new InstantCommand (()->pathScoreReefCmd.endCommand()).onlyIf(()->pathScoreReefCmd.getRunPath()));

    driverJoystick.b().whileTrue(pathCoralStationCmd);
    driverJoystick.b().whileFalse(new InstantCommand (()->pathCoralStationCmd.endCommand()));

    //driverJoystick.b().whileTrue(pathScoreReefCmd.onlyIf(()->!driverJoystick.getHID().getXButton()));

    //Prepare to Pick Coral
     //driverJoystick.b().onTrue(coralStationCmd);
     //set command to score level
     driverJoystick.a().whileTrue(scoreLevel);

    //Home backRobot
     //driverJoystick.y().whileTrue(homingEndGame);

     //Align robot to score Right
     driverJoystick.povRight().whileTrue(sensorAlignRight);
     //Align robot to score Left
     driverJoystick.povLeft().whileTrue(sensorAlignLeft);
     //Algae AI
     driverJoystick.povUp().whileTrue(trackAlgaeCmd);


     /**
      * OPERATOR CONTROLS
      */

    JoystickButton leaveAlgaeReef = new JoystickButton (mesinhaJoy2, operatorHub.kSix);
    leaveAlgaeReef.whileTrue(freeAlgaeReefCmd);

    //Set command to climber
    JoystickButton climb = new JoystickButton (mesinhaJoy2, operatorHub.kFive);
    climb.whileTrue(new InstantCommand(() -> escaladorSubsystem.runMotors(EscaladorConstants.escaladorClimbPosition, true))
            .onlyIf(()->rampSubsystem.isHomedBoolean()));
/* 
    climb.whileFalse(new InstantCommand(ledControl::setGreenLed)
                    .onlyIf(()->(escaladorSubsystem.isDistanceValid() && !escaladorSubsystem.getSensorClimber())));
//*/
    Trigger prepareClimb = new Trigger(()->escaladorSubsystem.isDistanceValid() && escaladorSubsystem.getSensorClimber());
    prepareClimb.onTrue(new InstantCommand(ledControl::setGreenLed));

    //JoystickButton climbHoming = new JoystickButton (mesinhaJoy2, operatorHub.kSix);
    //climbHoming.whileTrue(new InstantCommand(()->escaladorSubsystem.runMotors(0,false)));
    //driverJoystick.rightBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(EscaladorConstants.escaladorClimbPosition,true))));
    //driverJoystick.leftBumper().whileTrue((new InstantCommand(()->escaladorSubsystem.runMotors(0,false))));

    //Initialize Ramp
    //OperatorHub 2 Button 1
    JoystickButton coralStation = new JoystickButton(mesinhaJoy1, operatorHub.kEight);
    coralStation.onTrue(coralStationCmd);

    //Pick Position L1
    //OperatorHub 2 Button 6
    JoystickButton homeEndGame = new JoystickButton(mesinhaJoy2, operatorHub.kNine);
    homeEndGame.onTrue(homingEndGame);

    //Score L1
    //OperatorHub 1 Button 1
    JoystickButton initRamp = new JoystickButton(mesinhaJoy2, operatorHub.kSeven);
    initRamp.onTrue(initializeRamp);

    //Pick Algae - testado 
    JoystickButton pickAlgae = new JoystickButton(mesinhaJoy1, operatorHub.kThree);
    pickAlgae.whileTrue(new InstantCommand(()-> articuladorAlgaeSubsystem.runMotor(85)));
    
    //Score Algae - testado
    JoystickButton scoreAlgae = new JoystickButton(mesinhaJoy1, operatorHub.kFour);
    scoreAlgae.whileTrue(new InstantCommand(()-> articuladorAlgaeSubsystem.runMotor(8)));
    
    //Pick Coral
   /*  JoystickButton pickCoral = new JoystickButton(mesinhaJoy1, operatorHub.kOne);
    pickCoral.whileTrue(new ConditionalCommand(
                              new InstantCommand(()-> coralSubsystem.runMotor(20)), 
                              new InstantCommand(()->algaSubsystem.startAlga(9.5)), 
                              ()-> operatorHub.getSelectOne()
                            ));

    pickCoral.whileFalse(new ConditionalCommand(
                              new InstantCommand(()-> coralSubsystem.setVelocity(0))
                                .onlyIf(()->(!operatorHub.getOne() && !operatorHub.getTwo())),
                              new InstantCommand(()->algaSubsystem.resetEncoder())
                                .onlyIf(()->(!operatorHub.getOne() && !operatorHub.getTwo())), 
                              ()-> operatorHub.getSelectOne()
                            ));*/


   /*  shootCoral.whileTrue(new ConditionalCommand(
                              new InstantCommand(()-> coralSubsystem.runMotor(-20)), 
                              new InstantCommand(()-> algaSubsystem.startAlga(-9.5)), 
                              ()-> operatorHub.getSelectOne()
                            )); */
    
                            //JoystickButton shootCoral = new JoystickButton(mesinhaJoy1, operatorHub.kTwo);

    JoystickButton shootCoral = new JoystickButton(mesinhaJoy1, operatorHub.kTwo);
    shootCoral.whileTrue(new InstantCommand(()-> coralSubsystem.runMotor(coralSubsystem.getPosition()+20.0))
    .onlyIf(()->operatorHub.getSelectOne()));  

    shootCoral.whileFalse(new InstantCommand(()-> coralSubsystem.stopMotor())
                                            .onlyIf(()->(operatorHub.getSelectOne() && operatorHub.getOne()))); 
    
    JoystickButton backCoral = new JoystickButton(mesinhaJoy1, operatorHub.kOne);
    backCoral.whileTrue(new InstantCommand(()-> coralSubsystem.runMotor(coralSubsystem.getPosition()-20))
                                            .onlyIf(()->operatorHub.getSelectOne()));  
    

    backCoral.whileFalse(new InstantCommand(()-> coralSubsystem.stopMotor())
                                            .onlyIf(()->(operatorHub.getSelectOne() && operatorHub.getTwo()))); 
    

    JoystickButton shootAlgae = new JoystickButton(mesinhaJoy1, operatorHub.kOne);
    shootAlgae.whileTrue(new RunCommand(()-> algaSubsystem.startAlga(algaSubsystem.getPosition() + 10))
                                            .onlyIf(()-> !operatorHub.getSelectOne()).until(()->algaSubsystem.getPosition()==9.3));  
    shootAlgae.whileFalse(new InstantCommand(()-> coralSubsystem.stopMotor())
                                            .onlyIf(()->(!operatorHub.getSelectOne() && !operatorHub.getOne()))); 
    
    JoystickButton backAlgae = new JoystickButton(mesinhaJoy1, operatorHub.kTwo);
    backAlgae.whileTrue(new RunCommand(()-> algaSubsystem.startAlga(algaSubsystem.getPosition()-10))
                                            .onlyIf(()-> !operatorHub.getSelectOne()).until(()->algaSubsystem.getPosition()== -9.3));  
    backAlgae.whileFalse(new InstantCommand(()-> coralSubsystem.stopMotor())
                                            .onlyIf(()->(!operatorHub.getSelectOne() && !operatorHub.getTwo()))); 
  
    
    JoystickButton selectonCoralStation = new JoystickButton(mesinhaJoy1, operatorHub.kSix);
    //selectonCoralStation.whileTrue(new InstantCommand(ledControl::setBlueLed));
    //selectonCoralStation.whileFalse(new InstantCommand(ledControl::setWhiteLed));
    
    JoystickButton positionCoralStation = new JoystickButton(mesinhaJoy1, operatorHub.kSeven);
    //positionCoralStation.whileTrue(new InstantCommand(ledControl::setPurpleLed));
    //positionCoralStation.whileFalse(new InstantCommand(ledControl::setOrangeLed));


    
     //outTakeTrapForward.whileTrue(new RunCommand (()-> outTakeCmd.selectForward()));
     //outTakeTrapForward.whileFalse(new RunCommand(() -> outTakeCmd.selectStopForward()));

     //set command to shoot coral
     



/* 

     operatorJoystick.leftBumper().whileTrue(ramp'Coral');*/
     
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
