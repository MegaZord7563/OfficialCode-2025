// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;




/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

  SparkMax neoIntake = new SparkMax(10, MotorType.kBrushless);

  CommandXboxController driverJoystick = new CommandXboxController(0);

  double speed = 0.7;

  SparkMaxConfig config = new SparkMaxConfig();

      /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    config
    .idleMode(IdleMode.kBrake)
    .smartCurrentLimit(20);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    if (driverJoystick.getHID().getAButton()) {
      neoIntake.set(0.15);
    }    

    else if (driverJoystick.getHID().getBButton()) {
      neoIntake.set(-0.15);
    }   
    else{
      neoIntake.set(0 );
    
    }

    

  }
  

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
