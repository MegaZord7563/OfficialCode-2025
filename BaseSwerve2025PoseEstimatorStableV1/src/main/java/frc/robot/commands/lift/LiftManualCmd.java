package frc.robot.commands.lift;





//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.Utils.OperatorHub;
import frc.robot.subsystems.lift.LiftSubsystem;

public class LiftManualCmd extends Command
{
   // Declare the boolean variables   
    private boolean finish = false;

    private double encoderPosition, currentPosition;
   
    
    public final OperatorHub operatorHub;//=RobotContainer.operatorHub;

    //Declare doubles
   

    
    

  // Create the subsystem
  private final LiftSubsystem liftSubsystem = RobotContainer.liftSubsystem;

  /** Creates a new AngleCmd. */
  public LiftManualCmd(OperatorHub operatorHub) 
  {
    this.operatorHub=operatorHub;
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
    
    currentPosition=liftSubsystem.getPosition();
    
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
     

    if(operatorHub.getRedAxisUp(0.75))
    {
      liftSubsystem.setSpeed(0.3);
      encoderPosition = liftSubsystem.getPosition();
    }
    else if(operatorHub.getRedAxisDown(-0.75))
    {
      liftSubsystem.setSpeed(-0.3);
      encoderPosition = liftSubsystem.getPosition();
    }
    else if(operatorHub.getRedAxisUpDownHome())
    {
      liftSubsystem.holdMotor(encoderPosition);
      
    }
    else if (liftSubsystem.isHomed().get())
    {
      
      finish = true;
    }
     
  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    
    liftSubsystem.holdMotor(liftSubsystem.getPosition());
    System.out.println("Lift Manual Command Done!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return finish;///||RobotContainer.driverJoystick.getHID().getStartButton();
  }

}

