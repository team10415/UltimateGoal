package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.team10415.ftc_recorder.Controller;
import com.team10415.ftc_recorder.TeleOpRunner;

import org.jetbrains.annotations.NotNull;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends TeleOpRunner {
    Controller controller = new TeleOpController2();

    @NotNull
    @Override
    public Controller getController() {
        return controller;
    }
}
