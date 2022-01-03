package com.g3g4x5x6.ui.embed.editor;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.util.OsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.function.BiConsumer;

import static com.formdev.flatlaf.FlatClientProperties.*;


@Slf4j
public class EmbedEditor extends JFrame implements ActionListener {
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("文件");
    private JMenu editMenu = new JMenu("编辑");
    private JMenu searchMenu = new JMenu("搜索");
    private JMenu viewMenu = new JMenu("视图");
    private JMenu encodeMenu = new JMenu("编码");
    private JMenu langMenu = new JMenu("语言");
    private JMenu settingsMenu = new JMenu("设置");
    private JMenu macroMenu = new JMenu("宏");
    private JMenu runMenu = new JMenu("运行");
    private JMenu pluginMenu = new JMenu("插件");
    private JMenu winMenu = new JMenu("窗口");

    private JToolBar toolBar = new JToolBar();
    private JButton newBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/addFile.svg"));
    private JButton openBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/menu-open.svg"));
    private JButton saveBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/savedContext.svg"));
    private JButton saveAllBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/Save.svg"));
    private JButton closeBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/ignore_file.svg"));
    private JButton closeAllBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/ignore_file.svg"));
    private JButton cutBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/menu-cut.svg"));
    private JButton copyBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/copy.svg"));
    private JButton pasteBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/menu-paste.svg"));
    private JButton undoBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/undo.svg"));
    private JButton redoBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/redo.svg"));
    private JTabbedPane tabbedPane;
    private JToolBar statusBar;
    private JPopupMenu trailPopupMenu = new JPopupMenu();

    private LinkedList<EditorPanel> globalWindows = new LinkedList<>();
    private LinkedHashMap<EditorPanel, Integer> tmpWindows = new LinkedHashMap<>();     // <EditorPanel, index>

    public EmbedEditor() {
        this.setLayout(new BorderLayout());
        this.setTitle("简易编辑器");
        this.setSize(new Dimension(1200, 700));
        this.setPreferredSize(new Dimension(1200, 700));
        this.setLocationRelativeTo(null);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(searchMenu);
        menuBar.add(viewMenu);
        menuBar.add(encodeMenu);
        menuBar.add(langMenu);
        menuBar.add(settingsMenu);
        menuBar.add(macroMenu);
        menuBar.add(runMenu);
        menuBar.add(pluginMenu);
        menuBar.add(winMenu);

        toolBar.setFloatable(false);
        toolBar.add(newBtn);
        toolBar.add(openBtn);
        toolBar.add(saveBtn);
        toolBar.add(saveAllBtn);
        toolBar.add(closeBtn);
        toolBar.add(closeAllBtn);
        toolBar.addSeparator();
        toolBar.add(cutBtn);
        toolBar.add(copyBtn);
        toolBar.add(pasteBtn);
        toolBar.addSeparator();
        toolBar.add(undoBtn);
        toolBar.add(redoBtn);
        initToolbarAction();

        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        initClosableTabs(tabbedPane);
        customComponents();
        addAndSelectPanel(new EditorPanel());

        statusBar = new JToolBar();
        statusBar.setFloatable(false);
        statusBar.add(new JLabel("文本文件"));

        this.setJMenuBar(menuBar);
        this.add(toolBar, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.SOUTH);
    }

    private void initToolbarAction() {
        newBtn.registerKeyboardAction(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                quickAction("newAction");
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        saveBtn.registerKeyboardAction(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                quickAction("saveAction");
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void initClosableTabs(JTabbedPane tabbedPane) {
        tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSABLE, true);
        tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, "Close");
        tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSE_CALLBACK,
                (BiConsumer<JTabbedPane, Integer>) (tabPane, tabIndex) -> {
                    globalWindows.remove(tabPane.getComponentAt(tabIndex));
                    this.tabbedPane.removeTabAt(tabIndex);
                    if (this.tabbedPane.getTabCount() == 0) {
                        addAndSelectPanel(new EditorPanel());
                    }
                });
    }

    private void customComponents() {
        JToolBar leading = null;
        JToolBar trailing = null;
        leading = new JToolBar();
        leading.setFloatable(false);
        leading.setBorder(null);
        trailing = new JToolBar();
        trailing.setFloatable(false);
        trailing.setBorder(null);

        JButton dashboardBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/homeFolder.svg"));
        dashboardBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(0);
            }
        });

        JButton addBtn = new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/add.svg"));
        addBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAndSelectPanel(new EditorPanel());
            }
        });

        // TODO 选项卡面板前置工具栏，暂不使用
        leading.add(dashboardBtn);
//        mainTabbedPane.putClientProperty(TABBED_PANE_LEADING_COMPONENT, leading);
        // TODO 选项卡面板后置工具栏
        String iconPath = null;
        if (OsUtils.isWin32()) {
            // windows.svg
            iconPath = "com/g3g4x5x6/ui/icons/windows.svg";
        } else if (OsUtils.isUNIX()) {
            // linux.svg
            iconPath = "com/g3g4x5x6/ui/icons/linux.svg";
        } else if (OsUtils.isOSX()) {
            // macOS.svg
            iconPath = "com/g3g4x5x6/ui/icons/macOS.svg";
        }
        JButton trailMenuBtn = new JButton(new FlatSVGIcon(iconPath));
        JMenuItem item = new JMenuItem("代码安全检查");
        item.setIcon(new FlatSVGIcon("com/g3g4x5x6/ui/icons/shield.svg"));
        item.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(EmbedEditor.this, "敬请期待！", "信息", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        trailPopupMenu.add(item);
        trailMenuBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                trailPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        trailing.add(addBtn);
        trailing.add(Box.createHorizontalGlue());
//        trailing.add(new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/commit.svg")));
//        trailing.add(new JButton(new FlatSVGIcon("com/g3g4x5x6/ui/icons/diff.svg")));
        trailing.add(trailMenuBtn);
        tabbedPane.putClientProperty(TABBED_PANE_TRAILING_COMPONENT, trailing);
    }

    public void addAndSelectPanel(EditorPanel editorPanel) {
        boolean flag = true;
        for (EditorPanel editor : globalWindows) {
            if (editor.getSavePath() != null && editor.getSavePath().equals(editorPanel.getSavePath())) {
                log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>" + editor.getSavePath());
                if (editor.getFs() != null && editor.getFs().getId().equals(editorPanel.getFs().getId())) {
                    this.tabbedPane.setSelectedComponent(editor);
                    flag = false;
                    log.debug("已有编辑面板");
                }
            }
        }
        if (flag) {
            this.globalWindows.add(editorPanel);
            this.tabbedPane.addTab(editorPanel.getTitle(), editorPanel.getIcon(), editorPanel, editorPanel.getTips());
            this.tabbedPane.setSelectedIndex(this.tabbedPane.getTabCount() - 1);
        }
    }

    private boolean exist(EditorPanel editorPanel) {

        return false;
    }

    private void quickAction(String command) throws IOException {
        switch (command) {
            case "newAction":
                log.debug("newAction");
                addAndSelectPanel(new EditorPanel());
                break;
            case "saveAction":
                log.debug("saveAction");
                EditorPanel editorPanel = (EditorPanel) this.tabbedPane.getComponentAt(this.tabbedPane.getSelectedIndex());
                if (editorPanel.getFs() != null) {
                    // 远程文件保存   Files.write(fs.getPath(titleField.getSelectedItem().toString().strip()), textArea.getText().getBytes(StandardCharsets.UTF_8));
                    Files.write(editorPanel.getFs().getPath(editorPanel.getSavePath()), editorPanel.getTextArea().getBytes(StandardCharsets.UTF_8));
                } else {
                    // 本地文件保存
                }
                break;
        }
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug(e.getActionCommand());
    }
}
