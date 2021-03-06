package nl.erikduisters.letsbake.async;

/**
 * Created by Erik Duisters on 13-05-2017.
 */

public abstract class BackgroundJob implements Cancellable, Runnable {
    protected volatile boolean canceled;
    protected BackgroundJobHandler backgroundJobHandler;

    void setBackgroundJobHandler(BackgroundJobHandler handler) {
        this.backgroundJobHandler = handler;
    }

    @Override
    public void cancel() {
        canceled = true;
        backgroundJobHandler.cancelJob(this);
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    abstract void onError(Throwable error);

    void finish() { backgroundJobHandler.onFinished(this); }
}
