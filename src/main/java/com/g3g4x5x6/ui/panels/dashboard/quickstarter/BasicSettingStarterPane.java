package com.g3g4x5x6.ui.panels.dashboard.quickstarter;


import com.g3g4x5x6.ui.CloseButton;
import com.g3g4x5x6.ui.TabbedTitlePane;
import com.g3g4x5x6.ui.formatter.IpAddressFormatter;
import com.g3g4x5x6.ui.formatter.PortFormatter;
import com.g3g4x5x6.ui.panels.ssh.MyJSchShellTtyConnector;
import com.g3g4x5x6.ui.panels.ssh.SshSettingsProvider;
import com.g3g4x5x6.ui.panels.ssh.SshTabbedPane;
import com.g3g4x5x6.utils.Utils;
import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.ui.JediTermWidget;
import org.apache.log4j.Logger;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.config.hosts.HostConfigEntry;
import org.apache.sshd.client.session.ClientSession;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class BasicSettingStarterPane extends JPanel {
    static final Logger logger = Logger.getLogger(BasicSettingStarterPane.class);

    private FlowLayout flowLayout = new FlowLayout();
    private JTabbedPane mainTabbedPane;

    private JFormattedTextField hostField;
    private JFormattedTextField portField;

    private String host;
    private int port;
    private String username;
    private String password;

    public BasicSettingStarterPane(JTabbedPane mainTabbedPane) {
        flowLayout.setAlignment(FlowLayout.LEFT);
        this.setLayout(flowLayout);

        this.mainTabbedPane = mainTabbedPane;

        createBasicComponent();
    }


    private void createBasicComponent() {
        // TODO host address
        JPanel hostPane = new JPanel();
        JLabel hostLabel = new JLabel("Remote Host*");
        hostField = new JFormattedTextField(new IpAddressFormatter());
        hostField.setColumns(10);
        hostField.setText("192.168.83.137");    // For testing
        hostPane.add(hostLabel);
        hostPane.add(hostField);

        // TODO port
        JPanel portPane = new JPanel();
        JLabel portLabel = new JLabel("Port*");
        portField = new JFormattedTextField(new PortFormatter());
        portField.setColumns(4);
        portField.setText("22");
        portPane.add(portLabel);
        portPane.add(portField);

        // TODO user name
        JPanel userPane = new JPanel();
        JLabel userLabel = new JLabel("Username");
        JFormattedTextField userField = new JFormattedTextField();
        userField.setText("root");
        userField.setColumns(8);
        userPane.add(userLabel);
        userPane.add(userField);

        // TODO password
        JPanel passPane = new JPanel();
        JLabel passLabel = new JLabel("Password");
        JPasswordField passField = new JPasswordField();
        passField.setText("12345678");
        passField.setColumns(8);
        passPane.add(passLabel);
        passPane.add(passField);

        // TODO Save and open session
        JPanel savePane = new JPanel();
        JButton openButton = new JButton("快速连接");
        openButton.setToolTipText("默认自动保存会话");
        JButton testButton = new JButton("测试连接");
        savePane.add(openButton);
        savePane.add(testButton);

        this.add(hostPane);
        this.add(portPane);
        this.add(userPane);
        this.add(passPane);
        this.add(savePane);

        openButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logger.debug("快速连接");

                // TODO 测试连接
                if (testConnection() == 1) {
                    host = hostField.getText();
                    port = Integer.parseInt(portField.getText());
                    username = userField.getText();
                    password = String.valueOf(passField.getPassword());
                    logger.debug(password);

                    String defaultTitle = hostField.getText().equals("") ? "未命名" : "(" + (mainTabbedPane.getTabCount()-1) + ") " + hostField.getText();
                    mainTabbedPane.insertTab(defaultTitle, null,
                            new SshTabbedPane(mainTabbedPane,
                                    createTerminalWidget(),
                                    hostField.getText(),
                                    portField.getText(),
                                    userField.getText(),
                                    String.valueOf(passField.getPassword())),
                            "快速连接",
                            mainTabbedPane.getTabCount()-1);
                    mainTabbedPane.setTabComponentAt(mainTabbedPane.getTabCount()-2, new TabbedTitlePane(defaultTitle, mainTabbedPane, new CloseButton(defaultTitle, mainTabbedPane)));
                    mainTabbedPane.setSelectedIndex(mainTabbedPane.getTabCount()-2);
                } else {
                    Utils.warn("连接失败");
                }
            }
        });

        testButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logger.debug("测试连接");
                switch (testConnection()) {
                    case 0:
                        Utils.warn("连接失败");
                        break;
                    case 1:
                        Utils.info("连接成功");
                        break;
                    case 2:
                        Utils.info("请输入主机地址！！！");
                }
            }
        });
    }



    private @NotNull JediTermWidget createTerminalWidget() {
        JediTermWidget widget = new JediTermWidget(new SshSettingsProvider());
        widget.setTtyConnector(createTtyConnector());
        widget.start();
        return widget;
    }

    // TODO 创建 sFTP channel
    private @NotNull TtyConnector createTtyConnector() {
        try {
            if (username.equals("")) {
                return new MyJSchShellTtyConnector(host, port);
            }
            if (password.equals("")) {
                return new MyJSchShellTtyConnector(host, port, username);
            }
            return new MyJSchShellTtyConnector(host, port, username, password);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // TODO 获取 sFTP channel


    private int testConnection() {
        host = hostField.getText();
        port = Integer.parseInt(portField.getText());
        logger.debug(host);
        logger.debug(port);

        HostConfigEntry hostConfigEntry = new HostConfigEntry();
        hostConfigEntry.setHostName(host);
        hostConfigEntry.setHost(host);
        hostConfigEntry.setPort(port);

        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        if (host.equals("")) {
            try {
                client.close();
            } catch (IOException e) {
                logger.debug(e.getMessage());
            }
            return 2;
        }

        try {
            ClientSession session = client.connect(hostConfigEntry).verify(5000).getClientSession();
            session.close();
            client.close();
            return 1;
        } catch (IOException ioException) {
            try {
                client.close();
            } catch (IOException e) {
                logger.debug(e.getMessage());
            }
            logger.debug(ioException.getMessage());
        }
        return 0;
    }


}
