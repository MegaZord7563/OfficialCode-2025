package frc.robot.commands.swerve;


//Java Imports
import java.io.IOException;
import org.json.simple.parser.ParseException;

//WPILIB Imports
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.PathPlannerConstants;

//Robot Imports
import frc.robot.subsystems.Swerve.SwerveSubsystem;


public class PathClimb extends Command 
{

  // Instantiate the object
  private final SwerveSubsystem swerveDrive;

  //Creates variables
  private Command findToPath ;
  private final double maxVelocity;
  private final String pathName;
  private PathPlannerPath pathToFollow;

 //Creates a boolean variable
  private boolean finish;
  

  /** Creates a new AngleCmd. */
  public PathClimb(SwerveSubsystem swerveDrive, double maxVelocity, String pathName) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.swerveDrive = swerveDrive;
    this.maxVelocity = maxVelocity;
    this.pathName = pathName;

    // Add the requirements for the swerveDrive subsystem
    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    finish = false;

    // Create the constraints to use while pathfinding
    PathConstraints constraints = new PathConstraints(
      maxVelocity, PathPlannerConstants.maxAccelerationPath,
      PathPlannerConstants.maxAngularVelocityRadPerSec, 
      PathPlannerConstants.maxAngularAccelerationRadPerSecSq
    );

    // Load the path from the file
    try 
    {
      pathToFollow = PathPlannerPath.fromPathFile(pathName);
    } catch (FileVersionException | IOException | ParseException e) {
      e.printStackTrace();
    }

    // Create the pathfinding command
    findToPath = AutoBuilder.pathfindThenFollowPath(pathToFollow, constraints);

    // Schedule the pathfinding command
    findToPath.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // Check if the pathfinding command is finished
    if(findToPath.isFinished())
    {
        finish = true;
    }
  } 


  public void endCommand ()
  {
    findToPath.cancel();
    PPHolonomicDriveController.overrideXYFeedback(()->swerveDrive.getPoseEstimator().getX(), ()->swerveDrive.getPoseEstimator().getY());
    PPHolonomicDriveController.overrideRotationFeedback(()-> swerveDrive.getPoseEstimator().getRotation().getRadians());
    System.out.println("EndCommand");
    finish = true;
  }
  

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    PPHolonomicDriveController.clearXYFeedbackOverride();
    PPHolonomicDriveController.clearRotationFeedbackOverride();
    swerveDrive.stopModules();
    System.out.println("Comando Finalizado");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return finish;
  }
  
}
