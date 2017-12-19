package com.pbaraka.controllers;

import play.mvc.*;

import com.pbaraka.views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("The Tic-tac-toe Game API"));
    }   

}
