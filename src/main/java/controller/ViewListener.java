package controller;

import model.Commands;

import java.util.EventListener;

@FunctionalInterface
public interface ViewListener extends EventListener {

    public void eventPerformed(Commands code);

}
