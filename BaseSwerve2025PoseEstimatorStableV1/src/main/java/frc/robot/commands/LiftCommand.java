package frc.robot.commands;

//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.subsystems.LiftSubsystem;




public class LiftCommand extends Command
{
     // Instantiate the object
     boolean finish =false;

    private final LiftSubsystem liftSubsystem;

  /** Creates a new AngleCmd. */
  public LiftCommand() {
    liftSubsystem = RobotContainer.liftSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Lift Command Initialized");
    liftSubsystem.resetLiftEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    liftSubsystem.moveLift(100, false);
  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    liftSubsystem.stopLift();
    System.out.println("Lift Command Finished");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return RobotContainer.driverJoystick.getHID().getBButtonReleased();
  }

}

