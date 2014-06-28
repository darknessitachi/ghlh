package com.ghlh.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;

import com.ghlh.conf.ConfigurationAccessor;
import com.ghlh.icons.StockIconsFactory;
import com.ghlh.ui.autotrade.MainPanel;
import com.ghlh.ui.autotradestart.StartMainPanel;
import com.jidesoft.action.CommandBar;
import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.action.DefaultDockableBarHolder;
import com.jidesoft.action.DockableBarDockableHolderPanel;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.rss.FeedReader;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.LayoutPersistence;
import com.jidesoft.utils.SystemInfo;

public class Launcher extends DefaultDockableBarHolder {

	private static Launcher _frame;

	public static Launcher get_frame() {
		return _frame;
	}

	private static StatusBar _statusBar;

	static JideTabbedPane _tabbedPane;
	static DockableBarDockableHolderPanel _feedPanel;
	static DockableBarDockableHolderPanel _configPanel;
	static DockableBarDockableHolderPanel _decisionPanel;

	public Launcher(String title) throws HeadlessException {
		super(title);
	}

	public DockingManager getDockingManager() {
		if (_tabbedPane == null) {
			return null;
		}
		int selected = _tabbedPane.getSelectedIndex();
		if (selected == 0) {
			return _feedPanel.getDockingManager();
		} else if (selected == 1) {
			return _decisionPanel.getDockingManager();
		} else if (selected == 2) {
			return _configPanel.getDockingManager();
		} else
			return null;
	}

	@Override
	public LayoutPersistence getLayoutPersistence() {
		if (_tabbedPane == null) {
			return null;
		}
		int selected = _tabbedPane.getSelectedIndex();
		if (selected == 0) {
			return _feedPanel.getLayoutPersistence();
		} else if (selected == 1) {
			return _decisionPanel.getLayoutPersistence();
		} else if (selected == 2) {
			return _configPanel.getLayoutPersistence();
		} else
			return null;
	}

	static String getPrefix() {
		if (_tabbedPane.getSelectedIndex() == 0) {
			return "feed_";
		} else if (_tabbedPane.getSelectedIndex() == 1) {
			return "decision_";
		} else if (_tabbedPane.getSelectedIndex() == 2) {
			return "config_";
		}
		return "";
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			String openSoftware = args[0];
			if (openSoftware.toLowerCase().equals("close")) {
				ConfigurationAccessor.getInstance().setOpenSoftwareTrade(false);
			}
			if (openSoftware.toLowerCase().equals("open")) {
				ConfigurationAccessor.getInstance().setOpenSoftwareTrade(true);
			}
			if (args.length > 1) {
				ConfigurationAccessor.getInstance().setTradeWay(args[1]);
				if (args.length > 2) {
					String openTradeLog = args[2];
					if (openTradeLog.toLowerCase().equals("closeTradeLog")) {
						ConfigurationAccessor.getInstance().setTradeLog(false);
					}
					if (openSoftware.toLowerCase().equals("openTradeLog")) {
						ConfigurationAccessor.getInstance().setTradeLog(true);
					}
				}
			}
		}
		// setNative(true) will make the color used by action framework to be
		// kept the same as native XP theme.
		Office2003Painter.setNative(SystemInfo.isWindowsXP());

		LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
		showWindow();

	}

	public static void showWindow() {
		if (_frame != null) {
			_frame.toFront();
			return;
		}
		_frame = new Launcher("股海猎户股票自动决策交易助手");
		_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		_frame.setIconImage(StockIconsFactory.getImageIcon(
				StockIconsFactory.Title64).getImage());

		// add a window listener so that timer can be stopped when exit
		_frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				clearUp();
				System.exit(0);
			}
		});

		_feedPanel = new DockableBarDockableHolderPanel(_frame);
		_configPanel = new DockableBarDockableHolderPanel(_frame);
		_decisionPanel = new DockableBarDockableHolderPanel(_frame);

		_tabbedPane = new JideTabbedPane();
		_tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
		_frame.getDockableBarManager().getMainContainer()
				.setLayout(new BorderLayout());
		_frame.getDockableBarManager().addDockableBar(createMenuBar());
		_frame.getDockableBarManager().getMainContainer()
				.add(_tabbedPane, BorderLayout.CENTER);

		Component trailingComponent = new LabelUIResource(
				StockIconsFactory.getImageIcon(StockIconsFactory.PageFinancial));
		_tabbedPane.setTabTrailingComponent(trailingComponent);

		ImageIcon feedImage = StockIconsFactory
				.getImageIcon(StockIconsFactory.TabFeed32);
		ImageIcon configImage = StockIconsFactory
				.getImageIcon(StockIconsFactory.TabConfig32);
		ImageIcon decisionImage = StockIconsFactory
				.getImageIcon(StockIconsFactory.TabDecision32);
		// _tabbedPane.addTab("博客订阅", feedImage, _feedPanel);
		_tabbedPane.addTab("自动交易设置", decisionImage, _decisionPanel);
		_tabbedPane.addTab("自动交易启动", configImage, _configPanel);

		_tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switchDockableBarManager(_tabbedPane.getSelectedIndex());
			}
		});

		_decisionPanel.add(MainPanel.getInstance().getMainPanel());
		_configPanel.add(new StartMainPanel().getContentPanel());
		// FeedReader _reader = new FeedReader(
		// new String[] { //"http://blog.sina.com.cn/rss/2716358792.xml"
		// "http://blog.sina.com.cn/rss/2716358792.xml",
		// "http://blog.sina.com.cn/rss/1216826604.xml",
		// "http://blog.sina.com.cn/rss/1278228085.xml",
		// "http://luodaisohu.blog.sohu.com/rss"
		// }, "http://blog.sina.com.cn/rss/2716358792.xml");
		// _feedPanel.add(_reader);

		// add status bar
		_statusBar = createStatusBar();
		_frame.getContentPane().add(_statusBar, BorderLayout.AFTER_LAST_LINE);

		_frame.getDockableBarManager().loadLayoutData();

		switchDockableBarManager(_tabbedPane.getSelectedIndex());

		_frame.toFront();
	}

	private static void switchDockableBarManager(int index) {
		if (index == 0) {
			_feedPanel.getDockableBarManager().setFloatingDockableBarsVisible(
					true);
			_feedPanel.getDockingManager().setFloatingFramesVisible(true);
		} else {
			_feedPanel.getDockingManager()
					.stopShowingAutohideFrameImmediately();
			_feedPanel.getDockingManager().setFloatingFramesVisible(false);
			_feedPanel.getDockableBarManager().setFloatingDockableBarsVisible(
					false);
		}

		if (index == 1) {
			_configPanel.getDockableBarManager()
					.setFloatingDockableBarsVisible(true);
			_configPanel.getDockingManager().setFloatingFramesVisible(true);
		} else {
			_configPanel.getDockingManager()
					.stopShowingAutohideFrameImmediately();
			_configPanel.getDockingManager().setFloatingFramesVisible(false);
			_configPanel.getDockableBarManager()
					.setFloatingDockableBarsVisible(false);
		}
	}

	private static void clearUp() {
		if (_frame.getDockableBarManager() != null) {
			_frame.getDockableBarManager().saveLayoutData();
		}
		if (_statusBar != null && _statusBar.getParent() != null)
			_statusBar.getParent().remove(_statusBar);
		_statusBar = null;
		_frame.dispose();
		_frame = null;
	}

	private static StatusBar createStatusBar() {
		// setup status bar
		StatusBar statusBar = new StatusBar();

		final LabelStatusBarItem rec = new LabelStatusBarItem("状态栏");
		rec.setText("状态栏");
		rec.getComponent().setForeground(Color.GRAY);
		rec.setAlignment(JLabel.CENTER);
		statusBar.add(rec, JideBoxLayout.FLEXIBLE);

		statusBar.add(StatusField.getInstance().getField(), JideBoxLayout.VARY);
		StatusField.getInstance().setPromptMessage("系统就绪!");
		return statusBar;
	}

	protected static CommandBar createMenuBar() {
		CommandBar commandBar = new CommandMenuBar("Menu Bar");
		commandBar.setStretch(true);
		commandBar.setPaintBackground(false);

		JMenu fileMenu = createFileMenu();
		JMenu helpMenu = createHelpMenu();

		commandBar.add(fileMenu);
		commandBar.add(helpMenu);

		return commandBar;
	}

	private static JMenu createHelpMenu() {
		JMenu menu = new JideMenu("帮助");
		menu.setMnemonic('H');

		JMenuItem item = new JMenuItem("关于股票决策自动交易助手");
		item.setMnemonic('A');
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// Todo
			}
		});
		menu.add(item);

		return menu;
	}

	private static JMenu createFileMenu() {
		JMenuItem item;

		JMenu menu = new JideMenu("系统");
		menu.setMnemonic('F');

		item = new JMenuItem("退出");
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				clearUp();
			}
		});
		menu.add(item);
		return menu;
	}

}

class LabelUIResource extends JLabel implements UIResource {
	public LabelUIResource(String text) {
		super(text);
	}

	public LabelUIResource(Icon image) {
		super(image);
	}

}