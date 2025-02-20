// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Algae;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Utils.OperatorHub;
import frc.robot.subsystems.Algae.AlgaSubsystem;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;



public class AlgaeManualCmd extends Command 
{


    private final AlgaSubsystem algaSubsystem = RobotContainer.algaSubsystem;
    private final ArticuladorAlgaeSubsystem aAlgaeSubsystem = RobotContainer.articuladorAlgaeSubsystem;

    private final OperatorHub operatorHub ;

    boolean finish = false;
    boolean trava ;
    boolean travaC = false;

    private double encoderPosition;


  /** Creates a new AlgaeCoralManualCmd. */
  public AlgaeManualCmd(OperatorHub operatorHub) 
  {
    this.operatorHub=operatorHub;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(algaSubsystem,aAlgaeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("AlgaeManual Command Initialized");
    finish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //Selective Key Coral/Algae
    if (!operatorHub.getSelectOne()){
      //Algae Articulator Up
      if(operatorHub.getBlackAxisUp(0.75))
      {
        aAlgaeSubsystem.setVelocity(0.1);
        encoderPosition = aAlgaeSubsystem.getPosition();
        trava =true;
        System.out.println("BlackAxisUp");
      }
      //Algae Articulator Down
      else if(operatorHub.getBlackAxisDown(-0.75))
      {
        aAlgaeSubsystem.setVelocity(-0.1);
        encoderPosition = aAlgaeSubsystem.getPosition();
        trava =true;
        System.out.println("BlackAxisDown");
      }
      //Algae Articulator Center Position
      else if(operatorHub.getBlackAxisUpDownHome()&&!aAlgaeSubsystem.isHomed().get()&&trava)
      {
        System.out.println("BlackAxisHome");
        aAlgaeSubsystem.runMotor(encoderPosition);
        trava=false;
      }
      else if (aAlgaeSubsystem.isHomed().get())
      {
        //finish = true;
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
