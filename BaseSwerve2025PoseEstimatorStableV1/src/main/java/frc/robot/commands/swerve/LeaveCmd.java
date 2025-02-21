// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
// WPI imports
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class LeaveCmd extends Command 
{
  // Instantiate the object
  private final static SwerveSubsystem swerveDrive = RobotContainer.swerveDrive;
    

  

  // Variables
  //private final double kX, kY, kRot; // Replace with the correct class ID for the game piece
 private static boolean getPose = false;  

  //Max speed variables
  //private final double kMaxSpeed2Drive = 0.15;                   // Simple speed limit so we don't drive too fast
  

  //PID offset variables
                   // how hard to turn toward the target
  private final double kPdrive = 1.0; //0.26 
  
  //PID controller
  private PIDController pidDrive =  new PIDController(kPdrive, 0, 0);
  private double initialPosition = 0;
  
  private final double speed = -0.05; 

  private boolean finish = false;
  


  //double kX, double kY , double kRot
  /** Creates a new AimAndRangeCmd. */
  public LeaveCmd()
  {
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      // Switch to pipeline 1
    
    System.out.println("Leave Command Started!!");
    //pidDrive.setTolerance(0.02);

    
    // Set variables
    getPose = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
  //
   if (getPose) 
   {
    initialPosition = robotPosition().getX();
      
    getPose=false;
    
   }

   //double drive_cmd = pidDrive.calculate(robotPosition().getX(),initialPosition + 0.1);
    
    // Drive the swerve subsystem with the calculated speeds
    //swerveDrive.driveToTarget(() -> drive_cmd, () -> 0.0, () -> 0.0, () -> false);
    swerveDrive.driveToTarget(() -> speed, () -> 0.0, () -> 0.0, () -> false);


    if(robotPosition().getX() > initialPosition + 0.1)
    {
      finish = true;
    }
    //testar metodo

    //swerveDrive.drive(new ChassisSpeeds(drive_cmd,0.0, 0.0));
  }
    
      
  

  //Helper method to calculate robot position

  /**
   * Method to get the robot's current position
   * @return robotPosition
   */
  public Pose2d robotPosition()
  {

    // Get the current robot pose from the pose estimator
    Pose2d currentPose = swerveDrive.getPoseEstimator();

    // Get the robot's current position
    
    return currentPose;
  }
 

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    swerveDrive.stopModules();
    System.out.println("Leave Command Done!!");
    //getPose = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    // Optionally, add a condition to end the command
        // For example, you can check if the robot is close enough to the target
        
        
        return finish;
  }

}
