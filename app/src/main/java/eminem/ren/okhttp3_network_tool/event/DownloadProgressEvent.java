package eminem.ren.okhttp3_network_tool.event;

/**
 * Created by EminemRen on 2017/9/8.
 */

public class DownloadProgressEvent {

    private long total;
    private int progress;
    private long currentSize;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public DownloadProgressEvent(long total, int progress, long currentSize) {
        this.total = total;
        this.progress = progress;
        this.currentSize = currentSize;
    }
}
