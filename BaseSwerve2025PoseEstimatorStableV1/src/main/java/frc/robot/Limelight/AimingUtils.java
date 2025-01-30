
package frc.robot.Limelight;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

public class AimingUtils 
{

    // Constants for aiming and ranging to the target
    private static final double kP = 0.1; // Proportional control constant
    private static final double kMaxAngularSpeed = 1.0; // Maximum angular speed in radians per second
    private static final double kMaxSpeed = 1.0; // Maximum forward speed in meters per second

    // Method to aim at the target using Limelight's tx value
    public static double aimAtTarget(String limelightName) 
    {
        // Get the horizontal offset from crosshair to target (tx)
        double targetingAngularVelocity = LimelightHelpers.getTX(limelightName) * kP;

        // Convert to radians per second for our drive method
        targetingAngularVelocity *= kMaxAngularSpeed;

        // Invert since tx is positive when the target is to the right of the crosshair
        targetingAngularVelocity *= -1.0;

        return targetingAngularVelocity;
    }

    // Method to range to the target using Limelight's ty value
    public static double rangeToTarget(String limelightName) 
    {
        // Get the vertical offset from crosshair to target (ty)
        double targetingForwardSpeed = LimelightHelpers.getTY(limelightName) * kP;

        // Convert to meters per second for our drive method
        targetingForwardSpeed *= kMaxSpeed;

        // Invert since ty is positive when the target is above the crosshair
        targetingForwardSpeed *= -1.0;

        return targetingForwardSpeed;
    }

    // Method to estimate the distance to the target using Limelight's ty value
    public static double estimateDistanceToTarget() 
    {
        // Constants for distance calculation
        double targetHeight = 2.5; // Height of the target in meters
        double limelightHeight = 1.0; // Height of the Limelight in meters
        double limelightAngle = 30.0; // Mounting angle of the Limelight in degrees

        // Get the vertical offset from crosshair to target (ty)
        double ty = LimelightHelpers.getTY("limelight-front");

        // Calculate the angle to the target in radians
        double angleToTarget = Math.toRadians(limelightAngle + ty);

        // Calculate the distance to the target using trigonometry
        double distanceToTarget = (targetHeight - limelightHeight) / Math.tan(angleToTarget);

        return distanceToTarget;
    }

    // Method to estimate the distance to the target using the robot's current pose
    public static double estimateDistanceToTargetFromPose(SwerveSubsystem swerveSubsystem, Translation2d targetPosition) 
    {
        // Get the current robot pose from the pose estimator
        Pose2d currentPose = swerveSubsystem.getPoseEstimator();

        // Get the robot's current position
        Translation2d robotPosition = currentPose.getTranslation();

        // Calculate the distance to the target
        double distanceToTarget = robotPosition.getDistance(targetPosition);

        return distanceToTarget;
    }
}