package frc.robot.commands;



import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Constants.PathPlannerConstants;
import frc.robot.subsystems.Swerve.SwerveSubsystem;


public class PathFindingCommand extends Command {

  // Instantiate the object
  private final SwerveSubsystem swerveDrive;

  //Creates variables
  private Command pathFindingCommand;
  private final double positionX;
  private final double positionY;
  private final double rotation;
  private double setEndVelocity = 0;

  private final double maxVelocity;
  private boolean finish;
  

  /** Creates a new AngleCmd. */
  public PathFindingCommand(SwerveSubsystem swerveDrive, double maxVelocity, 
                            double positionX, double positionY, double rotation) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.swerveDrive = swerveDrive;
    this.maxVelocity = maxVelocity;
    this.positionX = positionX;
    this.positionY = positionY;
    this.rotation = rotation;


    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Pose2d targetPose = new Pose2d(positionX, positionY, Rotation2d.fromDegrees(rotation));

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


    if(pathFindingCommand.isFinished()){
        finish = true;
    }
} 
  
  public void setEndVelocity(double setVelocity){
    setEndVelocity = setVelocity;
  }



  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerveDrive.stopModules();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish || RobotContainer.driverJoystick.getHID().getBButtonReleased();
  }
}
