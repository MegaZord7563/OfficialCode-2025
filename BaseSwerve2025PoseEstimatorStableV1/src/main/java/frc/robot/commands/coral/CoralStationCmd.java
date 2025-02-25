// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.coral;



import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;

import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.CoralSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class CoralStationCmd extends Command 
{
  /** Creates a new ArtiAlgaeCmd. */

  private final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;
  private final CoralSubsystem coralSubsystem = RobotContainer.coralSubsystem;

  // Declare the double variables
  private double encoderPosition = 0;
  private double setPoint = 70;//em graus
  private double tolerance = 0.05;//5%

  //declare boolean variables
  private boolean finish=false;

  public CoralStationCmd() 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aCoralSubsystem);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    System.out.println("Articulador Coral Command Initialized");
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

    // Get the encoder position
    encoderPosition = aCoralSubsystem.getPosition();

    // Run the motor
    aCoralSubsystem.runMotor(setPoint);

    // Check if the setpoint is within the tolerance
    if (setPoint * (1-tolerance) < encoderPosition && encoderPosition < setPoint * (1+tolerance)&& !aCoralSubsystem.sensorCoral()) finish=true;
  }



  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    System.out.println("Articulador Coral Command Done");
    //segura game piece
    coralSubsystem.runMotor(coralSubsystem.getPosition());
    //close articulador quando peça chega
    aCoralSubsystem.runMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
        return finish;
  }
  
}
