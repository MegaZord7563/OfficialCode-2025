// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//CTRE Imports
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.ClosedLoopSlot;
//REV Imports
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;



/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */

 //Constants class
 public final class Constants 
 {

 
  //Subsystem Constants
  public static final class SubsystemsConstants
  {
    //Lift Constants - SparkMax
    public static final class LiftConstants
    {
      //Lift
      public static final int kLiftLeftMotorId = 22;
      public static final int kLiftRightMotorId = 23;

      //Lift SparkMax
      public static final IdleMode kLiftIdleMode = IdleMode.kBrake;
      public static final int kLiftCurrentLimit = 40;
      public static final ClosedLoopSlot pidSparkSlot = ClosedLoopSlot.kSlot0;
      public static final double kLiftEncoderPositionPIDMinInput =0 ;
      public static final double kLiftEncoderPositionPIDMaxInput= 2 * Math.PI;

      //Lift Encoder
      public static final double KGearRatio = 1.0/20.0; // 
      public static final double kWheelDiameter = 0.013; //meters
      public static final double kLiftPositionConversionFactor = Math.PI* KGearRatio * kWheelDiameter; //rotation to Meters
      public static final double kLiftVelocityConversionFactor = KGearRatio* (2.0 * Math.PI); //rotation to Rad/s

      //Lift PID Up
      public static final double kPLiftUp = 0.5;//ANTERIOR 0,25 ; 0.2142875; 0.175; 0.125; 0.23
      public static final double kILiftUp = 0;
      public static final double kDLiftUp = 0;
      public static final double kLiftUpFF = 0; // = input = 0 to position control and 1.0/473.0 to velocity control
      public static final double kLiftMinOutput = -1;
      public static final double kLiftMaxOutput = 1;

      //Lift PID Down
      public static final double kPLiftDown = 1.3;//ANTERIOR 0,25 ; 0.2142875; 0.175; 0.125; 0.23
      public static final double kILiftDown = 0;
      public static final double kDLiftDown = 0.7;
      public static final double kLiftDownFF = 0; // = input = 0 to position control and 1.0/473.0 to velocity control
      
    } 


    //Algae Constants - SparkMax
    public static final class AlgaeConstants
    {
      //Algae
      public static final int kAlgaeMotorId = 22;
      

      //Algae SparkMax
      public static final IdleMode kAlgaeIdleMode = IdleMode.kBrake;
      public static final int kAlgaeCurrentLimit = 40;
      public static final ClosedLoopSlot pidSparkSlot = ClosedLoopSlot.kSlot0;
      public static final double kAlgaeEncoderPositionPIDMinInput =0 ;
      public static final double kAlgaeEncoderPositionPIDMaxInput= 2 * Math.PI;

      //Algae Encoder
      public static final double KGearRatio = 1.0/20.0; // 
      public static final double kWheelDiameter = 0.013; //meters
      public static final double kAlgaePositionConversionFactor = Math.PI* KGearRatio * kWheelDiameter; //rotation to Meters
      public static final double kAlgaeVelocityConversionFactor = KGearRatio* (2.0 * Math.PI); //rotation to Rad/s

      //Algae PID Up
      public static final double kPAlgaeUp = 0.5;//ANTERIOR 0,25 ; 0.2142875; 0.175; 0.125; 0.23
      public static final double kIAlgaeUp = 0;
      public static final double kDAlgaeUp = 0;
      public static final double kAlgaeUpFF = 0; // = input = 0 to position control and 1.0/473.0 to velocity control
      public static final double kAlgaeMinOutput = -1;
      public static final double kAlgaeMaxOutput = 1;

      //Algae PID Down
      public static final double kPAlgaeDown = 1.3;//ANTERIOR 0,25 ; 0.2142875; 0.175; 0.125; 0.23
      public static final double kIAlgaeDown = 0;
      public static final double kDAlgaeDown = 0.7;
      public static final double kAlgaeDownFF = 0; // = input = 0 to position control and 1.0/473.0 to velocity control
      
    } 

    //Coral Constants - SparkMax
    public static final class CoralConstants
    {
      //Coral
      public static final int kCoralMotorId = 22;
      

      //Coral SparkMax
      public static final IdleMode kCoralIdleMode = IdleMode.kBrake;
      public static final int kCoralCurrentLimit = 40;
      public static final ClosedLoopSlot pidSparkSlot = ClosedLoopSlot.kSlot0;
      public static final double kCoralEncoderPositionPIDMinInput =0 ;
      public static final double kCoralEncoderPositionPIDMaxInput= 2 * Math.PI;

      //Coral Encoder
      public static final double KGearRatio = 1.0/20.0; // 
      public static final double kWheelDiameter = 0.013; //meters
      public static final double kCoralPositionConversionFactor = Math.PI* KGearRatio * kWheelDiameter; //rotation to Meters
      public static final double kCoralVelocityConversionFactor = KGearRatio* (2.0 * Math.PI); //rotation to Rad/s

      //Coral PID Up
      public static final double kPCoralUp = 0.5;//ANTERIOR 0,25 ; 0.2142875; 0.175; 0.125; 0.23
      public static final double kICoralUp = 0;
      public static final double kDCoralUp = 0;
      public static final double kCoralUpFF = 0; // = input = 0 to position control and 1.0/473.0 to velocity control
      public static final double kCoralMinOutput = -1;
      public static final double kCoralMaxOutput = 1;

      //Coral PID Down
      public static final double kPCoralDown = 1.3;//ANTERIOR 0,25 ; 0.2142875; 0.175; 0.125; 0.23
      public static final double kICoralDown = 0;
      public static final double kDCoralDown = 0.7;
      public static final double kCoralDownFF = 0; // = input = 0 to position control and 1.0/473.0 to velocity control
      
    } 


    

    //Escalador Constants - TalonFX
    public static final class EscaladorConstants
    {
      //Escalador
      public static final int kMotor1Id = 25;
      public static final int kMotor2Id = 19;
      public static final NeutralModeValue kNeutralMode = NeutralModeValue.Brake; // TalonFx

      
      public static final double KS = 0.1;//<-MNL 11/11/2024 0.1; //0.32;  // Add 0.1 V output to overcome static friction
      public static final double KV = 0.12; //1.51; // A velocity target of 1 rps results in 0.12 V output
      public static final double KA = 0.11;  //0.27;

      //TO DO: This must be tuned to specific robot
      //PID slot 0
      public static final double kP = 1.0;//<--MNL11/11/2024 0.0665;//ANTERIOR 0,0665 ; 0.05872615 // kP = 0.11 An error of 1 rps results in 0.11 V output
      public static final double kI =0.0;
      public static final double kD=0.0;
      
      public static final double kFF = 1.0/6000.0;
      public static final double kMinOutput= -1;
      public static final double kMaxOutput = 1;

      //PID slot 1
      public static final double kP1 = 2.0;
      public static final double kI1 =0.0;
      public static final double kD1=0.0;


      /* Current Limiting */
     //TalonFX
      public static final int kCurrentLimit = 70; //supply current
      public static final int kCurrentThreshold = 120; //stator current
      public static final double kCurrentThresholdTime = 0.1;
      public static final boolean kEnableCurrentLimit = true;
      public static final double kClosedLoopRamp=0.25;
      public static boolean kInverted = false;

    }
  } 


  //OI Constants
  public static final class OIConstants 
  {
      //Joystick Driver
      public static final class JoystickDriverConstants 
      {
        //Joystick Driver
        public static final int kDriverControllerPort = 0;
        
        public static final int kDriverYAxis = 1;
        public static final int kDriverXAxis = 0;
        //public static final int kDriverRotAxis = 4;
        public static final int kDriverFieldOrientedButtonIdx = 1;
        
        public static final double kDeadband = 0.1;// 0.05 anterior

        //public static final int kRumbleOn = 1;
        //public static final int kRumbleOff = 0;
      
      }

      

      
      
      public static final double kDeadband =0.1;
}

  
  /*******************************************************************
   * @param https://docs.revrobotics.com/brushless/neo/v1.1/neo-v1
   * 
   *****************************************************************/
  

  public static final class NeoMotorConstants 
  {
    public static final double kFreeSpeedRpm = 5676;
  }

  /***********************************************************************************************************************************
   * @param https://docs.wcproducts.com/kraken-x60/kraken-x60-motor/overview-and-features/motor-performance
   * 
   * @param https://store.ctr-electronics.com/announcing-kraken-x60/?srsltid=AfmBOorh3sPSXQ-WmuWYeJlxrIkATC1wRVPc0V65woNtynzQ1Sil1Ueh
   *
   *************************************************************************************************************************************/
  public static final class KrakenMotorConstants 
  {
    public static final double kFreeSpeedRpm = 6000;
  }

  
}
