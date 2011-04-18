package cdu.computer.hxl.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import cdu.computer.hxl.factory.MenuFactory;
import cdu.computer.hxl.util.Constants;

/**
 * 家庭财务管理系统主显示窗体
 * 
 * @author hxl
 * @date 2011-03-06
 */
public class BaseJFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final StatusPanel status = new StatusPanel();

	private final Dimension screen = Toolkit.getDefaultToolkit()
			.getScreenSize();// 获得屏幕的大小
	private final static SystemTray sTray = SystemTray.getSystemTray();// 获得系统托盘

	private static final int DEFAULT_WIDTH = 300;// 窗体默认的宽度
	private static final int DEFAULT_HEIGHT = 210;// 窗体默认的高度

	private TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit()
			.getImage(this.getClass().getResource("/images/trayIcon.jpg")));// 系统托盘图标

	/*
	 * 创建一个系统托盘弹出菜单，并添加相应的事件
	 */
	private PopupMenu trayMenu = MenuFactory.createPopupMenu("", new String[] {
			"还原", "退出" }, new ActionListener[] { new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			sTray.remove(trayIcon);// 还原将系统托盘图标移除
			setVisible(true);
			setExtendedState(JFrame.NORMAL);
		}
	}, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}

	} });

	public BaseJFrame() {

		// this.getContentPane().setSize(new Dimension(width, height));
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLayout(new BorderLayout());// 设置布局管理器

		trayIcon.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sTray.remove(trayIcon);
				setVisible(true);
				setExtendedState(JFrame.NORMAL);
			}

		});
		trayIcon.setPopupMenu(trayMenu);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				try {
					sTray.add(trayIcon);// 添加系统托盘
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void windowIconified(WindowEvent e) {
				try {
					setVisible(false);
					sTray.add(trayIcon);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}

		});

	}

	/**
	 * 设置财务管理系统框体的标题
	 */
	public BaseJFrame setFrameTitle(String title) {

		this.setTitle(title);
		return this;
	}

	/**
	 * 设置财务管理系统的宽度和高度
	 * 
	 * @param width
	 * @param height
	 * @return this
	 */
	@SuppressWarnings("static-access")
	public BaseJFrame setFrameSize(double width, double height) {

		/*
		 * 如果传入的宽度或者高度小于等于了0，那么就把窗口设置为默认高度、宽度
		 */
		if (width <= 0.0 || height <= 0.0) {
			width = this.DEFAULT_WIDTH;
			height = this.DEFAULT_HEIGHT;
		}

		Dimension dimension = new Dimension();
		dimension.setSize(width, height);
		this.setSize(dimension);
		return this;
	}

	/**
	 * 设置窗口图标显示的图象
	 * 
	 * @param image
	 * @return this
	 */
	public BaseJFrame setFrameIconImage(Image image) {
		this.setIconImage(image);
		return this;
	}

	/**
	 * 设置此窗体是否可由用户调整大小
	 * 
	 * @return this
	 */
	public BaseJFrame setFrameResizable(boolean b) {
		this.setResizable(b);
		return this;
	}

	/**
	 * 设置窗体的位置居中
	 * 
	 * @return this object
	 */
	public BaseJFrame setFrameCenter() {
		double cashManagerWidth = this.getSize().getWidth();
		double cashManagerHeight = this.getSize().getHeight();
		double screenWidth = screen.getWidth();
		double screenHeight = screen.getHeight();

		this.setLocation(((int) (screenWidth - cashManagerWidth) / 2),
				(int) ((screenHeight - cashManagerHeight) / 2));

		return this;
	}
	

	/**
	 * 显示底部状态栏
	 */
	public void showStatus() {
		this.add(status, BorderLayout.SOUTH);
	}

	/**
	 * 显示窗口
	 */
	public void showFrame() {
		setVisible(true);
	}

	/**
	 * 改变状态栏左边的提示信息
	 * 
	 * @param text
	 */
	public void setStatusText(String text) {
		status.chanageStatusText(text);
	}

	/**
	 * 底部状态栏
	 * 
	 * @author hxl
	 * 
	 */
	private class StatusPanel extends BaseJPanel {

		private static final long serialVersionUID = -7563273600503012769L;
		private JLabel status = null;

		@Override
		protected void init() {
			setLayout(new BorderLayout());
			@SuppressWarnings("deprecation")
			JLabel time = new JLabel(new Date().toLocaleString());
			time.setFont(new Font("宋体", Font.ITALIC, 10));
			add(time, BorderLayout.EAST);

			chanageStatusText("准备就绪");

		}

		public void chanageStatusText(String text) {
			status = new JLabel();
			status.setText(text);
			status.setFont(new Font("宋体", Font.ITALIC, 10));
			if (this.getComponentCount() >= 2)
				remove(1);
			add(status, BorderLayout.WEST);
			validate();
		}

	}
	// public static void main(String[] args) throws IOException {
	// BaseJFrame frame = new BaseJFrame();
	// frame.setVisible(true);
	// }
}
