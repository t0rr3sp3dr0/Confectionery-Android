package me.t0rr3sp3dr0.confectionery.singletons;

import java.util.HashMap;

/**
 * Created by pedro on 2/4/17.
 */
public final class Mailbox extends HashMap<String, Object> {
    private static Mailbox ourInstance = new Mailbox();

    private Mailbox() {
    }

    public static Mailbox getInstance() {
        return ourInstance;
    }
}
