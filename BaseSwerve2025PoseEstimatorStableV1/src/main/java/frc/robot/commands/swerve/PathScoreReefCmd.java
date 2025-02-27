// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;

//PathPlanner imports
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

// WPI imports
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.PathPlannerConstants;
// Robot imports
import frc.robot.RobotContainer;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.Utils.OperatorHub;
import frc.robot.Utils.SelectScore;
import frc.robot.Utils.TouchScreenInterface.VirtualButton;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class PathScoreReefCmd extends Command 
{
  // Instantiate the object
  private static SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
  private  Pose2d targetPosition = new Pose2d(0.1, 0.1, Rotation2d.fromDegrees(0.1));

  private static OperatorHub operatorHub=RobotContainer.operatorHub;
  
    public Command pathScoreReefCmd = RobotContainer.pathScoreReefCmd;
  
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
    private boolean kCoralStaton1 =false;
    private boolean kBButton =false;
  
  
    //Path Variables:
  
    private double setEndVelocity = 0;
  
    private final double maxVelocity;

    private boolean runPath = false;
  
  
    //double kX, double kY , double kRot
    /** Creates a new AimAndRangeCmd. */
    public PathScoreReefCmd( double maxVelocity)
    {
    this.maxVelocity = maxVelocity;

    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      // Switch to pipeline 1
    System.out.println("PathScoreReef Command Started!!");
    // Set variables
    finish = false;
    runPath = false;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

    if(!RobotContainer.driverJoystick.getHID().getXButton()){
        endCommand();
    }
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
    
    kCoralStaton1 = RobotContainer.operatorHub.getSelectTwo();

    //kBButton =RobotContainer.driverJoystick.getHID().getBButton();

    // Get raw AprilTag/Fiducial data
    if (kCoralStaton1) 
    {
        System.out.println("Coral Station 1 selected !!");
        targetPosition = new Pose2d(1.05, 0.86, Rotation2d.fromDegrees(54));
        runPath = true;
    }
    else if (kA)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        System.out.println("Position A selected !!");
        runPath = true;
    }
    else if (kB)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kC)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kD)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kE)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kF)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kG)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        System.out.println("Position G selected !!");
        runPath = true;

    }
    else if (kH)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        System.out.println("Position H selected !!");
        runPath = true;
    }
    else if (kI)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kJ)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kK)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }
    else if (kL)
    {
        targetPosition =SelectScore.poseToScore(RobotContainer.touchScreenInterface);
        runPath = true;
    }else
    {

    finish = true;
    runPath = false;

    }
    
   // Check if a valid detection was found
   if (runPath) 
   {
    Pose2d targetPose = new Pose2d(targetPosition.getX(), targetPosition.getY(),targetPosition.getRotation());

    // Create the constraints to use while pathfinding
    PathConstraints constraints = new PathConstraints(
        maxVelocity, PathPlannerConstants.maxAccelerationPath,
            PathPlannerConstants.maxAngularVelocityRadPerSec, 
            PathPlannerConstants.maxAngularAccelerationRadPerSecSq);
      
    // Since AutoBuilder is configured, we can use it to build pathfinding commands
    pathScoreReefCmd = AutoBuilder.pathfindToPose(
            targetPose,
            constraints,
            setEndVelocity// Goal end velocity in meters/sec
    );

    pathScoreReefCmd.schedule();



    }



  }

// End path command
public void endCommand(){
    pathScoreReefCmd.cancel();
    PPHolonomicDriveController.overrideXYFeedback(() -> swerveDrive.getPoseEstimator().getX(), ()-> swerveDrive.getPoseEstimator().getY());
    PPHolonomicDriveController.overrideRotationFeedback(()-> swerveDrive.getPoseEstimator().getRotation().getRadians());
    finish = true;
}


public boolean getRunPath()
{
return runPath;
}



  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    System.out.println("Path Score Command Done!!");
    PPHolonomicDriveController.clearXYFeedbackOverride();
    PPHolonomicDriveController.clearRotationFeedbackOverride();
    swerveDrive.stopModules();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
        return finish ;
  }

}
