package com.liulishuo.okdownload.core.file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;

import java.io.IOException;

public class CustomMultiPointOutputStream extends MultiPointOutputStream {

    private static final String TAG = "CustomMultiPointOutputStream";
    private final DownloadTask task;

    CustomMultiPointOutputStream(@NonNull final DownloadTask task, @NonNull BreakpointInfo info, @NonNull DownloadStore store, @Nullable Runnable syncRunnable) {
        super(task, info, store, syncRunnable);
        this.task = task;
    }

    public CustomMultiPointOutputStream(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull DownloadStore store) {
        this(task, info, store, null);
    }

    @Override
    synchronized void close(int blockIndex) throws IOException {
        final DownloadOutputStream outputStream = outputStreamMap.get(blockIndex);
        if (outputStream != null) {
            outputStream.close();
            synchronized (noSyncLengthMap) {
                // make sure the length of noSyncLengthMap is equal to outputStreamMap
                outputStreamMap.remove(blockIndex);
                noSyncLengthMap.remove(blockIndex);
            }
            Util.d(TAG, "OutputStream close task[" + task.getId() + "] block[" + blockIndex + "]");
        }
    }
}
