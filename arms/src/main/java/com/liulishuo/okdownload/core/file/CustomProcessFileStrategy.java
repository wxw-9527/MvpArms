package com.liulishuo.okdownload.core.file;

import androidx.annotation.NonNull;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;

public class CustomProcessFileStrategy extends ProcessFileStrategy {

    @Override
    @NonNull
    public MultiPointOutputStream createProcessStream(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull DownloadStore store) {
        return new CustomMultiPointOutputStream(task, info, store);
    }
}
