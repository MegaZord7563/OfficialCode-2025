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

    // Reset the encoder
    aCoralSubsystem.resetArtCoralEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // Get the button values
    l1 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL1);
    l2 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL2);
    l3 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL3);
    l4 = RobotContainer.touchScreenInterface.getButtonValue(VirtualButton.kL4);
    
    
    // Move the articulator
    if (l1)
    {
      System.out.println("L1 SELECTED");
      aCoralSubsystem.moveArtCoral(0.2);
    }
    // Move the articulator
    if (l2)
    {
      System.out.println("L2 SELECTED");
      aCoralSubsystem.moveArtCoral(0.4);
    }
    //  Move the articulator
    if (l3)
    {
      System.out.println("L3 SELECTED");
      aCoralSubsystem.moveArtCoral(0.4);
    }
    //  Move the articulator
    if (l4)
    {
      System.out.println("L4 SELECTED");
      aCoralSubsystem.moveArtCoral(0.6);
      
    }


  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    // Stop the articulator
    aCoralSubsystem.stopArtCoral();
    // Print the end of the command
    System.out.println("Articulador 1 Command Done");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return false;
  }

}

