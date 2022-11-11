package com.g3g4x5x6.remote.console;


import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.g3g4x5x6.AppConfig;
import com.jediterm.pty.PtyProcessTtyConnector;
import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.UIUtil;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class ConsolePane extends JPanel {

    private BorderLayout borderLayout = new BorderLayout();
    private JToolBar toolBar;
    private JProgressBar progressBar;

    public ConsolePane() {
        this.setLayout(borderLayout);
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // 刷新按钮
        FlatButton freshBtn = new FlatButton();
        freshBtn.setButtonType(FlatButton.ButtonType.toolBarButton);
        freshBtn.setToolTipText("重置本地终端");
        freshBtn.setIcon(new FlatSVGIcon("icons/refresh.svg"));
        freshBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    log.debug("刷新本地终端");
                    openTerminal();
                }).start();
            }
        });
        // 终端刷新进度条
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        openTerminal();

        toolBar.add(freshBtn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(progressBar);
        toolBar.add(Box.createHorizontalGlue());
        this.add(toolBar, BorderLayout.NORTH);
        log.info(">>>>>>>> 启动本地终端");
    }

    private void openTerminal() {
        new Thread(() -> {
            progressBar.setVisible(true);
            CmdSettingsProvider cmdSettingsProvider = new CmdSettingsProvider();
            JediTermWidget terminalPanel = new JediTermWidget(cmdSettingsProvider);
            terminalPanel.setTtyConnector(createTtyConnector());
            terminalPanel.start();

            progressBar.setVisible(false);
            this.add(terminalPanel, BorderLayout.CENTER);
        }).start();
    }


    private static @NotNull TtyConnector createTtyConnector() {
        try {
            Map<String, String> envs = System.getenv();
            String[] command;
            if (UIUtil.isWindows) {
                command = new String[]{"powershell.exe"};
                // 分号; 分割变量值  => C:\Windows\system32;C:\Windows;
                String PATH = envs.get("Path") + ";" + AppConfig.getBinPath();
                envs = new HashMap<>(System.getenv());
                envs.put("Path", PATH);
            } else {
                command = new String[]{"/bin/bash", "--login"};
                envs = new HashMap<>(System.getenv());
                envs.put("TERM", "xterm-256color");
            }

            PtyProcess process = new PtyProcessBuilder().setCommand(command).setEnvironment(envs).start();
            return new PtyProcessTtyConnector(process, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
