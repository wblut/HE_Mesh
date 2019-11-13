package wblut.core;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import wblut.hemesh.HE_Element;

//TODO duplicate errors in HE_Mesh create transform step
public class WB_ProgressReporter extends Thread {
	WB_ProgressTracker	tracker;
	WB_ProgressStatus	status;
	PrintStream			output;
	String				path;

	public WB_ProgressReporter(final String path) {
		this(0, path, false);
	}

	public WB_ProgressReporter(final int depth, final int consoleDepth,
			final String path) {
		this(depth, path, false);
	}

	public WB_ProgressReporter(final int depth, final String path,
			final boolean append) {
		super();
		tracker = WB_ProgressTracker.instance();
		tracker.setMaxLevel(depth);
		this.path = path;
		try {
			output = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(path, append)), true, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		super.start();
		System.out.println("Starting WB_ProgressTracker: " + path);
		System.out.println("");
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				while (tracker.isUpdated()) {
					status = tracker.getStatus();
					if (status != null) {
						String s = status.getText();
						if (s != null) {
							output.println(s);
							if (status.getLevel() < 2) {
								System.out.println(status.getConsoleText());
							}
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class WB_ProgressCounter {
		protected volatile int		count;
		protected volatile String	caller;
		protected volatile String	text;
		private int					limit;
		private int					percentageStep;
		private volatile int		currentPercentage;
		private volatile int		nextUpdate;
		static WB_ProgressTracker	tracker	= WB_ProgressTracker.instance();

		public WB_ProgressCounter(final int limit, final int percentageStep) {
			this.count = 0;
			this.limit = limit;
			this.percentageStep = percentageStep;
			if (percentageStep < 0) {
				this.percentageStep = 10;
			}
			if (percentageStep > 100) {
				this.percentageStep = 10;
			}
			currentPercentage = 0;
			nextUpdate = (int) (limit * 0.01
					* (currentPercentage + percentageStep));
			caller = null;
			text = null;
		}

		public void increment() {
			increment(1);
		}

		public void increment(final int inc) {
			count += inc;
			if (count >= nextUpdate) {
				while (nextUpdate <= count) {
					currentPercentage += percentageStep;
					nextUpdate = (int) (limit * 0.01
							* (currentPercentage + percentageStep));
				}
				tracker.setCounterStatusStr(caller, text, this);
			} else if (count >= limit) {
				tracker.setCounterStatusStr(caller, text, this);
			}
		}

		protected int getCount() {
			return count;
		}

		protected int getLimit() {
			return limit;
		}
	}

	public static class WB_ProgressTracker {
		protected Queue<WB_ProgressStatus>	statuses;
		protected volatile int				level;
		static int							indent	= 3;
		protected volatile int				maxLevel;
		final int							INCLVL	= +1;
		final int							DECLVL	= -1;
		SimpleDateFormat					sdf		= new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");

		protected WB_ProgressTracker() {
			statuses = new ConcurrentLinkedQueue<WB_ProgressStatus>();
			level = 0;
			maxLevel = 2;
		}

		private static final WB_ProgressTracker tracker = new WB_ProgressTracker();

		public static WB_ProgressTracker instance() {
			return tracker;
		}

		public void setIndent(final int indent) {
			WB_ProgressTracker.indent = Math.max(0, indent);
		}

		protected WB_ProgressStatus getStatus() {
			if (statuses.size() > 0) {
				return statuses.poll();
			}
			return null;
		}

		public void setStartStatus(final Object caller, final String status) {
			if (level <= maxLevel) {
				String key = "";
				if (caller instanceof HE_Element) {
					key = " (key: " + ((HE_Element) caller).getKey() + ")";
				}
				statuses.add(new WB_ProgressStatus("\u250C",
						caller.getClass().getSimpleName() + key, status, level,
						sdf.format(new Date().getTime())));
			}
			level = Math.max(0, level + INCLVL);
		}

		public void setStopStatus(final Object caller, final String status) {
			level = Math.max(0, level + DECLVL);
			if (level <= maxLevel) {
				String key = "";
				if (caller instanceof HE_Element) {
					key = " (key: " + ((HE_Element) caller).getKey() + ")";
				}
				statuses.add(new WB_ProgressStatus("\u2514",
						caller.getClass().getSimpleName() + key, status, level,
						sdf.format(new Date().getTime())));
			}
		}

		public void setDuringStatus(final Object caller, final String status) {
			if (level <= maxLevel) {
				String key = "";
				if (caller instanceof HE_Element) {
					key = " (key: " + ((HE_Element) caller).getKey() + ")";
				}
				statuses.add(new WB_ProgressStatus("|",
						caller.getClass().getSimpleName() + key, status, level,
						sdf.format(new Date().getTime())));
			}
		}

		public void setStartStatusStr(final String caller,
				final String status) {
			if (level <= maxLevel) {
				statuses.add(new WB_ProgressStatus("\u250C", caller, status,
						level, sdf.format(new Date().getTime())));
			}
			level = Math.max(0, level + INCLVL);
		}

		public void setStopStatusStr(final String caller, final String status) {
			level = Math.max(0, level + DECLVL);
			if (level <= maxLevel) {
				statuses.add(new WB_ProgressStatus("\u2514", caller, status,
						level, sdf.format(new Date().getTime())));
			}
		}

		public void setDuringStatusStr(final String caller,
				final String status) {
			if (level <= maxLevel) {
				statuses.add(new WB_ProgressStatus("|", caller, status, level,
						sdf.format(new Date().getTime())));
			}
		}

		public void setCounterStatus(final Object caller, final String status,
				final WB_ProgressCounter counter) {
			if (counter.getLimit() > 0) {
				String key = "";
				if (caller instanceof HE_Element) {
					key = " (key: " + ((HE_Element) caller).getKey() + ")";
				}
				counter.caller = caller.getClass().getSimpleName() + key;
				counter.text = status;
				if (level <= maxLevel) {
					statuses.add(new WB_ProgressStatus("|",
							caller.getClass().getSimpleName() + key, status,
							counter, level, sdf.format(new Date().getTime())));
				}
			}
		}

		public void setCounterStatusStr(final String caller,
				final String status, final WB_ProgressCounter counter) {
			if (counter.getLimit() > 0) {
				if (level <= maxLevel) {
					statuses.add(new WB_ProgressStatus("|", caller, status,
							counter, level, sdf.format(new Date().getTime())));
				}
			}
		}

		public void setSpacer(final String caller) {
			if (level <= maxLevel) {
				statuses.add(new WB_ProgressStatus(caller, level,
						sdf.format(new Date().getTime())));
			}
		}

		public void setSpacer(final Object caller) {
			if (level <= maxLevel) {
				statuses.add(
						new WB_ProgressStatus(caller.getClass().getSimpleName(),
								level, sdf.format(new Date().getTime())));
			}
		}

		public boolean isUpdated() {
			return statuses.size() > 0;
		}

		public void setMaxLevel(final int maxLevel) {
			this.maxLevel = maxLevel;
		}
	}

	static class WB_ProgressStatus {
		String	caller;
		String	text;
		String	counterString;
		String	indent;
		String	time;
		String	delim;
		int		level;

		WB_ProgressStatus(final String delim, final String caller,
				final String text, final WB_ProgressCounter counter,
				final int depth, final String time) {
			this.caller = caller;
			this.text = text;
			StringBuffer outputBuffer = new StringBuffer(depth);
			for (int i = 0; i < depth; i++) {
				outputBuffer.append("|");
				for (int j = 0; j < WB_ProgressTracker.indent; j++) {
					outputBuffer.append(" ");
				}
			}
			this.indent = outputBuffer.toString();
			this.counterString = counter.getLimit() > 0 ? " ("
					+ counter.getCount() + " of " + counter.getLimit() + ")"
					: "";
			level = depth;
			this.time = new String(time);
			this.delim = delim;
		}

		WB_ProgressStatus(final String delim, final String caller,
				final String text, final int depth, final String time) {
			this.caller = caller;
			this.text = text;
			StringBuffer outputBuffer = new StringBuffer(depth);
			for (int i = 0; i < depth; i++) {
				outputBuffer.append("|");
				for (int j = 0; j < WB_ProgressTracker.indent; j++) {
					outputBuffer.append(" ");
				}
			}
			this.indent = outputBuffer.toString();
			this.counterString = null;
			this.time = new String(time);
			this.delim = delim;
			level = depth;
		}

		WB_ProgressStatus(final String caller, final int depth,
				final String time) {
			this.caller = "spacer";
			StringBuffer outputBuffer = new StringBuffer(caller.length());
			for (int i = 0; i < caller.length() + 1; i++) {
				outputBuffer.append("-");
			}
			this.text = outputBuffer.toString();
			outputBuffer = new StringBuffer(depth);
			for (int i = 0; i < time.length() + 1; i++) {
				outputBuffer.append(" ");
			}
			for (int i = 0; i < depth; i++) {
				outputBuffer.append("|");
				for (int j = 0; j < WB_ProgressTracker.indent; j++) {
					outputBuffer.append(" ");
				}
			}
			this.indent = outputBuffer.toString();
			this.counterString = null;
			this.time = new String(time);
			this.delim = "|";
			level = depth;
		}

		String getText() {
			if (caller == null) {
				return null;
			} else if (caller.equals("spacer")) {
				return indent + text;
			}
			if (text == " ") {
				return "";
			}
			if (counterString == null) {
				return time + " " + indent + delim + " " + caller + ": " + text;
			}
			return time + " " + indent + delim + " " + caller + ": " + text
					+ counterString;
		}

		String getConsoleText() {
			if (caller == null) {
				return null;
			} else if (caller.equals("spacer")) {
				return indent + text;
			}
			if (text == " ") {
				return "";
			}
			if (counterString == null) {
				return time + " " + indent + (delim.equals("|") ? "|" : "*")
						+ " " + caller + ": " + text;
			}
			return time + " " + indent + (delim.equals("|") ? "|" : "*") + " "
					+ caller + ": " + text + counterString;
		}

		int getLevel() {
			return level;
		}
	}
}
