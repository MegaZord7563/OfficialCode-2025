package frc.robot.commands.lift;



//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.subsystems.lift.LiftSubsystem;

public class LiftManualCmd extends Command
{
   // Declare the boolean variables   
    private boolean finish = false;

    //Declare doubles
    private double setPoint = 0.0;
    private double initialPosition;

    
    

  // Create the subsystem
  private final LiftSubsystem liftSubsystem = RobotContainer.liftSubsystem;

  /** Creates a new AngleCmd. */
  public LiftManualCmd(double targetPosition) 
  {
    setPoint = targetPosition;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(liftSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    // Print the initialization of the command
    System.out.println("Lift Manual Command Initialized");

    finish = false;
    initialPosition = liftSubsystem.getPosition();

    
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    //double currentPosition = liftSubsystem.getPosition();
   
    liftSubsystem.runMotor(setPoint , false);

    //if ((currentPosition-initialPosition)>100)finish=true;
     
  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    System.out.println("Lift Manual Command Done!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return finish ;
  }

}

