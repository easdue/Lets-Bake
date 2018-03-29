package nl.erikduisters.letsbake.async;

/**
 * Created by Erik Duisters on 13-05-2017.
 */

public interface Cancellable {
    boolean isCancelled();
    void cancel();
}
