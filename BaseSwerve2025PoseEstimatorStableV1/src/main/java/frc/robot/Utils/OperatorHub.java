package frc.robot.Utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import frc.robot.Constants.OIConstants.MesinhaJoy1Constants;
import frc.robot.Constants.OIConstants.MesinhaJoy2Constants;

public class OperatorHub {
    private final Joystick mesinha1;
    private final Joystick mesinha2;

/**************************************************************
 *                         Mesinha1                           *
 **************************************************************/
    //Botões mesinha1
    public int kPrepareToTrap = MesinhaJoy1Constants.kThree;
    public int kHome = MesinhaJoy1Constants.kFive;
    public int kTagAmp = MesinhaJoy1Constants.kSix;
    public int kTagSource = MesinhaJoy1Constants.kSeven;
    public int kPositionTrap = MesinhaJoy1Constants.kEight;
    public int kShooterForward = MesinhaJoy1Constants.kTen;
    public int kShooterReversed = MesinhaJoy1Constants.kEleven;
    public int kIntake = MesinhaJoy1Constants.kThirteen;
    public int kIntakeOutTake = MesinhaJoy1Constants.kFourteen;

    //Axis mesinha1
    public int kTiltAxis = MesinhaJoy1Constants.kBlackAxisUp;

/**************************************************************
 *                         Mesinha2                           *
 **************************************************************/
    //Botões mesinha2
    public int kOutTakeForward = MesinhaJoy2Constants.kTwo;
    public int kOutTakeReversed = MesinhaJoy2Constants.kOne;
    public int kClimb = MesinhaJoy2Constants.kFour;
    public int kTagSpeaker = MesinhaJoy2Constants.kNine;
    public int kShooterGroup = MesinhaJoy2Constants.kTwelve;

    //Axix mesinha2
    public int kClimberAxis = MesinhaJoy2Constants.kRedAxisUp;
    public int kTrapAxis = MesinhaJoy2Constants.kRedAxisSides;

    //Chaves mesinha2
    public int kSourcePosition = MesinhaJoy2Constants.kKey1;
    public int kStageLeft = MesinhaJoy2Constants.kKey2Left;
    public int kStageRight = MesinhaJoy2Constants.kKey2Right;
    public int kColorAlliance = MesinhaJoy2Constants.kKey3;



    public OperatorHub(Joystick mesinha1, Joystick mesinha2){
        this.mesinha1 = mesinha1;
        this.mesinha2 = mesinha2;
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    // Mesinha1
    //Mesinha1
    public double getTiltManual(){
        return mesinha1.getRawAxis(kTiltAxis);
    }
    
    //Mesinha1
    public boolean tiltGreaterThan(double percent){
        return mesinha1.getRawAxis(kTiltAxis) > percent;
    }

    //Mesinha1
    public boolean tiltLessThan(double percent){
        return mesinha1.getRawAxis(kTiltAxis) < percent;
    }

    //Mesinha1
    public boolean getPrepareToTrap(){
        return mesinha1.getRawButton(kPrepareToTrap);
    }

    //Mesinha1
    public boolean getHome(){
        return mesinha1.getRawButton(kHome);
    }

    //Mesinha1
    public boolean getTagAmp(){
        return mesinha1.getRawButton(kTagAmp);
    }

    //Mesinha1
    public boolean getTagSource(){
        return mesinha1.getRawButton(kTagSource);
    }
    
    //Mesinha1
    public boolean getPositionTrap(){
        return mesinha1.getRawButton(kPositionTrap);
    }

    //Mesinha1
    public boolean getShooterForward(){
        return mesinha1.getRawButton(kShooterForward);
    }
        
    //Mesinha1
    public boolean getShooterReversed(){
        return mesinha1.getRawButton(kShooterReversed);
    }

    //Mesinha1
    public boolean getIntake(){
        return mesinha1.getRawButton(kIntake);
    }

    //Mesinha1
    public boolean getIntakeOutTake(){
        return mesinha1.getRawButton(kIntakeOutTake);
    }

    //Mesinha2
    public double getClimberManual(){
        return mesinha2.getRawAxis(kClimberAxis);
    }

    //Mesinha2
    public double getTrapManual(){
        return mesinha2.getRawAxis(kTrapAxis);
    }

    //Mesinha2
    public boolean climberGreaterThan(double percent){
        return mesinha2.getRawAxis(kClimberAxis) > percent;
    }

    //Mesinha2
    public boolean climberLessThan(double percent){
        return mesinha2.getRawAxis(kClimberAxis) < percent;
    }

    //Mesinha2
    public boolean trapGreaterThan(double percent){
        return mesinha2.getRawAxis(kTrapAxis) > percent;
    }

    //Mesinha2
    public boolean trapLessThan(double percent){
        return mesinha2.getRawAxis(kTrapAxis) < percent;
    }

    //Mesinha2
    public boolean getOutTakeReversed(){
        return mesinha2.getRawButton(kOutTakeReversed);
    }

    //Mesinha2
    public boolean getOutTakeForward(){
        return mesinha2.getRawButton(kOutTakeForward);
    }

    //Mesinha2
    public boolean getClimb(){
        return mesinha2.getRawButton(kClimb);
    }

    //Mesinha2
    public boolean getTagSpeaker(){
        return mesinha2.getRawButton(kTagSpeaker);
    }
    
    //Mesinha2
    public boolean getShooterGroup(){
        return mesinha2.getRawButton(kShooterGroup);
    }

    //Mesinha2    
    public boolean getSourcePosition(){
        return mesinha2.getRawButton(kSourcePosition);
    }

    //Mesinha2
    public boolean getStageLeftRodrigo(){
        return (mesinha2.getRawButton(kStageLeft) && !mesinha2.getRawButton(kStageRight));
    }

    //Mesinha2    
    public boolean getStageCenterRodrigo(){
        return (!mesinha2.getRawButton(kStageLeft) && !mesinha2.getRawButton(kStageRight));
    }

    //Mesinha2
    public boolean getStageRightRodrigo(){
        return (!mesinha2.getRawButton(kStageLeft) && mesinha2.getRawButton(kStageRight));
    }

    //Mesinha2    
    public boolean getColorAlliance(){
        return mesinha2.getRawButton(kColorAlliance);
    }

}
