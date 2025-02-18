// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Utils.TouchScreenInterface.VirtualButton;

/** Add your docs here. */
public class SelectScore 
{

    public static Pose2d poseToScore(TouchScreenInterface touch){
        Pose2d poseToScore;
        var alliance2 = DriverStation.getAlliance();
        if (alliance2.get() == DriverStation.Alliance.Red) 
        {
            // Red Alliance Values
            if(touch.getButtonValue(VirtualButton.kA)){
                poseToScore = new Pose2d(13.726,3.861,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kB)){
                poseToScore = new Pose2d(13.726,4.190,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kC)){
                poseToScore = new Pose2d(13.556,4.603,Rotation2d.fromDegrees(60));
            }
            else if (touch.getButtonValue(VirtualButton.kD)){
                poseToScore = new Pose2d(13.392,4.887,Rotation2d.fromDegrees(60));
            }
            else if (touch.getButtonValue(VirtualButton.kE)){
                poseToScore = new Pose2d(12.725,4.887,Rotation2d.fromDegrees(120));
            }
            else if (touch.getButtonValue(VirtualButton.kF)){
                poseToScore = new Pose2d(12.561,4.603,Rotation2d.fromDegrees(120));
            }
            else if (touch.getButtonValue(VirtualButton.kG)){
                poseToScore = new Pose2d(12.062,4.190,Rotation2d.fromDegrees(180));
            }
            else if (touch.getButtonValue(VirtualButton.kH)){
                poseToScore = new Pose2d(12.062,3.861,Rotation2d.fromDegrees(180));
            }
            else if (touch.getButtonValue(VirtualButton.kI)){
                poseToScore = new Pose2d(12.561,3.448,Rotation2d.fromDegrees(240));
            }
            else if (touch.getButtonValue(VirtualButton.kJ)){
                poseToScore = new Pose2d(12.725,3.163,Rotation2d.fromDegrees(240));
            }
            else if (touch.getButtonValue(VirtualButton.kK)){
                poseToScore = new Pose2d(13.392,3.163,Rotation2d.fromDegrees(300));
            }
            else if (touch.getButtonValue(VirtualButton.kL)){
                poseToScore = new Pose2d(13.556,3.448,Rotation2d.fromDegrees(300));
            }
            else{
                poseToScore = new Pose2d();
            }
        }else{
            // Blue Alliance Values

            if(touch.getButtonValue(VirtualButton.kA)){
                poseToScore = new Pose2d(3.657,4.190,Rotation2d.fromDegrees(180));
            }
            else if (touch.getButtonValue(VirtualButton.kB)){
                poseToScore = new Pose2d(3.657,3.861,Rotation2d.fromDegrees(180));
            }
            else if (touch.getButtonValue(VirtualButton.kC)){
                poseToScore = new Pose2d(3.991,3.448,Rotation2d.fromDegrees(240));
            }
            else if (touch.getButtonValue(VirtualButton.kD)){
                poseToScore = new Pose2d(4.156,3.163,Rotation2d.fromDegrees(240));
            }
            else if (touch.getButtonValue(VirtualButton.kE)){
                poseToScore = new Pose2d(4.822,3.1639,Rotation2d.fromDegrees(300));
            }
            else if (touch.getButtonValue(VirtualButton.kF)){
                poseToScore = new Pose2d(4.986,3.448,Rotation2d.fromDegrees(300));
            }
            else if (touch.getButtonValue(VirtualButton.kG)){
                poseToScore = new Pose2d(6.00,4.60,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kH)){
                poseToScore = new Pose2d(5.95,3.85,Rotation2d.fromDegrees(180));
            }
            else if (touch.getButtonValue(VirtualButton.kI)){
                poseToScore = new Pose2d(4.986,4.603,Rotation2d.fromDegrees(60));
            }
            else if (touch.getButtonValue(VirtualButton.kJ)){
                poseToScore = new Pose2d(4.822,4.887,Rotation2d.fromDegrees(60));
            }
            else if (touch.getButtonValue(VirtualButton.kK)){
                poseToScore = new Pose2d(4.156,4.887,Rotation2d.fromDegrees(120));
            }
            else if (touch.getButtonValue(VirtualButton.kL)){
                poseToScore = new Pose2d(3.991,4.603,Rotation2d.fromDegrees(120));
            }
            else{
                poseToScore = new Pose2d();
            }
        }

        return poseToScore;
    }
}
