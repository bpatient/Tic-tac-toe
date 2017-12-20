package com.pbaraka.controllers;

import play.mvc.*;

import com.pbaraka.views.html.*;

/**
 * @author Patient Baraka
 * 
 * This the application main controller
 */
public class Application extends Controller {

	/**
	 * This function call/render the index view 
	 * and display the given text
	 * @return
	 */
    public static Result index() {
        return ok(index.render("The Tic-tac-toe Game API"));
    }   

}
