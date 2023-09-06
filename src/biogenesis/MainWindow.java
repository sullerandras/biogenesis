/* Copyright (C) 2006-2010  Joan Queralt Molina
 * Color Mod (C) 2012-2022  MarcoDBAA
 * Backup function by Tyler Coleman
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package biogenesis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import biogenesis.gui.stats.StatisticsWindow;

public class MainWindow extends JFrame implements MainWindowInterface {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	protected VisibleWorld _visibleWorld;
	protected World _world;
	protected boolean _isProcessActive = false;
	protected transient java.util.Timer _timer;
	protected transient Thread workerThread = null;
	protected JFileChooser worldChooser = new JFileChooserWithRemember();
	protected JFileChooser geneticCodeChooser = new JFileChooserWithRemember();
	protected BioFile _gameFile = null;

	protected JScrollPane scrollPane;
	protected StdAction newGameAction;
	protected StartStopAction startStopAction;
	protected StdAction saveGameAction;
	protected StdAction backupGameAction;
	protected StdAction increaseCO2Action;
	protected StdAction decreaseCO2Action;
	protected StdAction increaseCH4Action;
	protected StdAction decreaseCH4Action;
	protected StdAction increaseDetritusAction;
	protected StdAction decreaseDetritusAction;
	protected StdAction manageConnectionsAction;
	protected StdAction abortTrackingAction;
	protected StdAction openGameAction;
	protected StdAction saveGameAsAction;
	protected StdAction quitAction;
	protected StdAction statisticsAction;
	protected StdAction labAction;
	protected StdAction killAllAction;
	protected StdAction disperseAllAction;
	protected StdAction parametersAction;
	protected StdAction aboutAction;
	protected StdAction manualAction;
	protected StdAction checkLastVersionAction;
	protected StdAction netConfigAction;
	protected StdAction saveWorldImageAction;
	protected JCheckBox toggleRepaintWorld;

	protected JMenuItem _menuStartStopGame;
	protected JMenu _menuGame;
	protected JMenu _menuWorld;
	protected JMenu _menuGlobal;
	protected JMenu _menuHelp;
	protected JMenu _menuNet;
	protected NumberFormat _nf;
	protected JLabel _statusLabel;
	protected JToolBar toolBar = new JToolBar(Messages.getString("T_PROGRAM_NAME")); //$NON-NLS-1$

	protected InfoToolbar infoToolbar;

	private String statusMessage = ""; //$NON-NLS-1$
	private StringBuilder statusLabelText = new StringBuilder(100);
	protected Organism _trackedOrganism = null;

	protected transient NetServerThread serverThread = null;

	protected StatisticsWindow _statisticsWindow = null;
	// Comptador de frames, per saber quan actualitzar la finestra d'informaci�
	protected long nFrames = 0;
	private ImageIcon imageIcon = new ImageIcon(getClass().getResource("images/bullet.jpg")); //$NON-NLS-1$

	public JFileChooser getWorldChooser() {
		return worldChooser;
	}

	public JFileChooser getGeneticCodeChooser() {
		return geneticCodeChooser;
	}

	public void setStatusMessage(String str) {
		statusMessage = str;
		updateStatusLabel();
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public World getWorld() {
		return _world;
	}

	public VisibleWorld getVisibleWorld() {
		return _visibleWorld;
	}

	public boolean isProcessActive() {
		return _isProcessActive;
	}

	public InfoToolbar getInfoPanel() {
		return infoToolbar;
	}

	@Override
	public BioFile getBioFile() {
		return _gameFile;
	}

	public MainWindow() {
		createActions();
		createMenu();
		createToolBar();
		setControls();
		setResizable(true);
		setSize(new Dimension(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT));
		WindowManager.registerWindow(this, getWidth(), getHeight(), 0, 0);
		configureApp();
		_world = new World(_visibleWorld);
		startApp();
		_world.genesis();
		scrollPane.setViewportView(_visibleWorld);
		worldChooser = setUpdateUI(worldChooser);

		this.addWindowListener(new AppFocusWindowAdapter());
		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				Utils.setMainWindowInFocus(true);
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				Utils.setMainWindowInFocus(false);
			}
		});
	}

	@Override
	public Frame getFrame() {
		return this;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("java -jar biogenesis.jar [random seed]");
		} else if (args.length == 1) {
			try {
				long seed = Long.parseLong(args[0]);
				Utils.random.setSeed(seed);
			} catch (NumberFormatException e) {
				System.err.println("java -jar biogenesis.jar [random seed]");
			}
		}
		Utils.readPreferences();
		new MainWindow();
	}

	private void createActions() {
		newGameAction = new NewGameAction("T_NEW", "images/menu_new.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_NEW_WORLD"); //$NON-NLS-1$
		startStopAction = new StartStopAction("T_RESUME", "T_PAUSE", "images/menu_start.png", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
				"images/menu_stop.png", "T_RESUME_PROCESS", "T_PAUSE_PROCESS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		saveGameAction = new SaveGameAction("T_SAVE", "images/menu_save.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_SAVE_WORLD"); //$NON-NLS-1$
		backupGameAction = new BackupGameAction("T_BACKUP", "images/menu_backup.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_BACKUP_WORLD"); //$NON-NLS-1$
		increaseCO2Action = new IncreaseCO2Action("T_INCREASE_CO2", "images/menu_increase_co2.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_INCREASE_CO2"); //$NON-NLS-1$
		decreaseCO2Action = new DecreaseCO2Action("T_DECREASE_CO2", "images/menu_decrease_co2.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_DECREASE_CO2"); //$NON-NLS-1$
		manageConnectionsAction = new ManageConnectionsAction("T_MANAGE_CONNECTIONS", "images/menu_manage_connections.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_MANAGE_CONNECTIONS"); //$NON-NLS-1$
		abortTrackingAction = new AbortTrackingAction("T_ABORT_TRACKING", "images/menu_stop_tracking.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_ABORT_TRACKING"); //$NON-NLS-1$
		saveWorldImageAction = new SaveWorldImageAction("T_SAVE_IMAGE", "images/menu_save_image.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_SAVE_IMAGE"); //$NON-NLS-1$
		openGameAction = new OpenGameAction("T_OPEN", null, "T_OPEN_WORLD"); //$NON-NLS-1$//$NON-NLS-2$
		saveGameAsAction = new SaveGameAsAction("T_SAVE_AS", null, "T_SAVE_WORLD_AS"); //$NON-NLS-1$//$NON-NLS-2$
		quitAction = new QuitAction("T_QUIT", null, "T_QUIT_PROGRAM"); //$NON-NLS-1$ //$NON-NLS-2$
		statisticsAction = new StatisticsAction("T_STATISTICS", null, "T_WORLD_STATISTICS"); //$NON-NLS-1$ //$NON-NLS-2$
		labAction = new LabAction("T_GENETIC_LABORATORY", null, "T_GENETIC_LABORATORY"); //$NON-NLS-1$ //$NON-NLS-2$
		increaseCH4Action = new IncreaseCH4Action("T_INCREASE_CH4", null, "T_INCREASE_CH4"); //$NON-NLS-1$ //$NON-NLS-2$
		decreaseCH4Action = new DecreaseCH4Action("T_DECREASE_CH4", null, "T_DECREASE_CH4"); //$NON-NLS-1$ //$NON-NLS-2$
		increaseDetritusAction = new IncreaseDetritusAction("T_INCREASE_DETRITUS", null, "T_INCREASE_DETRITUS"); //$NON-NLS-1$ //$NON-NLS-2$
		decreaseDetritusAction = new DecreaseDetritusAction("T_DECREASE_DETRITUS", null, "T_DECREASE_DETRITUS"); //$NON-NLS-1$ //$NON-NLS-2$
		killAllAction = new KillAllAction("T_KILL_ALL", null, "T_KILL_ALL_ORGANISMS"); //$NON-NLS-1$ //$NON-NLS-2$
		disperseAllAction = new DisperseAllAction("T_DISPERSE_ALL", null, "T_DISPERSE_ALL_DEAD_ORGANISMS"); //$NON-NLS-1$ //$NON-NLS-2$
		parametersAction = new ParametersAction("T_PARAMETERS", null, "T_EDIT_PARAMETERS"); //$NON-NLS-1$ //$NON-NLS-2$
		aboutAction = new AboutAction("T_ABOUT", null, "T_ABOUT"); //$NON-NLS-1$//$NON-NLS-2$
		manualAction = new ManualAction("T_USER_MANUAL", null, "T_USER_MANUAL"); //$NON-NLS-1$//$NON-NLS-2$
		checkLastVersionAction = new CheckLastVersionAction("T_CHECK_LAST_VERSION", null, "T_CHECK_LAST_VERSION"); //$NON-NLS-1$ //$NON-NLS-2$
		netConfigAction = new NetConfigAction("T_CONFIGURE_NETWORK", null, "T_CONFIGURE_NETWORK"); //$NON-NLS-1$ //$NON-NLS-2$

		toggleRepaintWorld = new JCheckBox(Messages.getString("T_RENDER_WORLD"));
		toggleRepaintWorld.setSelected(Utils.isRepaintWorld());
		toggleRepaintWorld.setAlignmentY(100);
		toggleRepaintWorld.addActionListener(arg0 -> Utils.setRepaintWorld(!Utils.isRepaintWorld()));
	}

	public void createToolBar() {
		toolBar.removeAll();
		toolBar.add(newGameAction);
		toolBar.add(startStopAction);
		toolBar.add(saveGameAction);
		toolBar.add(backupGameAction);
		toolBar.add(increaseCO2Action);
		toolBar.add(decreaseCO2Action);
		toolBar.add(manageConnectionsAction);
		toolBar.add(saveWorldImageAction);
		toolBar.add(abortTrackingAction);
		abortTrackingAction.setEnabled(_trackedOrganism != null);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(toggleRepaintWorld);
		toolBar.invalidate();
		toolBar.repaint();
	}

	private void createMenu() {
		JMenuItem menuItem;
		JMenuBar menuBar = new JMenuBar();
		_menuGame = new JMenu(Messages.getString("T_GAME")); //$NON-NLS-1$
		_menuGame.setMnemonic(Messages.getMnemonic("T_GAME").intValue()); //$NON-NLS-1$
		menuBar.add(_menuGame);
		menuItem = new JMenuItem(newGameAction);
		menuItem.setIcon(null);
		_menuGame.add(menuItem);
		_menuStartStopGame = new JMenuItem(startStopAction);
		_menuStartStopGame.setIcon(null);
		_menuGame.add(_menuStartStopGame);
		_menuGame.add(new JMenuItem(openGameAction));
		menuItem = new JMenuItem(saveGameAction);
		menuItem.setIcon(null);
		_menuGame.add(menuItem);
		_menuGame.add(new JMenuItem(saveGameAsAction));
		menuItem = new JMenuItem(backupGameAction);
		menuItem.setIcon(null);
		_menuGame.add(menuItem);
		_menuGame.add(new JMenuItem(quitAction));
		_menuGlobal = new JMenu(Messages.getString("T_GLOBAL_ACTIONS")); //$NON-NLS-1$
		_menuGlobal.setMnemonic(Messages.getMnemonic("T_GLOBAL_ACTIONS").intValue()); //$NON-NLS-1$
		menuBar.add(_menuGlobal);
		menuItem = new JMenuItem(increaseCO2Action);
		menuItem.setIcon(null);
		_menuGlobal.add(menuItem);
		menuItem = new JMenuItem(decreaseCO2Action);
		menuItem.setIcon(null);
		_menuGlobal.add(menuItem);
		_menuGlobal.add(new JMenuItem(increaseCH4Action));
		_menuGlobal.add(new JMenuItem(decreaseCH4Action));
		_menuGlobal.add(new JMenuItem(increaseDetritusAction));
		_menuGlobal.add(new JMenuItem(decreaseDetritusAction));
		_menuGlobal.add(new JMenuItem(killAllAction));
		_menuGlobal.add(new JMenuItem(disperseAllAction));
		_menuNet = new JMenu(Messages.getString("T_NETWORK")); //$NON-NLS-1$
		_menuNet.setMnemonic(Messages.getMnemonic("T_NETWORK").intValue()); //$NON-NLS-1$
		menuBar.add(_menuNet);
		_menuNet.add(new JMenuItem(netConfigAction));
		menuItem = new JMenuItem(manageConnectionsAction);
		menuItem.setIcon(null);
		_menuNet.add(menuItem);
		_menuWorld = new JMenu(Messages.getString("T_WORLD")); //$NON-NLS-1$
		_menuWorld.setMnemonic(Messages.getMnemonic("T_WORLD").intValue()); //$NON-NLS-1$
		menuBar.add(_menuWorld);
		_menuWorld.add(new JMenuItem(statisticsAction));
		_menuWorld.add(new JMenuItem(labAction));
		_menuWorld.add(new JMenuItem(parametersAction));
		_menuHelp = new JMenu(Messages.getString("T_HELP")); //$NON-NLS-1$
		_menuHelp.setMnemonic(Messages.getMnemonic("T_HELP").intValue()); //$NON-NLS-1$
		menuBar.add(_menuHelp);
		_menuHelp.add(new JMenuItem(manualAction));
		_menuHelp.add(new JMenuItem(checkLastVersionAction));
		_menuHelp.add(new JMenuItem(aboutAction));
		// Only enable file management menu options if at least there is
		// permission to read user's home directory
		SecurityManager sec = System.getSecurityManager();
		try {
			if (sec != null)
				sec.checkPropertyAccess("user.home"); //$NON-NLS-1$
		} catch (SecurityException ex) {
			openGameAction.setEnabled(false);
			backupGameAction.setEnabled(false);
			saveGameAsAction.setEnabled(false);
			saveGameAction.setEnabled(false);
			manualAction.setEnabled(false);
			netConfigAction.setEnabled(false);
			manageConnectionsAction.setEnabled(false);
		}
		setJMenuBar(menuBar);
	}

	protected void checkLastVersion() {
		CheckVersionThread thread = new CheckVersionThread(this);
		thread.start();
	}

	protected void netConfig() {
		new NetConfigWindow(this);
	}

	public void setAcceptConnections(boolean newAcceptConnections) {
		if (newAcceptConnections != Utils.ACCEPT_CONNECTIONS) {
			Utils.ACCEPT_CONNECTIONS = newAcceptConnections;
			if (newAcceptConnections)
				startServer();
			else if (serverThread.getConnections().isEmpty())
				stopServer();
		}
	}

	public boolean isAcceptingConnections() {
		return Utils.ACCEPT_CONNECTIONS;
	}

	public NetServerThread getNetServer() {
		return serverThread;
	}

	public NetServerThread startServer() {
		if (serverThread == null || !serverThread.isActive()) {
			serverThread = new NetServerThread(this);
			serverThread.start();
		}
		return serverThread;
	}

	public void stopServer() {
		if (serverThread != null) {
			serverThread.closeServer();
		}
	}

	class NewGameAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public NewGameAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			int newWorld = JOptionPane.YES_NO_CANCEL_OPTION;
			Object[] options = { Messages.getString("T_CANCEL"), Messages.getString("T_YES"),
					Messages.getString("T_SAVE_WORLD") };
			newWorld = JOptionPane.showOptionDialog(null, Messages.getString("T_CREATE_NEW_WORLD") + "?", //$NON-NLS-1$
					Messages.getString("T_NEW_WORLD"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (newWorld == JOptionPane.NO_OPTION) {
				_trackedOrganism = null;
				_world.genesis();
				scrollPane.setViewportView(_visibleWorld);
				_isProcessActive = false;
				startStopAction.setActive(false);
				_menuStartStopGame.setIcon(null);
				setStatusMessage(Messages.getString("T_NEW_WORLD_CREATED")); //$NON-NLS-1$
			} else {
				if (newWorld == JOptionPane.CANCEL_OPTION) {
					if (_gameFile != null) {
						saveObject(_world, _gameFile.getFile());
					} else {
						if (saveGameAs() == null)
							return;
					}
				}
			}
		}
	}

	class StartStopAction extends StdAction {
		private static final long serialVersionUID = 1L;
		protected String name2;
		protected String description2;
		protected Integer mnemonic2;
		protected ImageIcon icon2;

		protected String name2_key;
		protected String desc2_key;

		protected boolean active;

		public StartStopAction(String text1, String text2, String icon_path1,
				String icon_path2, String desc1, String desc2) {
			super(text1, icon_path1, desc1);
			name2 = Messages.getString(text2);
			description2 = Messages.getString(desc2);
			icon2 = createIcon(icon_path2);
			mnemonic2 = Messages.getMnemonic(text2);
			name2_key = text2;
			desc2_key = desc2;
			active = false;

			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_PAUSE_ACCELERATOR")));
		}

		public void actionPerformed(ActionEvent e) {
			if (_isProcessActive)
				pauseGame();
			else
				playGame();
		}

		public void toogle() {
			String aux;
			ImageIcon auxicon;
			Integer auxmnemonic;
			aux = (String) getValue(NAME);
			putValue(NAME, name2);
			name2 = aux;
			aux = (String) getValue(SHORT_DESCRIPTION);
			putValue(SHORT_DESCRIPTION, description2);
			description2 = aux;
			auxicon = (ImageIcon) getValue(SMALL_ICON);
			putValue(SMALL_ICON, icon2);
			icon2 = auxicon;
			auxmnemonic = (Integer) getValue(MNEMONIC_KEY);
			putValue(MNEMONIC_KEY, mnemonic2);
			mnemonic2 = auxmnemonic;
			active = !active;
		}

		@Override
		public void changeLocale() {
			super.changeLocale();
			name2 = Messages.getString(name2_key);
			description2 = Messages.getString(desc2_key);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_PAUSE_ACCELERATOR")));
		}

		public void setActive(boolean newState) {
			if (newState != active) {
				toogle();
				active = newState;
			}
		}

		public boolean isActive() {
			return active;
		}
	}

	class SaveGameAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public SaveGameAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_SAVE_ACCELERATOR")));
		}

		public void actionPerformed(ActionEvent e) {
			if (_gameFile != null)
				saveObject(_world, _gameFile.getFile());
			else
				saveGameAs();
		}

		@Override
		public void changeLocale() {
			super.changeLocale();
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_SAVE_ACCELERATOR")));
		}
	}

	class BackupGameAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public BackupGameAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_BACKUP_ACCELERATOR")));
		}

		public void actionPerformed(ActionEvent e) {
			try {
				if (_gameFile != null) {
					saveObject(_world, _gameFile.getFileForTime(_world.getTime(), BioFile.Type.REGULAR));
					if (Utils.AUTO_BACKUP_WORLD_PNG) {
						_isProcessActive = false;
						java.awt.EventQueue.invokeAndWait(() -> saveWorldImage(_gameFile.getFileForTime(_world.getTime(), BioFile.Type.WORLD)));
						_isProcessActive = true;
					}
					if (Utils.AUTO_BACKUP_CLADES_PNG) {
						_isProcessActive = false;
						java.awt.EventQueue.invokeAndWait(() -> saveCladeImage(_gameFile.getFileForTime(_world.getTime(), BioFile.Type.CLADES)));
						_isProcessActive = true;
					}
					GsonFileSaver.saveWorldJson(_world, _gameFile.getFileForTime(_world.getTime(), BioFile.Type.JSON));
					if (Utils.AUTO_BACKUP_STATISTICS_PNG && _statisticsWindow != null) {
						// Apparently we have to wait for the statisticsWindow to repaint, it seems like
						// `repaintStats()` just enqueus an AWT job to repaint the dialog and the method
						// returns before it has been repainted. So we need to save the image in an AWT
						// job to make sure the repaint has been done before the saving.
						_statisticsWindow.repaintStats();
						_isProcessActive = false;
						java.awt.EventQueue.invokeAndWait(() -> saveStatisticsImage(_gameFile.getFileForTime(_world.getTime(), BioFile.Type.STATS)));
						_isProcessActive = true;
					}
				} else {
					java.awt.EventQueue.invokeAndWait(() -> saveGameAs());
				}
			} catch (InvocationTargetException | InterruptedException e1) {
				System.out.println("Error saving game: " + e);
				e1.printStackTrace();
			}
		}

		@Override
		public void changeLocale() {
			super.changeLocale();
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_BACKUP_ACCELERATOR")));
		}
	}

	class IncreaseCO2Action extends StdAction {
		private static final long serialVersionUID = 1L;

		public IncreaseCO2Action(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.addCO2(1000);
		}
	}

	class DecreaseCO2Action extends StdAction {
		private static final long serialVersionUID = 1L;

		public DecreaseCO2Action(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.decreaseCO2(1000);
		}
	}

	class IncreaseCH4Action extends StdAction {
		private static final long serialVersionUID = 1L;

		public IncreaseCH4Action(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.addCH4(1000);
		}
	}

	class DecreaseCH4Action extends StdAction {
		private static final long serialVersionUID = 1L;

		public DecreaseCH4Action(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.decreaseCH4(1000);
		}
	}

	class IncreaseDetritusAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public IncreaseDetritusAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.addDetritus(1000);
		}
	}

	class DecreaseDetritusAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public DecreaseDetritusAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.decreaseDetritus(1000);
		}
	}

	class ManageConnectionsAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public ManageConnectionsAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			netConnectionsWindow();
		}
	}

	class AbortTrackingAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public AbortTrackingAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			setTrackedOrganism(null);
		}
	}

	class SaveWorldImageAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public SaveWorldImageAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			// Stop time while asking for a file name
			_isProcessActive = false;
			startStopAction.setActive(false);
			_menuStartStopGame.setIcon(null);
			try {
				// Ask for file name
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new BioFileFilter("png")); //$NON-NLS-1$
				int returnVal = chooser.showSaveDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					int canWrite = JOptionPane.YES_OPTION;
					File f = chooser.getSelectedFile();
					// Check if file already exists and ask for confirmation
					if (f.exists()) {
						canWrite = JOptionPane.showConfirmDialog(null, Messages.getString("T_CONFIRM_FILE_OVERRIDE"), //$NON-NLS-1$
								Messages.getString("T_FILE_EXISTS"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
					}
					if (canWrite == JOptionPane.YES_OPTION) {
						saveWorldImage(f);
					}
				}
			} catch (SecurityException ex) {
				System.err.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, Messages.getString("T_PERMISSION_DENIED"), //$NON-NLS-1$
						Messages.getString("T_PERMISSION_DENIED"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
			_isProcessActive = false;
		}
	}

	class OpenGameAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public OpenGameAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_isProcessActive = false;
			try {
				int returnVal = getWorldChooser().showOpenDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Elimina les finestres antigues
					if (_statisticsWindow != null) {
						_statisticsWindow.dispose();
						_statisticsWindow = null;
					}
					// _world.clean();
					ObjectInputStream inputStream;
					try {
						File f = getWorldChooser().getSelectedFile();
						InputStream fileStream = new FileInputStream(f);
						if (f.getName().endsWith(".gz")) {
							fileStream = new GZIPInputStream(fileStream);
						}
						inputStream = new ObjectInputStream(fileStream);
						_world = (World) inputStream.readObject();
						inputStream.close();
						_gameFile = new BioFile(f);
						_trackedOrganism = null;
						_world.worldStatistics.saveGameLoaded(MainWindow.this);
						_world._isbackuped = true;
						setStatusMessage(Messages.getString("T_WORLD_LOADED_SUCCESSFULLY")); //$NON-NLS-1$
					} catch (IOException ex) {
						System.err.println(ex.getMessage());
						JOptionPane.showMessageDialog(null, Messages.getString("T_CANT_READ_FILE"), //$NON-NLS-1$
								Messages.getString("T_READ_ERROR"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					} catch (ClassNotFoundException ex) {
						System.err.println(ex.getMessage());
						JOptionPane.showMessageDialog(null, Messages.getString("T_WRONG_FILE_TYPE"), //$NON-NLS-1$
								Messages.getString("T_READ_ERROR"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					} catch (ClassCastException ex) {
						System.err.println(ex.getMessage());
						JOptionPane.showMessageDialog(null, Messages.getString("T_WRONG_FILE_VERSION"), //$NON-NLS-1$
								Messages.getString("T_READ_ERROR"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					}
					// warn user if file is a backup file and might overwrite later backups
					if (_gameFile.fileNameContainsTime() && Utils.AUTO_BACKUP) {
						JOptionPane.showMessageDialog(null, Messages.getString("T_WARNING_OPENING_BACKUP_FILE"));
					}
					// Torna a assignar els valors dels camps no guardats a l'objecte world
					_world.init(_visibleWorld);
					scrollPane.setViewportView(_visibleWorld);
					// Assegurem que s'ha dibuixat el m�n
					_visibleWorld.repaint();
				}
			} catch (SecurityException ex) {
				System.err.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, Messages.getString("T_PERMISSION_DENIED"), //$NON-NLS-1$
						Messages.getString("T_PERMISSION_DENIED"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
			_isProcessActive = false;
			startStopAction.setActive(false);
			_menuStartStopGame.setIcon(null);
		}
	}

	class SaveGameAsAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public SaveGameAsAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			saveGameAs();
		}
	}

	class QuitAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public QuitAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			quit();
		}
	}

	public void quit() {
		int save = JOptionPane.showConfirmDialog(this, Messages.getString("T_SAVE_BEFORE_QUIT"), //$NON-NLS-1$
				Messages.getString("T_SAVE_WORLD"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$

		if (save != JOptionPane.CANCEL_OPTION) {
			if (save == JOptionPane.YES_OPTION) {
				if (_gameFile != null)
					saveObject(_world, _gameFile.getFile());
				else if (saveGameAs() == null)
					return;
			}
			Utils.quitProgram(this);
			if (serverThread != null)
				serverThread.closeServer();
			try {
				System.exit(0);
			} catch (SecurityException ex) {
				dispose();
			}
		}
	}

	class StatisticsAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public StatisticsAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			if (_statisticsWindow != null)
				_statisticsWindow.dispose();
			_statisticsWindow = new StatisticsWindow(MainWindow.this, _world, _visibleWorld, _world.worldStatistics, _world._organisms);
		}
	}

	class LabAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public LabAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			new LabWindow(MainWindow.this);
		}
	}

	class KillAllAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public KillAllAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.killAll();
		}
	}

	class DisperseAllAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public DisperseAllAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			_world.disperseAll();
		}
	}

	class ParametersAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public ParametersAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			paramDialog();
		}
	}

	class ManualAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public ManualAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			String DirNameManual = "/usr/share/doc/biogenesis/biogenesis_manual";
			if ((new File(DirNameManual + "/manual_" + Messages.getLanguage() + ".html")).exists()) {
				BareBonesBrowserLaunch.openURL(DirNameManual + "/manual_" + Messages.getLanguage() + ".html");
			} else {
				if ((new File(DirNameManual + "/manual_en.html")).exists()) {
					BareBonesBrowserLaunch.openURL(DirNameManual + "/manual_en.html");
				} else {
					if ((Messages.getLanguage().equals("ca")) || (Messages.getLanguage().equals("es"))
							|| (Messages.getLanguage().equals("en"))) {
						BareBonesBrowserLaunch.openURL("http://biogenesis.sourceforge.net/manual."
								+ Messages.getLanguage() + ".php"); //$NON-NLS-1$
					} else {
						BareBonesBrowserLaunch.openURL("http://biogenesis.sourceforge.net/manual.en.php"); //$NON-NLS-1$
					}
				}
			}
		}
	}

	class CheckLastVersionAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public CheckLastVersionAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			checkLastVersion();
		}
	}

	class AboutAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public AboutAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			about();
		}
	}

	class NetConfigAction extends StdAction {
		private static final long serialVersionUID = 1L;

		public NetConfigAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}

		public void actionPerformed(ActionEvent e) {
			netConfig();
		}
	}

	protected void paramDialog() {
		new ParamDialog(this);
	}

	public void setTrackedOrganism(Organism org) {
		_trackedOrganism = org;
		abortTrackingAction.setEnabled(_trackedOrganism != null);
	}

	protected void netConnectionsWindow() {
		new NetConnectionsWindow(this);
	}

	public void pauseGame() {
		_isProcessActive = false;
		startStopAction.setActive(false);
		_menuStartStopGame.setIcon(null);
		setStatusMessage(Messages.getString("T_GAME_PAUSED")); //$NON-NLS-1$
	}

	public void playGame() {
		_isProcessActive = true;
		startStopAction.setActive(true);
		_menuStartStopGame.setIcon(null);
		setStatusMessage(Messages.getString("T_GAME_RESUMED")); //$NON-NLS-1$
	}

	protected File saveGameAs() {
		File savedFile = saveObjectAs(this, _world);
		if (savedFile != null)
			_gameFile = new BioFile(savedFile);
		return savedFile;
	}

	protected void about() {
		String aboutString = Messages.getString("T_PROGRAM_NAME") + "http://biogenesis.sourceforge.net/" + "\n" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				+ Messages.getString("T_VERSION_B") + " 0.9\n" + //$NON-NLS-1$//$NON-NLS-2$
				Messages.getString("T_VERSION_CM") + " 2.01\n" + //$NON-NLS-1$//$NON-NLS-2$
				Messages.getString("T_VERSION_BB") + " 1\n" + //$NON-NLS-1$//$NON-NLS-2$
				Messages.getString("T_COPYRIGHT") + "joanq@users.sourceforge.net\n" + //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("T_ARTWORK_BY") + " Ananda Daydream, Florian Haag (http://toolbaricons.sourceforge.net/)"; //$NON-NLS-1$//$NON-NLS-2$
		JOptionPane.showMessageDialog(this, aboutString, Messages.getString("T_ABOUT"), //$NON-NLS-1$
				JOptionPane.INFORMATION_MESSAGE, imageIcon);
	}

	private void setControls() {
		setIconImage(imageIcon.getImage());
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());

		_visibleWorld = new VisibleWorld(this);
		scrollPane = new JScrollPane(_visibleWorld);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		setLocation(Utils.WINDOW_X, Utils.WINDOW_Y);
		setExtendedState(Utils.WINDOW_STATE);
		getContentPane().setLayout(new BorderLayout());

		infoToolbar = new InfoToolbar(null, this);
		centralPanel.add(scrollPane, BorderLayout.CENTER);
		centralPanel.add(infoToolbar, BorderLayout.SOUTH);

		getContentPane().add(centralPanel, BorderLayout.CENTER);

		_statusLabel = new JLabel(" "); //$NON-NLS-1$
		_statusLabel.setBorder(new EtchedBorder());
		_nf = NumberFormat.getInstance();
		_nf.setMaximumFractionDigits(1);
		_nf.setMinimumFractionDigits(1);
		getContentPane().add(_statusLabel, BorderLayout.SOUTH);
		getContentPane().add(toolBar, BorderLayout.NORTH);

		worldChooser.setFileFilter(new BioFileFilter(BioFileFilter.WORLD_EXTENSION, BioFileFilter.WORLD_EXTENSION + ".gz"));
		geneticCodeChooser.setFileFilter(new BioFileFilter(BioFileFilter.GENETIC_CODE_EXTENSION));
		geneticCodeChooser = setUpdateUI(geneticCodeChooser);
	}

	public File saveObjectAs(Component parent, Object obj) {
		File resultFile = null;
		boolean processState = _isProcessActive;
		_isProcessActive = false;

		try {
			JFileChooser chooser;
			if (obj instanceof GeneticCode)
				chooser = getGeneticCodeChooser();
			else
				chooser = getWorldChooser();
			int returnVal = chooser.showSaveDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				int canWrite = JOptionPane.YES_OPTION;
				File f = chooser.getSelectedFile();
				String filename = f.getName().toLowerCase();
				BioFileFilter bff = (BioFileFilter) chooser.getFileFilter();
				boolean fullFilename = filename.endsWith(bff.getValidExtension()) ||
						(!bff.getValidExtension2().equals("") && filename.endsWith(bff.getValidExtension2()));
				if (!fullFilename) {
					if (Utils.COMPRESS_BACKUPS) {
						f = new File(f.getAbsolutePath() + "." + bff.getValidExtension() + ".gz");
					} else {
						f = new File(f.getAbsolutePath() + "." + bff.getValidExtension());
					}
				}
				if (f.exists()) {
					canWrite = JOptionPane.showConfirmDialog(null, Messages.getString("T_CONFIRM_FILE_OVERRIDE"), //$NON-NLS-1$
							Messages.getString("T_FILE_EXISTS"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
				}
				if (canWrite == JOptionPane.YES_OPTION) {
					if (obj instanceof GeneticCode) {
						try {
							BioXMLParser.writeGeneticCode(new PrintStream(f), (GeneticCode) obj);
							resultFile = f;
						} catch (FileNotFoundException ex) {
							System.err.println(ex.getLocalizedMessage());
						}
					} else if (saveObject(obj, f))
						resultFile = f;
				}
			}
		} catch (SecurityException ex) {
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null, Messages.getString("T_PERMISSION_DENIED"), //$NON-NLS-1$
					Messages.getString("T_PERMISSION_DENIED"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
		_isProcessActive = processState;
		return resultFile;
	}

	public boolean saveObject(Object obj, File f) {
		ObjectOutputStream outputStream;
		try {
			OutputStream fileStream = new FileOutputStream(f);
			if (f.getName().endsWith(".gz")) {
				fileStream = new GZIPOutputStream(fileStream);
			}
			outputStream = new ObjectOutputStream(fileStream);
			outputStream.writeObject(obj);
			outputStream.close();
			setStatusMessage(Messages.getString("T_WRITING_COMPLETED")); //$NON-NLS-1$
			return true;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (SecurityException ex) {
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null, Messages.getString("T_PERMISSION_DENIED"), //$NON-NLS-1$
					Messages.getString("T_PERMISSION_DENIED"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
		return false;
	}

	public void saveWorldImage(File f) {
		final BufferedImage worldimage = new BufferedImage(_world._width, _world._height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = worldimage.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, _world.getWidth(), _world.getHeight());
		_world.draw(g, true);
		try {
			ImageIO.write(worldimage, "PNG", f); //$NON-NLS-1$
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void saveCladeImage(File f) {
		CladeStats cladeStats;
		synchronized (_world._organisms) {
			cladeStats = new CladeStats(_world._organisms);
		}
		Rectangle bounds = cladeStats.getBounds();
		final BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		cladeStats.draw(g);
		try {
			ImageIO.write(img, "PNG", f); //$NON-NLS-1$
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void saveStatisticsImage(File f) {
		final BufferedImage worldimage = new BufferedImage(_statisticsWindow.getWidth(), _statisticsWindow.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		_statisticsWindow.paint(worldimage.getGraphics());
		try {
			ImageIO.write(worldimage, "PNG", f); //$NON-NLS-1$
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void configureApp() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});
		setTitle(Messages.getString("T_BIOGENESIS")); //$NON-NLS-1$
		UIManager.put("OptionPane.yesButtonText", Messages.getString("T_YES"));
		UIManager.put("OptionPane.noButtonText", Messages.getString("T_NO"));
		UIManager.put("OptionPane.cancelButtonText", Messages.getString("T_CANCEL"));
	}

	public void updateStatusLabel() {
		statusLabelText.setLength(0);
		statusLabelText.append(Messages.getString("T_FPS")); //$NON-NLS-1$
		statusLabelText.append(nFrames - historicalFrames.get(0));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_TIME")); //$NON-NLS-1$
		statusLabelText.append(_world.getTime());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_CURRENT_POPULATION")); //$NON-NLS-1$
		statusLabelText.append(_world.getPopulation());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_CLADES2")); //$NON-NLS-1$
		statusLabelText.append(_world.getDistinctCladeIDCount());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_O2")); //$NON-NLS-1$
		statusLabelText.append(_nf.format(_world.getO2()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_CO2")); //$NON-NLS-1$
		statusLabelText.append(_nf.format(_world.getCO2()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_CH4")); //$NON-NLS-1$
		statusLabelText.append(_nf.format(_world.getCH4()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_DETRITUS2")); //$NON-NLS-1$
		statusLabelText.append(_nf.format(_world.getDetritus()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(getStatusMessage());
		_statusLabel.setText(statusLabelText.toString());
	}

	final transient Runnable lifeProcess = new Runnable() {
		public void run() {
			if (_isProcessActive) {
				// executa un torn
				_world.time();
				nFrames++;
				// dibuixa de nou si cal
				_world.setPaintingRegion();
				// tracking
				if (_trackedOrganism != null) {
					if (!_trackedOrganism.isAlive()) {
						_trackedOrganism = null;
						abortTrackingAction.setEnabled(false);
					} else {
						JScrollBar bar = scrollPane.getHorizontalScrollBar();
						bar.setValue(Utils.between(_trackedOrganism._centerX - scrollPane.getWidth() / 2,
								bar.getValue() - 2 * (int) Utils.MAX_VEL, bar.getValue() + 2 * (int) Utils.MAX_VEL));
						bar = scrollPane.getVerticalScrollBar();
						bar.setValue(Utils.between(_trackedOrganism._centerY - scrollPane.getHeight() / 2,
								bar.getValue() - 2 * (int) Utils.MAX_VEL, bar.getValue() + 2 * (int) Utils.MAX_VEL));
					}
				}
			}
		}
	};

	/**
	 * Scrolls the world so that the given organism is at the center of the view.
	 */
	public void scrollOrganismToCenter(Organism o) {
		if (o == null) {
			return;
		}

		scrollPane.getHorizontalScrollBar().setValue(o._centerX - scrollPane.getWidth() / 2);
		scrollPane.getVerticalScrollBar().setValue(o._centerY - scrollPane.getHeight() / 2);
	}

	/**
	 * Used for FPS calculation in the status bar.
	 *
	 * There are `Utils.STATUS_BAR_REFRESH_FPS` number of elements in this list.
	 * We appent the `nFrames` to this list and remove the first element every
	 * time we update the status bar. The `FPS:` in the status bar is calculated
	 * as `nFrames - historicalFrames.get(0)`.
	 * This allows us to have a less jumpy FPS counter in status bar since it
	 * shows the number of frames done in the last 1000 msec instead of just
	 * last 250 msec like before.
	 */
	protected List<Long> historicalFrames = new ArrayList<>();

	public void startApp() {
		/*
		 * GraphicsDevice gd =
		 * GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		 * if (gd.isFullScreenSupported()) {
		 * setResizable(false);
		 * setUndecorated(true);
		 * gd.setFullScreenWindow(this);
		 * validate();
		 * } else {
		 */
		setVisible(true);
		// }

		_timer = new java.util.Timer();
		startWorkerThread();
		for (int i = 0; i < Utils.STATUS_BAR_REFRESH_FPS; i++) {
			historicalFrames.add(0L);
		}
		new javax.swing.Timer(1000 / Utils.STATUS_BAR_REFRESH_FPS, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (_world._organisms) {
					updateStatusLabel();
					historicalFrames.remove(0);
					historicalFrames.add(nFrames);
				}
			}
		}).start();

		if (isAcceptingConnections())
			startServer();
	}

	public void startWorkerThread() {
		workerThread = new Thread() {
			@Override
			public void run() {
				long prevNanos = System.nanoTime();
				long accumulatedNanosForFpsAdjust = 0L;
				try {
					while (true) {
						EventQueue.invokeAndWait(lifeProcess);
						// Add a little delay if we were faster than the target fps.
						// But only if we need to repaint the world, so avoid unnecessary slowdowns when
						// not looking at the world.
						if (Utils.repaintWorld()) {
							final long currentNanos = System.nanoTime();
							final long frameNanos = currentNanos - prevNanos;
							prevNanos = currentNanos;
							accumulatedNanosForFpsAdjust += Utils.DELAY * 1_000_000L - frameNanos;
							if (accumulatedNanosForFpsAdjust > 100_000_000L) {
								// Clear the delay if it's abnormally big, to avoid sleeping for hours or days
								// due to a bug.
								accumulatedNanosForFpsAdjust = 0L;
							} else if (accumulatedNanosForFpsAdjust > 10_000_000L) {
								// If the accumulated delay is more than 10msec then we sleep.
								Thread.sleep(Math.round(accumulatedNanosForFpsAdjust * 0.000001));
							} else if (accumulatedNanosForFpsAdjust < -10_000_000L) {
								// Clean up negative delay in case we are slower than the target.
								accumulatedNanosForFpsAdjust = 0L;
							}
						}
						// Do automatic backups if we already saved the game. Ignore automatic backup
						// if the world has not been saved yet (i.e. after started a new world).
						if (Utils.AUTO_BACKUP && _world.getTime() % Utils.BACKUP_DELAY == 0 && _world.getFrame() == 1) {
							if (_world.getTime() > 0) {
								if ((_gameFile != null) && (!_world._isbackuped)) {
									backupGameAction.actionPerformed(null);
									_world._isbackuped = true;
								}
							} else {
								if (_gameFile == null) {
									backupGameAction.actionPerformed(null);
								}
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		};
		workerThread.start();
	}

	public void changeLocale() {
		UIManager.put("OptionPane.yesButtonText", Messages.getString("T_YES"));
		UIManager.put("OptionPane.noButtonText", Messages.getString("T_NO"));
		UIManager.put("OptionPane.cancelButtonText", Messages.getString("T_CANCEL"));
		_menuGame.setText(Messages.getString("T_GAME")); //$NON-NLS-1$
		_menuGame.setMnemonic(Messages.getMnemonic("T_GAME").intValue()); //$NON-NLS-1$
		newGameAction.changeLocale();
		startStopAction.changeLocale();
		openGameAction.changeLocale();
		saveGameAction.changeLocale();
		backupGameAction.changeLocale();
		saveGameAsAction.changeLocale();
		quitAction.changeLocale();
		_menuWorld.setText(Messages.getString("T_WORLD")); //$NON-NLS-1$
		_menuWorld.setMnemonic(Messages.getMnemonic("T_WORLD").intValue()); //$NON-NLS-1$
		statisticsAction.changeLocale();
		increaseCO2Action.changeLocale();
		decreaseCO2Action.changeLocale();
		increaseCH4Action.changeLocale();
		decreaseCH4Action.changeLocale();
		increaseDetritusAction.changeLocale();
		decreaseDetritusAction.changeLocale();
		parametersAction.changeLocale();
		labAction.changeLocale();
		killAllAction.changeLocale();
		disperseAllAction.changeLocale();
		_menuHelp.setText(Messages.getString("T_HELP")); //$NON-NLS-1$
		_menuHelp.setMnemonic(Messages.getMnemonic("T_HELP").intValue()); //$NON-NLS-1$
		manualAction.changeLocale();
		checkLastVersionAction.changeLocale();
		aboutAction.changeLocale();
		_menuNet.setText(Messages.getString("T_NETWORK")); //$NON-NLS-1$
		_menuNet.setMnemonic(Messages.getMnemonic("T_NETWORK").intValue()); //$NON-NLS-1$
		netConfigAction.changeLocale();
		manageConnectionsAction.changeLocale();
		setTitle(Messages.getString("T_BIOGENESIS")); //$NON-NLS-1$
		createMenu();
		_visibleWorld.changeLocale();
		infoToolbar.changeLocale();
		worldChooser = setUpdateUI(worldChooser);
	}

	public JFileChooser setUpdateUI(JFileChooser choose) {
		UIManager.put("FileChooser.openButtonText", Messages.getString("T_OPEN_TXT"));
		UIManager.put("FileChooser.cancelButtonText", Messages.getString("T_CANCEL"));
		UIManager.put("FileChooser.lookInLabelText", Messages.getString("T_LOOK_IN"));
		UIManager.put("FileChooser.fileNameLabelText", Messages.getString("T_FILE_NAME"));
		UIManager.put("FileChooser.filesOfTypeLabelText", Messages.getString("T_FILES_OF_TYPE"));

		UIManager.put("FileChooser.saveButtonText", Messages.getString("T_SAVE"));
		UIManager.put("FileChooser.saveButtonToolTipText", Messages.getString("T_SAVE"));
		UIManager.put("FileChooser.openButtonText", Messages.getString("T_OPEN_TXT"));
		UIManager.put("FileChooser.openButtonToolTipText", Messages.getString("T_OPEN_TXT"));
		UIManager.put("FileChooser.cancelButtonText", Messages.getString("T_CANCEL"));
		UIManager.put("FileChooser.cancelButtonToolTipText", Messages.getString("T_CANCEL"));

		UIManager.put("FileChooser.lookInLabelText", Messages.getString("T_LOOK_IN"));
		UIManager.put("FileChooser.saveInLabelText", Messages.getString("T_SAVE_IN"));
		UIManager.put("FileChooser.fileNameLabelText", Messages.getString("T_FILE_NAME"));
		UIManager.put("FileChooser.filesOfTypeLabelText", Messages.getString("T_FILES_OF_TYPE"));

		UIManager.put("FileChooser.upFolderToolTipText", Messages.getString("T_UP_FOLDER"));
		UIManager.put("FileChooser.homeFolderToolTipText", Messages.getString("T_HOME"));
		UIManager.put("FileChooser.newFolderToolTipText", Messages.getString("T_NEW_FOLDER"));
		UIManager.put("FileChooser.listViewButtonToolTipText", Messages.getString("T_LIST_VIEW"));
		UIManager.put("FileChooser.detailsViewButtonToolTipText", Messages.getString("T_DETAILS_VIEW"));
		UIManager.put("FileChooser.fileNameHeaderText", Messages.getString("T_NAME"));
		UIManager.put("FileChooser.fileSizeHeaderText", Messages.getString("T_FILE_SIZE"));
		UIManager.put("FileChooser.fileTypeHeaderText", Messages.getString("T_FILE_TYPE"));
		UIManager.put("FileChooser.fileDateHeaderText", Messages.getString("T_FILE_DATE"));
		UIManager.put("FileChooser.fileAttrHeaderText", Messages.getString("T_FILE_ATTR"));

		UIManager.put("FileChooser.acceptAllFileFilterText", Messages.getString("T_ALL_FILES"));

		UIManager.put("FileChooser.openDialogTitleText", Messages.getString("T_OPEN_TXT"));
		UIManager.put("FileChooser.saveDialogTitleText", Messages.getString("T_SAVE"));
		choose.updateUI();

		return choose;

	}

}
