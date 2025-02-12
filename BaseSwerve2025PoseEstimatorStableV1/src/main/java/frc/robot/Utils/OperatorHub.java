package frc.robot.Utils;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants.OIConstants.MesinhaJoy1Constants;
import frc.robot.Constants.OIConstants.MesinhaJoy2Constants;

public class OperatorHub {
    private final Joystick mesinha1;
    private final Joystick mesinha2;

/**************************************************************
 *                         Mesinha1                           *
 **************************************************************/
    //Botões mesinha1
    public int kAlgeaOrCoral = MesinhaJoy1Constants.kThree;

    //Algae
    public int kAlgaeUp = MesinhaJoy1Constants.kSix;
    public int kAlgaeDown = MesinhaJoy1Constants.kSeven;
    public int kPickAlgae = MesinhaJoy1Constants.kEight;
    public int kLeaveAlgae = MesinhaJoy1Constants.kTen;
    public int kTiltAlgae = MesinhaJoy1Constants.kBlackAxisUp;

    //Escalator
    public int kPrepareEscalate = MesinhaJoy1Constants.kFive;

    //Coral
    public int kCoralShoot = MesinhaJoy1Constants.kEleven;


/**************************************************************
 *                         Mesinha2                           *
 **************************************************************/
    //Botões mesinha2

    //Positions
    public int kStationSide = MesinhaJoy2Constants.kFour;
    public int kStationPosition = MesinhaJoy2Constants.kNine;

    //Coral
    public int kPickCoral = MesinhaJoy2Constants.kTwo;
    public int kCoralAxis = MesinhaJoy2Constants.kRedAxisSides;

    //Ramp
    public int kRampStart = MesinhaJoy2Constants.kOne;

    //Home
    public int kHome = MesinhaJoy2Constants.kTwelve;

    //Escalador
    public int kElevatorAxis = MesinhaJoy2Constants.kRedAxisUp;




    public OperatorHub(Joystick mesinha1, Joystick mesinha2){
        this.mesinha1 = mesinha1;
        this.mesinha2 = mesinha2;
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    
    //Mesinha1
    public boolean getTiltAlgae(double percent){
        return mesinha1.getRawAxis(kTiltAlgae) > percent;
    }

    //Mesinha1
    public boolean getElevatorAxis(double percent){
        return mesinha1.getRawAxis(kElevatorAxis) < percent;
    }

    //Mesinha1
    public boolean getAlgaeUp(){
        return mesinha1.getRawButton(kAlgaeUp);
    }

    //Mesinha1
    public boolean getAlgaeDown(){
        return mesinha1.getRawButton(kAlgaeDown);
    }

    //Mesinha1
    public boolean getPickAlgae(){
        return mesinha1.getRawButton(kPickAlgae);
    }

    //Mesinha1
    public boolean getLeaveAlgae(){
        return mesinha1.getRawButton(kLeaveAlgae);
    }
    
    //Mesinha1
    public boolean getPrepareEscalate(){
        return mesinha1.getRawButton(kPrepareEscalate);
    }

    //Mesinha1
    public boolean getkCoralShoot(){
        return mesinha1.getRawButton(kCoralShoot);
    }
        
    //Mesinha1
    public boolean getStationSide(){
        return mesinha1.getRawButton(kStationSide);
    }

    //Mesinha1
    public boolean getStationPosition(){
        return mesinha1.getRawButton(kStationPosition);
    }

    //Mesinha1
    public boolean getPickCoral(){
        return mesinha1.getRawButton(kPickCoral);
    }

    //Mesinha2
    public double getRampStart(){
        return mesinha2.getRawAxis(kRampStart);
    }

    //Mesinha2
    public double getHome(){
        return mesinha2.getRawAxis(kHome);
    }

    //Mesinha2
    public double getkCoralAxis(){
        return mesinha2.getRawAxis(kCoralAxis);
    }


}
