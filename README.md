# 2025 Megazord robot Code
This is the official code from the FRC Megazord #7563 Team here you can browse into commits and Branchs, but remember just create a new file if its necessary

# Rules:
- Do not create copies of the same file, instead try to create a new Branch.
- Just make changes in the Main Branch if the code is already validated.
- If you do a change, you must to commit.
- Never do a Merge if you are not sure of what you are doing

  # Systems:
  Here we're gonna to do a brief review of each Subsystem of the robot

  # Swerve
  The SwerveDrive allow us to move fast and with precision in any position of the field.

  The code its separed in 3 different places
  # SwerveModules

  Here we're gonna to set the output for each wheel separatly, as shown in the pic:
  ![image](https://github.com/user-attachments/assets/3db5946f-9272-4c8a-95ba-dd79039eae32)
  
  # Swerve Subsystem / Command
  In this part of the code we're gonna to mix all the wheel's together and create ChassisSpeeds, and the command will look for the inputs of the the Joystick and convert in outputSpeeds
  ![image](https://github.com/user-attachments/assets/bf6685db-2e2a-4c8e-84aa-dfef68be0d86)

  # Inside the swerve
  - Here we have some controlls to automate our robot, like the Pose estimator , and with it we can implament controlls like PathPlanner For Autonomous and PathFinding
 
  # Pose Estimator
  This class wraps Swerve Drive Odometry to fuse latency-compensated vision measurements with swerve drive encoder distance measurements. It is intended to be a drop-in replacement for SwerveDriveOdometry.

  How to create?

  ## Instantiating:
    *
    *private final SwerveDrivePoseEstimator m_poseEstimator =
    *           new SwerveDrivePoseEstimator(
                    DriveConstants.kDriveKinematics,
                     gyro.getRotation2d(),
                    new SwerveModulePosition[] {
                      frontLeftModule.getPosition(),
                      frontRightModule.getPosition(),
                      backLeftModule.getPosition(),
                      backRightModule.getPosition()
                    },
                    new Pose2d(),
                    VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
                    VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30)));


## UpdatePoseEstimator
Class that Update the Pose

 public void updatePoseEstimator() 
 {
    
    m_poseEstimator.update(
        gyro.getRotation2d(),
        new SwerveModulePosition[]
        {
          frontLeftModule.getPosition(),
          frontRightModule.getPosition(),
          backLeftModule.getPosition(),
          backRightModule.getPosition()
        });
  
  }


We use poseEstimator in other Places but thease two are the most important

## PathPlanner

How to create?

  
