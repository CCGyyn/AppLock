package com.miki.applock.contract;

import com.miki.applock.base.BasePresenter;
import com.miki.applock.base.BaseView;
import com.miki.applock.bean.LockStage;
import com.miki.applock.view.widget.LockPatternView;

import java.util.List;

public interface GestureCreateContract {

    interface View extends BaseView {
        void updateUiStage(LockStage stage); //更新UI状态

        void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern); //更新密码

        void updateLockTip(String text, boolean isToast); //更新解锁提示

        void setHeaderMessage(int headerMessage);

        void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode);  //控件的一些配置

        void Introduction(); //控件状态（刚开始）

        void HelpScreen(); //帮助（错误多少次后可以启动帮助动画）

        void ChoiceTooShort(); //锁屏路径太短

        void moveToStatusTwo(); //转到第二步

        void clearPattern(); //清空控件状态

        void ConfirmWrong(); //两次的路径不一样

        void ChoiceConfirmed(); //成功绘制了2次路径
    }

    interface Presenter extends BasePresenter {

        void updateStage(LockStage stage);

        void onPatternDetected(List<LockPatternView.Cell> pattern, List<LockPatternView.Cell> mChosenPattern, LockStage mUiStage);

        void onDestroy();
    }
}