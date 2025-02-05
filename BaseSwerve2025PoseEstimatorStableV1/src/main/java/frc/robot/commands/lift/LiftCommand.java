package frc.robot.commands.lift;


//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.Utils.TouchScreenInterface.VirtualButton;
import frc.robot.subsystems.lift.LiftSubsystem;

public class LiftCommand extends Command
{
   // Declare the boolean variables   
    private boolean l1 = false;
    private boolean l2 = false;
    private boolean l3 = false;
    private boolean l4 = false;
    private boolean finish = false;

    private double encoderPosition = 0;

  // Create the subsystem
    private final LiftSubsystem liftSubsystem = RobotContainer.liftSubsystem;

  /** Creates a new AngleCmd. */
  public LiftCommand() 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(liftSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    // Print the initialization of the command
    System.out.println("Lift Command Initialized");

    finish = false;
   
    // Set the button values to false
    l1=false;
    l2=false;
    l3=false;
    l4=false;
    // Reset the encoder
    //liftSubsystem.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    encoderPosition = liftSubsystem.getPosition();
    // Get the button values
    l1 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL1);
    l2 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL2);
    l3 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL3);
    l4 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL4);
    
    // Move the lift
    if (l1)
    {
      System.out.println("L1 SELECTED");
      liftSubsystem.runMotor(0.5, false);
      if (encoderPosition >=0.492) finish=true;
      //{finish =true;}
    }
    if (l2)
    {
      System.out.println("L2 SELECTED");
      liftSubsystem.runMotor(0.8, false);
      if (encoderPosition >=0.792) finish=true;
     
    }
    if (l3)
    {
      System.out.println("L3 SELECTED");
      liftSubsystem.runMotor(1.0, false);
      if (encoderPosition >=0.992) finish=true;
    }
    if (l4)
    {
      System.out.println("L4 SELECTED");
      liftSubsystem.runMotor(1.2, false);
      if (encoderPosition >=1.195) finish=true;
    }


    
  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    // Stop the lift
    //liftSubsystem.stopMotor();
    // Print the end of the command
    System.out.println("Lift Command Done!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return finish;
  }

}

