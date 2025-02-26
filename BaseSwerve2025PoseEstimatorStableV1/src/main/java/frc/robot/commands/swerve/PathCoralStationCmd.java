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
public class PathCoralStationCmd extends Command 
{
  // Instantiate the object
  private static SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
  private  Pose2d targetPosition = new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(0.0));

  
  
    public Command pathCoralStationCmd = RobotContainer.pathCoralStationCmd;
  
    // Variables
    //private final double kX, kY, kRot; // Replace with the correct class ID for the game piece
    private static boolean finish = false; 
  
  
    // Declare the boolean variables   
    
    
  
    //Path Variables:
  
    private double setEndVelocity = 0;
  
    private final double maxVelocity;

    
  
  
    //double kX, double kY , double kRot
    /** Creates a new AimAndRangeCmd. */
    public PathCoralStationCmd( double maxVelocity)
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
   

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

    if(!RobotContainer.driverJoystick.getHID().getBButton())
    {
        endCommand();
    }
    // Select the target position whether it is Coral Station 1 or Coral Station 2
    targetPosition = swerveDrive.getBlueAlliance() ? targetPosition1() : targetPosition2();
        
  

    //kBButton =RobotContainer.driverJoystick.getHID().getBButton();

    // Get raw AprilTag/Fiducial data
    
      //Blue Coral Station 1
        //System.out.println("Coral Station 1 selected !!");
        //targetPosition = new Pose2d(1.18, 1.0, Rotation2d.fromDegrees(55));
       
    
    
   // Check if a valid detection was found
  
    Pose2d targetPose = new Pose2d(targetPosition.getX(), targetPosition.getY(),targetPosition.getRotation());

    // Create the constraints to use while pathfinding
    PathConstraints constraints = new PathConstraints(
        maxVelocity, PathPlannerConstants.maxAccelerationPath,
            PathPlannerConstants.maxAngularVelocityRadPerSec, 
            PathPlannerConstants.maxAngularAccelerationRadPerSecSq);
      
    // Since AutoBuilder is configured, we can use it to build pathfinding commands
    pathCoralStationCmd = AutoBuilder.pathfindToPose(
            targetPose,
            constraints,
            setEndVelocity// Goal end velocity in meters/sec
    );

    pathCoralStationCmd.schedule();



    



  }
//Helper method  select the target position
public Pose2d targetPosition1()
{
    //Blue Coral Station 1
    System.out.println("Coral Station 1 selected !!");
    return targetPosition = new Pose2d(1.18, 1.0, Rotation2d.fromDegrees(55));
}

public Pose2d targetPosition2()
{
    //Blue Coral Station 2
    System.out.println("Coral Station 2 selected !!");
    return targetPosition = new Pose2d(16.1, 7, Rotation2d.fromDegrees(-126));
}

// End path command
public void endCommand()
{
    pathCoralStationCmd.cancel();
    PPHolonomicDriveController.overrideXYFeedback(() -> swerveDrive.getPoseEstimator().getX(), ()-> swerveDrive.getPoseEstimator().getY());
    PPHolonomicDriveController.overrideRotationFeedback(()-> swerveDrive.getPoseEstimator().getRotation().getRadians());
    finish = true;
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
