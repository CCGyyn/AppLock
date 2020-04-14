package com.miki.settings.applock.utils;

import android.app.Activity;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

/**
 * @author：cai_gp on 2020/3/19
 */
public class FingerHelper extends FingerprintManagerCompat.AuthenticationCallback {

    private Activity activity;
    private FingerPrintCallBack callBack;
    private CancellationSignal cancellationSignal;
    private FingerprintManagerCompat fingerprintManagerCompat;
    /**
     * 是否开启过指纹功能
     */
    public boolean isStartFinger;

    private int count;

    public FingerHelper(Activity activity, FingerPrintCallBack callBack) {
        fingerprintManagerCompat = FingerprintManagerCompat.from(activity);
        if (!fingerprintManagerCompat.isHardwareDetected()
                || !fingerprintManagerCompat.hasEnrolledFingerprints()) {
            if(callBack != null) {
                callBack.fingerClosed();
            }
            return;
        }
        this.activity = activity;
        this.callBack = callBack;
        isStartFinger = false;
        cancellationSignal = new CancellationSignal();
    }

    public void hasFinger() {
        if (!fingerprintManagerCompat.isHardwareDetected()
                || !fingerprintManagerCompat.hasEnrolledFingerprints()) {
            if(callBack != null) {
                callBack.fingerClosed();
            }
        } else {
            if(callBack != null) {
                callBack.fingerOpen();
            }
        }
    }

    public void startFingerPrint() {
        isStartFinger = true;
        count = 5;
        fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, this, null);
    }

    public void stopFingerPrint() {
        if(isStartFinger) {
            if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
                cancellationSignal.cancel();
            }
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
        if (errMsgId == 5) {
            /*if (callBack != null) {
                callBack.fingerClosed();
            }*/
            return;
        }
        if (errMsgId == 7) {
            if (callBack != null) {
                callBack.onAuthenticationError();
            }
            return;
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        if(callBack != null) {
            callBack.onAuthenticationSucceeded();
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        count--;
        if(count > 0) {
            if(callBack != null) {
                callBack.onAuthenticationFailed(count);
            }
        }
    }

    public interface FingerPrintCallBack {
        /**
         * 识别成功
         */
        void onAuthenticationSucceeded();
        /**
         * 识别失败
         *
         * @param count 还可以尝试的次数
         * @param count
         */
        void onAuthenticationFailed(int count);
        /**
         * 失败次数过多
         */
        void onAuthenticationError();
        /**
         * 未开启指纹功能
         */
        void fingerClosed();
        /**
         * 不支持指纹功能
         */
        void fingerOpen();
    }
}
