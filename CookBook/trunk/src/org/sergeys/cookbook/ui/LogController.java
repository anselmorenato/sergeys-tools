package org.sergeys.cookbook.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.sergeys.cookbook.logic.Settings;

public class LogController extends DialogController implements
		EventHandler<WindowEvent> {
	@FXML
	private TextArea logTextarea;

	private Future<?> future;
	private Runnable watcher = new Runnable() {

		@Override
		public void run() {

			// http://www.ericbruno.com/ericbruno/Programming_with_Reason/Entries/2010/4/30_Java_File_IO_Comparison.html
			// http://skillshared.blogspot.com/2012/11/how-to-read-dynamically-growing-file.html

			File logfile = new File(Settings.getSettingsDirPath()
					+ File.separator + "log0.txt");
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(logfile));
				boolean canContinue = true;
				while (canContinue) {
					final String str = br.readLine();
					if (str != null) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								logTextarea.appendText(str + "\n");
							}
						});
					}

					Thread.sleep(1000);
				}

			} catch (IOException e) {
				Settings.getLogger().error("", e);
			} catch (InterruptedException e) {
				try {
					br.close();
					Settings.getLogger().debug("reader closed");
				} catch (IOException e1) {
					Settings.getLogger().error("", e1);
				}
				Settings.getLogger().debug("log watch thread interrupted");
			}

			Settings.getLogger().debug("watcher run complete");
		}
	};

	public void initialize() {
		super.initialize();
	}

	@Override
	public void handle(WindowEvent evt) {
		if (evt.getEventType() == WindowEvent.WINDOW_SHOWN) {
			//future = Settings.getExecutor().submit(watcher);
			//Settings.getExecutor().execute(watcher);
		} else if (evt.getEventType() == WindowEvent.WINDOW_HIDING) {
//
//			try {
//				future.cancel(true);
//
//			} catch (Exception e) {
//				Settings.getLogger().error("", e);
//			}
//			future = null;
		}
	}

	@Override
	public void setStage(Stage stage) {
		super.setStage(stage);

		stage.setOnHiding(this);
		stage.setOnShown(this);
	}

	@Override
	protected void finalize() throws Throwable {

		Settings.getLogger().debug("log controller finalize");

		super.finalize();
	}
}
