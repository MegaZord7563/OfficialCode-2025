package frc.robot.commands.AutonomousCmd;	


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.Utils.TouchScreenInterface.VirtualButton;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;

public class ArticuladorCoralAutoCmd extends Command
{
     
    // Declare the boolean variables
    private boolean l1 = false;
    private boolean l2 = false;
    private boolean l3 = false;
    private boolean l4 = false;

    private boolean finish = false;
    
    private double encoderPosition = 0;

    private double tolerance = 0.05;//5%

    //setpoint in grades
   
    private double l1Setpoint = 55.0;
    private double l2Setpoint = 50.0;
    private double l3Setpoint = 50.0;
    private double l4Setpoint = 62.0;


    // Create the subsystem
    private final ArticuladorCoralSubsystem aCoralSubsystem = RobotContainer.articuladorCoralSubsystem;

  /** Creates a new AngleCmd. */
  public ArticuladorCoralAutoCmd(boolean l1, boolean l2, boolean l3, boolean l4) 
  {
    this.l1 = l1;
    this.l2 = l2;
    this.l3 = l3;
    this.l4 = l4;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aCoralSubsystem);
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    // Print the initialization of the command
    System.out.println("Articulador 1 Command Initialized");
    // Set the button values to false
    finish =false;

    // Reset the encoder
    //aCoralSubsystem.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

    encoderPosition = aCoralSubsystem.getPosition();
    
    SmartDashboard.getBoolean("L1", l1);
    SmartDashboard.getBoolean("L2", l2);
    SmartDashboard.getBoolean("L3", l3);
    SmartDashboard.getBoolean("L4", l4);


    // Move the articulator
    if (l1)
    {
      System.out.println("L1 SELECTED");
      aCoralSubsystem.runMotor(l1Setpoint);
      if (l1Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l1Setpoint * (1+tolerance)) finish=true;
    }
    // Move the articulator
    if (l2)
    {
      System.out.println("L2 SELECTED");
      aCoralSubsystem.runMotor(l2Setpoint);
      if (l2Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l2Setpoint * (1+tolerance)) finish=true;
    }
    //  Move the articulator
    if (l3)
    {
      System.out.println("L3 SELECTED");
      aCoralSubsystem.runMotor(l3Setpoint);
      if (l3Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l3Setpoint * (1+tolerance)) finish=true;
    }
    //  Move the articulator
    if (l4)
    {
      System.out.println("L4 SELECTED");
      aCoralSubsystem.runMotor(l4Setpoint);
      if (l4Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l4Setpoint * (1+tolerance)) finish=true;
      
    }

    

     if(!l1 && !l2 && !l3 && !l4 )finish=true;
     if(RobotContainer.driverJoystick.start().getAsBoolean())finish=true;


    
  }   

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    // Stop the articulator
    //aCoralSubsystem.stopMotor();
    // Print the end of the command
    System.out.println("Articulador 1 Command Done");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return finish;
  }

}

