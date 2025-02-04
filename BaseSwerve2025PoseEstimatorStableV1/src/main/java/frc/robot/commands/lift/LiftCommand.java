package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.subsystems.lift.LiftSubsystem;




public class LiftCommand extends Command
{
     // Instantiate the object
    private boolean finish =false;
    private boolean l1 = false;
    private boolean l2 = false;
    private boolean l3 = false;
    private boolean l4 = false;


    private final LiftSubsystem liftSubsystem = RobotContainer.liftSubsystem;

  /** Creates a new AngleCmd. */
  public LiftCommand() 
  {
    addRequirements(liftSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    System.out.println("Lift Command Initialized");
    finish =false;
    l1=false;
    l2=false;
    l3=false;
    l4=false;
    liftSubsystem.resetLiftEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    l1 = SmartDashboard.getBoolean("L1 LEVEL", false);
    l2 = SmartDashboard.getBoolean("L2 LEVEL", false);
    l3 = SmartDashboard.getBoolean("L3 LEVEL", false);
    l4 = SmartDashboard.getBoolean("L4 LEVEL", false);

    if (l1)
    {
      System.out.println("L1 SELECTED");
      liftSubsystem.moveLift(0.5, false);
    }
    if (l2)
    {
      System.out.println("L2 SELECTED");
      liftSubsystem.moveLift(0.8, false);
    }
    if (l3)
    {
      System.out.println("L3 SELECTED");
      liftSubsystem.moveLift(1.0, false);
    }
    if (l4)
    {
      System.out.println("L4 SELECTED");
      liftSubsystem.moveLift(1.2, false);
    }


    //liftSubsystem.moveLift(300, false);
  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    liftSubsystem.stopLift();
    System.out.println("Lift Command Finished");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return false;
  }

}

