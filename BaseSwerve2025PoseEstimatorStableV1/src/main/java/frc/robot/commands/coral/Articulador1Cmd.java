package frc.robot.commands.coral;	


//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.Utils.TouchScreenInterface.VirtualButton;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;





public class Articulador1Cmd extends Command
{
     
    // Declare the boolean variables
    private boolean l1 = false;
    private boolean l2 = false;
    private boolean l3 = false;
    private boolean l4 = false;
    private boolean finish = false;

    private double encoderPosition = 0;


    // Create the subsystem
    private final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;

  /** Creates a new AngleCmd. */
  public Articulador1Cmd() 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aCoralSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    // Print the initialization of the command
    System.out.println("Articulador 1 Command Initialized");
   
    // Set the button values to false
    l1=false;
    l2=false;
    l3=false;
    l4=false;
    finish =false;

    // Reset the encoder
    //aCoralSubsystem.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

    encoderPosition = aCoralSubsystem.getPosition();
    // Get the button values
    l1 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL1);
    l2 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL2);
    l3 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL3);
    l4 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL4);
    
    
    // Move the articulator
    if (l1)
    {
      System.out.println("L1 SELECTED");
      aCoralSubsystem.runMotor(0.2);
      if (encoderPosition >=0.195) finish=true;
    }
    // Move the articulator
    if (l2)
    {
      System.out.println("L2 SELECTED");
      aCoralSubsystem.runMotor(0.4);
      if (encoderPosition >=0.192) finish=true;
    }
    //  Move the articulator
    if (l3)
    {
      System.out.println("L3 SELECTED");
      aCoralSubsystem.runMotor(0.4);
      if (encoderPosition >=0.392) finish=true;
    }
    //  Move the articulator
    if (l4)
    {
      System.out.println("L4 SELECTED");
      aCoralSubsystem.runMotor(0.6);
      if (encoderPosition >=0.595) finish=true;
      
    }


  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    // Stop the articulator
    //aCoralSubsystem.stopMotor();
    // Print the end of the command
    System.out.println("Articulador 1 Command Done");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return finish;
  }

}

