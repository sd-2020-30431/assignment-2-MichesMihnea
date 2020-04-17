package com.application;

import com.alee.laf.WebLookAndFeel;

import com.alee.skin.dark.WebDarkSkin;
import com.client.MainWindow;
import com.client.Controller;

public class WasteLessV2Application{
     
    public static void main(String[] args) {
    	WebLookAndFeel.install(WebDarkSkin.class);
    	MainWindow mw = new MainWindow();
        Controller controller = new Controller(mw);
    }

}
