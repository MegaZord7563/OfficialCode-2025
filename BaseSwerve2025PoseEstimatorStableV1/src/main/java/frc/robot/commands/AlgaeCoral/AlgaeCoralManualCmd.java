// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AlgaeCoral;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Utils.OperatorHub;
import frc.robot.subsystems.Algae.AlgaSubsystem;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.CoralSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AlgaeCoralManualCmd extends Command {

    private final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;
    private final CoralSubsystem coralSubsystem = RobotContainer.coralSubsystem;
    private final AlgaSubsystem algaSubsystem = RobotContainer.algaSubsystem;
    private final ArticuladorAlgaeSubsystem aAlgaeSubsystem = RobotContainer.articuladorAlgaeSubsystem;

    private final OperatorHub operatorHub = RobotContainer.operatorHub;

    boolean finish = false;

  /** Creates a new AlgaeCoralManualCmd. */
  public AlgaeCoralManualCmd() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aCoralSubsystem);
    addRequirements(coralSubsystem);
    addRequirements(algaSubsystem);
    addRequirements(aAlgaeSubsystem);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("AlgaeCoralManual Command Initialized");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (operatorHub.getSelectOne()){

      if(operatorHub.getOne()){ coralSubsystem.runMotor(20);}

      if(operatorHub.getTwo()){ coralSubsystem.runMotor(-20);}

      if( operatorHub.getBlackAxisUp(0.7)){ aCoralSubsystem.setVelocity(0.4);}

      if( operatorHub.getBlackAxisDown(-0.7)){ aCoralSubsystem.setVelocity(-0.4);}

      else{ aCoralSubsystem.runMotor(aCoralSubsystem.getPosition());
        coralSubsystem.resetEncoder();
        finish = true;
      }
    }
    else{
            
      if(operatorHub.getOne()){ algaSubsystem.setVelocity(0.5);}
      
      if(operatorHub.getTwo()){ algaSubsystem.setVelocity(-0.5); }

      if( operatorHub.getBlackAxisUp(0.7)){ aAlgaeSubsystem.setVelocity(0.4);}

      if( operatorHub.getBlackAxisDown(-0.7)){ aAlgaeSubsystem.setVelocity(-0.4);}

      else{aAlgaeSubsystem.runMotor(aAlgaeSubsystem.getPosition());
        finish = true;
      }
      
          }
        }
      

      
        // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish;
  }
}
