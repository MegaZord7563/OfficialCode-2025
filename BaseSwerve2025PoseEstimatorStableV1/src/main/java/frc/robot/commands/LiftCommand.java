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

    LiftSubsystem liftSubsystem = new LiftSubsystem();

  /** Creates a new AngleCmd. */
  public LiftCommand(LiftSubsystem liftSubsystem) {
    this.liftSubsystem = liftSubsystem;


  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
    //liftSubsystem.liftMotorRightEncoder.getPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (RobotContainer.driverJoystick.getHID().getAButtonPressed()) {
      getNowPosition();
  };

  }

  public void getNowPosition(){
    liftSubsystem.liftMotorRightEncoder.getPosition();
  }
    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    return finish ;
  }

}

