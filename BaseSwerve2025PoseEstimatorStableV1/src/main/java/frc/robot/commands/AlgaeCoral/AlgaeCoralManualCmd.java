// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AlgaeCoral;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Utils.OperatorHub;
import frc.robot.subsystems.LedControl;
import frc.robot.subsystems.Algae.AlgaSubsystem;
import frc.robot.subsystems.Algae.ArticuladorAlgaeSubsystem;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Coral.CoralSubsystem;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AlgaeCoralManualCmd extends Command 
{

    private final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;
    private final CoralSubsystem coralSubsystem = RobotContainer.coralSubsystem;
    private final AlgaSubsystem algaSubsystem = RobotContainer.algaSubsystem;
    private final ArticuladorAlgaeSubsystem aAlgaeSubsystem = RobotContainer.articuladorAlgaeSubsystem;
    private final SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
    private final LedControl ledControl = RobotContainer.ledControl;
    

    private final OperatorHub operatorHub ;

    boolean finish = false;
    boolean trava = false;
    boolean travaC = false;

    boolean leftScore = false;
    boolean rigthScore = false;

    private double encoderPosition;

    private double encoderPositionC;

  /** Creates a new AlgaeCoralManualCmd. */
  public AlgaeCoralManualCmd(OperatorHub operatorHub) 
  {
    this.operatorHub=operatorHub;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aCoralSubsystem,coralSubsystem,algaSubsystem,aAlgaeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //System.out.println("AlgaeCoralManual Command Initialized");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {


    //Selective Key Coral/Algae
    if (operatorHub.getSelectOne()){

      //Coral Shooter
      if(operatorHub.getOne()){ coralSubsystem.setVelocity(1);}
      else if(operatorHub.getTwo()){ coralSubsystem.setVelocity(-1);}

      else if(!operatorHub.getTwo()){ coralSubsystem.setVelocity(0);}


      //Coral Articulator
      //Up
      if(operatorHub.getBlackAxisUp(0.75))
      {
        aCoralSubsystem.setVelocity(0.1);
        encoderPositionC = aCoralSubsystem.getPosition();
        System.out.println("BlackAxisUp");
        travaC =true;
      }
      //Down
      else if(operatorHub.getBlackAxisDown(-0.75))
      {
        aCoralSubsystem.setVelocity(-0.1);
        encoderPositionC = aCoralSubsystem.getPosition();
        System.out.println("BlackAxisDown");
        travaC =true;
      }
      //Center Position
      else if(operatorHub.getBlackAxisUpDownHome()&&!aCoralSubsystem.isHomed().get()&&travaC)
      {
        encoderPositionC = aCoralSubsystem.getPosition();
        aCoralSubsystem.runMotor(encoderPositionC);
        System.out.println("BackAxisHome");
        travaC=false;
      }
      else if (aCoralSubsystem.isHomed().get())
      {
        finish = true;
      }


    }
    else{

      //Algae Shooter
      if(operatorHub.getOne()){  
      algaSubsystem.startAlga(9.5);
      ledControl.redLed();
      }
      

      else if(operatorHub.getTwo()){ algaSubsystem.startAlga(-9.5);}

      else if(!operatorHub.getOne() && !operatorHub.getTwo()){algaSubsystem.resetEncoder();}



      //Algae Articulator
      //Up
      if(operatorHub.getBlackAxisUp(0.75))
      {
        aAlgaeSubsystem.setVelocity(0.1);
        encoderPosition = aAlgaeSubsystem.getPosition();
        trava =true;
        System.out.println("BlackAxisUp");
      }
      //Down
      else if(operatorHub.getBlackAxisDown(-0.75))
      {
        aAlgaeSubsystem.setVelocity(-0.1);
        encoderPosition = aAlgaeSubsystem.getPosition();
        trava =true;
        System.out.println("BlackAxisDown");
      }
      //Center Position
      else if(operatorHub.getBlackAxisUpDownHome()&&!aAlgaeSubsystem.isHomed().get()&&trava)
      {
        System.out.println("BlackAxisHome");
        encoderPosition = aAlgaeSubsystem.getPosition();
        aAlgaeSubsystem.runMotor(encoderPosition);
        trava=false;
      }
      else if (aAlgaeSubsystem.isHomed().get())
      {
        //finish = true;
      }
      }

      SmartDashboard.putBoolean("LeftScore", leftScore);
      SmartDashboard.putBoolean("RightScore", rigthScore);
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
