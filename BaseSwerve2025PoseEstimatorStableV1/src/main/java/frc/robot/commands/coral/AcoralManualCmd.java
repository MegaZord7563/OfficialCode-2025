// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.coral;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Utils.OperatorHub;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;


/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AcoralManualCmd extends Command {

  private final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;
  private final OperatorHub operatorHub ;

  private boolean finish = false;
  private boolean travaC = false; 
  private double encoderPositionC;
  
  /** Creates a new CoralManualCmd. */
  public AcoralManualCmd(OperatorHub operatorHub) {

    this.operatorHub=operatorHub;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
   // System.out.println("EntrouComandoManualCoral");
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      //Coral Articulator
      //Up

      if(operatorHub.getSelectOne())
      { 

        if(operatorHub.getBlackAxisUp(0.75))
        {
          aCoralSubsystem.setVelocity(0.1);
          encoderPositionC = aCoralSubsystem.getPosition();
          travaC =true;
        }
        //Down
        else if(operatorHub.getBlackAxisDown(-0.75))
        {
          aCoralSubsystem.setVelocity(-0.1);
          encoderPositionC = aCoralSubsystem.getPosition();
          travaC =true;
        }
        //Center Position
        else if(operatorHub.getBlackAxisUpDownHome()&&!aCoralSubsystem.isHomed().get()&&travaC)
        {
          aCoralSubsystem.runMotor(encoderPositionC);
          travaC=false;
        }

        else if (aCoralSubsystem.isHomed().get())
        {
          finish = true;
        }

      }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  { 

   
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish;
  }
}
