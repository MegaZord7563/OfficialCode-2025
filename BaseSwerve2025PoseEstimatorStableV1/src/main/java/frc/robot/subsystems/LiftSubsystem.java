package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ModuleConstants;

public class LiftSubsystem extends SubsystemBase {

private final SparkMax liftMotorRight;
private final SparkMax liftMotorLeft;

SparkMaxConfig configRightMotor = new SparkMaxConfig();
SparkMaxConfig configLeftMotor = new SparkMaxConfig();

private final SparkClosedLoopController liftPidController;

private final RelativeEncoder liftMotorRightEncoder;


public LiftSubsystem(){

liftMotorRight = new SparkMax(10, MotorType.kBrushless);
liftMotorLeft = new SparkMax(11, MotorType.kBrushless);

liftMotorRightEncoder = liftMotorRight.getEncoder();  
liftPidController = liftMotorRight.getClosedLoopController(); 

configRightMotor();
configLeftMotor();

}

@Override
public void periodic(){
        
}

public void configRightMotor(){
    configRightMotor.closedLoop.
    feedbackSensor(FeedbackSensor.kPrimaryEncoder);
    
    configRightMotor.idleMode( IdleMode.kBrake)
    .smartCurrentLimit(20);
    
    configRightMotor.encoder
    .positionConversionFactor(ModuleConstants.kTurningEncoderRot2Rad)
    .velocityConversionFactor(ModuleConstants.kTurningEncoderVelocityFactor);
    
    configRightMotor.closedLoop
    .pidf(1,0,0,0);
    
    liftMotorRight.configure(configRightMotor, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
}

public void configLeftMotor(){
    configLeftMotor.closedLoop.
    feedbackSensor(FeedbackSensor.kPrimaryEncoder);
    
    configLeftMotor.idleMode( IdleMode.kBrake)
    .smartCurrentLimit(20);
    
    configLeftMotor.encoder
    .positionConversionFactor(ModuleConstants.kTurningEncoderRot2Rad)
    .velocityConversionFactor(ModuleConstants.kTurningEncoderVelocityFactor);
    
    configLeftMotor.closedLoop
    .pidf(1,0,0,0);
    
    configLeftMotor.follow(liftMotorRight);

    liftMotorLeft.configure(configLeftMotor, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

}
}
