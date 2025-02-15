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

    private final OperatorHub operatorHub ;

    boolean finish = false;
    boolean trava = false;
    boolean travaC = false;

    private double encoderPosition;

    private double encoderPositionC;

  /** Creates a new AlgaeCoralManualCmd. */
  public AlgaeCoralManualCmd(OperatorHub operatorHub) {
    this.operatorHub=operatorHub;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aCoralSubsystem,coralSubsystem,algaSubsystem,aAlgaeSubsystem);


  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("AlgaeCoralManual Command Initialized");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    //Selective Key Coral/Algae
    if (operatorHub.getSelectOne()){

      //Coral Shooter
      if(operatorHub.getOne()){ coralSubsystem.setVelocity(1);;}

      else if(operatorHub.getTwo()){ coralSubsystem.setVelocity(-1);}

      else if(!operatorHub.getTwo()){ coralSubsystem.setVelocity(0);}
      else if(!operatorHub.getTwo()){ coralSubsystem.setVelocity(0);}

      //Coral Articulator
      else if(operatorHub.getBlackAxisUp(0.75))
      {
        aCoralSubsystem.setVelocity(0.3);;
        encoderPositionC = aCoralSubsystem.getPosition();
        trava =true;
      }
      else if(operatorHub.getBlackAxisDown(-0.75))
      {
        aCoralSubsystem.setVelocity(-0.3);
        encoderPositionC = aCoralSubsystem.getPosition();
        trava =true;
      }
      else if(operatorHub.getBlackAxisUpDownHome()&&!aCoralSubsystem.isHomed().get())
      {
        encoderPositionC = aCoralSubsystem.getPosition();
        aCoralSubsystem.runMotor(encoderPositionC);
        System.out.println("BackAxisHome");
        trava=false;
      }
      else if (aCoralSubsystem.isHomed().get())
      {
        finish = true;
      }
      else{
        //Prestar atenção
        aCoralSubsystem.setVelocity(0);
      }

    }
    else{
      //Algae Shooter
      if(operatorHub.getOne()){ algaSubsystem.setVelocity(1);}
      
      else if(operatorHub.getTwo()){ algaSubsystem.setVelocity(-1); }

      else if(!operatorHub.getOne()){ algaSubsystem.setVelocity(0);}
      
      else if(!operatorHub.getTwo()){ algaSubsystem.setVelocity(0); }

      //Algae Articulator
      else if(operatorHub.getBlackAxisUp(0.75))
      {
        aAlgaeSubsystem.setVelocity(0.5);;
        encoderPosition = aAlgaeSubsystem.getPosition();
        trava =true;
      }
      else if(operatorHub.getBlackAxisDown(-0.75))
      {
        aAlgaeSubsystem.setVelocity(-0.5);
        encoderPosition = aAlgaeSubsystem.getPosition();
        trava =true;
      }
      else if(operatorHub.getBlackAxisUpDownHome()&&!aAlgaeSubsystem.isHomed().get())
      {
        encoderPosition = aAlgaeSubsystem.getPosition();
        aAlgaeSubsystem.runMotor(encoderPosition);
        trava=false;
        System.out.println("BlackAxisHome");
      }
      else if (aAlgaeSubsystem.isHomed().get())
      {
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
