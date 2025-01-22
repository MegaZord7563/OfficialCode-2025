package frc.robot.commands;

import java.util.concurrent.AbstractExecutorService;

import edu.wpi.first.units.mutable.GenericMutableMeasureImpl;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LiftSubsystem;

import frc.robot.subsystems.Swerve.SwerveSubsystem;


public class LiftCommand extends Command{
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
  public void execute() {
    liftSubsystem.startLift(0.5);
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

