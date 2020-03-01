package ares.ywq.com.bezierlearning.entity;

public class ProgressItem {

    private int progress;

    private boolean isWaving = true;

    public boolean isWaving() {
        return isWaving;
    }

    public void setWaving(boolean waving) {
        isWaving = waving;
    }

    public ProgressItem(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
