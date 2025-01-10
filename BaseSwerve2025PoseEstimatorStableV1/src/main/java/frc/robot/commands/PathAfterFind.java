package frc.robot.commands;



import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.PathPlannerConstants;
import frc.robot.subsystems.Swerve.SwerveSubsystem;


public class PathAfterFind extends Command {

  // Instantiate the object
  private final SwerveSubsystem swerveDrive;

  //Creates variables
  private Command findToPath;
  private final double maxVelocity;
  private final String pathName;
  private PathPlannerPath pathToFollow;


  private boolean finish;
  

  /** Creates a new AngleCmd. */
  public PathAfterFind(SwerveSubsystem swerveDrive, double maxVelocity, String pathName) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.swerveDrive = swerveDrive;
    this.maxVelocity = maxVelocity;
    this.pathName = pathName;

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
       // Create the constraints to use while pathfinding
       PathConstraints constraints = new PathConstraints(
        maxVelocity, PathPlannerConstants.maxAccelerationPath,
            PathPlannerConstants.maxAngularVelocityRadPerSec, 
            PathPlannerConstants.maxAngularAccelerationRadPerSecSq);

    try {
      pathToFollow = PathPlannerPath.fromPathFile(pathName);
    } catch (FileVersionException | IOException | ParseException e) {
      e.printStackTrace();
    }

    findToPath = AutoBuilder.pathfindThenFollowPath(pathToFollow, constraints);

    findToPath.schedule();
    if(findToPath.isFinished()){
        finish = true;
    }
} 
  

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerveDrive.stopModules();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish;
  }
}
