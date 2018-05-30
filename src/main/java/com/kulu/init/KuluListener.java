package com.kulu.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.epcs.Main;

public class KuluListener implements ServletContextListener {

	public static Main main;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		main = new Main();
		main.start();
	}

}
