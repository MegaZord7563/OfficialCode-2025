// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;


import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
// WPI imports
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.PathPlannerConstants;
// Robot imports
import frc.robot.RobotContainer;
import frc.robot.Limelight.AimingUtils;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Utils.SelectScore;
import frc.robot.Utils.TouchScreenInterface;
import frc.robot.Utils.TouchScreenInterface.VirtualButton;
import frc.robot.commands.pathPlanner.PathFindingCommand;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class PathScoreReefCmd extends Command 
{
  // Instantiate the object
  private static SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
      private  Pose2d targetPosition = new Pose2d(0, 0, null);
      private  Pose2d currentPosition = new Pose2d(0, 0, null);
  
    private static String limelightName = "limelight-front";
  
    public Command pathFindingCommand;
  
    // Variables
    //private final double kX, kY, kRot; // Replace with the correct class ID for the game piece
    private static boolean finish = false; 
  
  
    // Declare the boolean variables   
    private boolean kA = false;
    private boolean kB = false;
    private boolean kC = false;
    private boolean kD = false;
    private boolean kE = false;
    private boolean kF = false;
    private boolean kG = false;
    private boolean kH = false;
    private boolean kI = false;
    private boolean kJ = false;
    private boolean kK = false;
    private boolean kL = false;
  
  
    //Path Variables:
  
    private  double positionX;
    private  double positionY;
    private  double rotation;
    private double setEndVelocity = 0;
  
    private final double maxVelocity;
  
  
    //double kX, double kY , double kRot
    /** Creates a new AimAndRangeCmd. */
    public PathScoreReefCmd(SwerveSubsystem swerveDrive, double maxVelocity)
    {
    this.swerveDrive = swerveDrive;
    this.maxVelocity = maxVelocity;

    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      // Switch to pipeline 1
    LimelightHelpers.setPipelineIndex(limelightName, 0);
    System.out.println("Aim and Range Command Started!!");
    // Set variables
    finish = false;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

    kA = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kA);
    kB = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kB);  
    kC = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kC);
    kD = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kD);
    kE = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kE);
    kF = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kF);
    kG = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kG);
    kH = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kH);
    kI = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kI);
    kJ = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kJ);
    kK = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kK);
    kL = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL);


    currentPosition =robotPosition();
    // Get raw AprilTag/Fiducial data
    if (kA)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 1);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        System.out.println("Position A selected !!");
    }
    else if (kB)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 2);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kC)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 3);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kD)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 4);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kE)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 5);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kF)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 6);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kG)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 7);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        System.out.println("Position G selected !!");

    }
    else if (kH)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 8);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        System.out.println("Position H selected !!");
    }
    else if (kI)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 9);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kJ)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 10);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kK)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 11);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    else if (kL)
    {
        //AimingUtils.trackReefPosition(limelightName, 0, 12);
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
    }
    
   // Check if a valid detection was found
   if (!finish) 
   {
    Pose2d targetPose = new Pose2d(targetPosition.getX(), targetPosition.getY(),targetPosition.getRotation());

    // Create the constraints to use while pathfinding
    PathConstraints constraints = new PathConstraints(
        maxVelocity, PathPlannerConstants.maxAccelerationPath,
            PathPlannerConstants.maxAngularVelocityRadPerSec, 
            PathPlannerConstants.maxAngularAccelerationRadPerSecSq);
      
    // Since AutoBuilder is configured, we can use it to build pathfinding commands
    pathFindingCommand = AutoBuilder.pathfindToPose(
            targetPose,
            constraints,
            setEndVelocity// Goal end velocity in meters/sec
    );

    pathFindingCommand.schedule();


    }

    if(pathFindingCommand.isFinished()){
      finish = true;
  }
    
  }


  /**
   * Method to get the robot's current position
   * @return robotPosition
   */
  public Pose2d robotPosition()
  {

    Pose2d currentPose = swerveDrive.getPoseEstimator();

    return currentPose;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    System.out.println("Aim and Range Command Done!!");

    //PathCommand

    PPHolonomicDriveController.clearXYFeedbackOverride();
    PPHolonomicDriveController.clearRotationFeedbackOverride();
    swerveDrive.stopModules();

  }

    public void endCommand (){
    pathFindingCommand.cancel();
    PPHolonomicDriveController.overrideXYFeedback(()->swerveDrive.getPoseEstimator().getX(), ()->swerveDrive.getPoseEstimator().getY());
    PPHolonomicDriveController.overrideRotationFeedback(()-> swerveDrive.getPoseEstimator().getRotation().getRadians());
    System.out.println("EndCommand");
    finish = true;
  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    // Optionally, add a condition to end the command
        // For example, you can check if the robot is close enough to the target
        Pose2d distanceToTarget = AimingUtils.estimateDistanceToTargetFromPose(swerveDrive, targetPosition);
        
        return false;//finish || (distanceToTarget.getX() < 0.1 && distanceToTarget.getY() < 0.1 && distanceToTarget.getRotation().getDegrees() < 5); // Example threshold distance in meters
  }

}
