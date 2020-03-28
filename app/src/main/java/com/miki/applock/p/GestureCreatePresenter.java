package com.miki.applock.p;

import android.content.Context;
import android.os.AsyncTask;

import com.miki.applock.R;
import com.miki.applock.bean.LockStage;
import com.miki.applock.bean.LockSwitch;
import com.miki.applock.contract.GestureCreateContract;
import com.miki.applock.db.LockSwitchManager;
import com.miki.applock.utils.LockPatternUtils;
import com.miki.applock.view.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：cai_gp on 2020/3/12
 */
public class GestureCreatePresenter implements GestureCreateContract.Presenter {

    private GestureCreateContract.View mView;
    private Context mContext;
    private LockSwitchManager lockSwitchManager;
    private IsUpdateLockStatus isUpdateLockStatus;

    public GestureCreatePresenter(GestureCreateContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void updateStage(LockStage stage) {
        //更新UiStage
        mView.updateUiStage(stage);
        if (stage == LockStage.ChoiceTooShort) {
            //如果少于4个点
            mView.updateLockTip(mContext.getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE), true);
        } else {
            if (stage.headerMessage == R.string.lock_need_to_unlock_wrong) {
                mView.updateLockTip(mContext.getResources().getString(R.string.lock_need_to_unlock_wrong), true);
                mView.setHeaderMessage(R.string.lock_recording_intro_header);
            } else {
                mView.setHeaderMessage(stage.headerMessage);
            }
        }
        // same for whether the patten is enabled
        mView.lockPatternViewConfiguration(stage.patternEnabled, LockPatternView.DisplayMode.Correct);

        switch (stage) {
            case Introduction:
                mView.Introduction(); //第一步
                break;
            case HelpScreen: //帮助（错误多少次后可以启动帮助动画）
                mView.HelpScreen();
                break;
            case ChoiceTooShort: //锁屏路径太短
                mView.ChoiceTooShort();
                break;
            case FirstChoiceValid: //第一步提交成功
                updateStage(LockStage.NeedToConfirm); //转跳到第二步
                mView.moveToStatusTwo();
                break;
            case NeedToConfirm:
                mView.clearPattern();  //第二步
                break;
            case ConfirmWrong:
                //第二步跟第一步不一样
                mView.ConfirmWrong();
                break;
            case ChoiceConfirmed:
                //第三步
                mView.ChoiceConfirmed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern, List<LockPatternView.Cell> mChosenPattern, LockStage mUiStage) {
        if (mUiStage == LockStage.NeedToConfirm) {
            // 第二次绘制确认
            if (mChosenPattern == null) {
                throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
            }
            if (mChosenPattern.equals(pattern)) {
                updateStage(LockStage.ChoiceConfirmed);
                isUpdateLockStatus = new IsUpdateLockStatus();
                isUpdateLockStatus.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                updateStage(LockStage.ConfirmWrong);
            }
        } else if (mUiStage == LockStage.ConfirmWrong) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(LockStage.ChoiceTooShort);
            } else {
                if (mChosenPattern.equals(pattern)) {
                    updateStage(LockStage.ChoiceConfirmed);
                } else {
                    updateStage(LockStage.ConfirmWrong);
                }
            }
        } else if (mUiStage == LockStage.Introduction || mUiStage == LockStage.ChoiceTooShort) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(LockStage.ChoiceTooShort);
            } else {
                mChosenPattern = new ArrayList<>(pattern);
                mView.updateChosenPattern(mChosenPattern);
                updateStage(LockStage.FirstChoiceValid);
            }
        } else {
            throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
        }
    }

    @Override
    public void onDestroy() {

    }

    private class IsUpdateLockStatus extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            lockSwitchManager = new LockSwitchManager(mContext);
            List<LockSwitch> list = lockSwitchManager.queryAll();
            if(list.size() != 0) {
                return null;
            }
            // 第一次开启锁生成密码
            LockSwitch lockSwitch = new LockSwitch();
            lockSwitch.setLockstitch(true);
            lockSwitchManager.insert(lockSwitch);
            return null;
        }
    }
}
