package cc.ibooker.ibookereditorlib;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 书客编辑器 - 编辑界面
 * Created by 邹峰立 on 2018/2/11.
 */
public class IbookerEditorEditView extends LinearLayout {
    private EditText ibookerEd;

    public EditText getIbookerEd() {
        return ibookerEd;
    }

    public void setIbookerEd(EditText ibookerEd) {
        this.ibookerEd = ibookerEd;
    }

    // 三种构造方法
    public IbookerEditorEditView(Context context) {
        this(context, null);
    }

    public IbookerEditorEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IbookerEditorEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        init(context);
    }

    // 初始化
    private void init(Context context) {
        ibookerEd = new EditText(context);
        ibookerEd.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        ibookerEd.setGravity(Gravity.TOP | Gravity.START);
        ibookerEd.setInputType(InputType.TYPE_CLASS_TEXT);
        ibookerEd.setPadding(IbookerEditorUtil.dpToPx(context, 8), IbookerEditorUtil.dpToPx(context, 8), IbookerEditorUtil.dpToPx(context, 8), IbookerEditorUtil.dpToPx(context, 8));
        ibookerEd.setBackgroundResource(android.R.color.transparent);
        ibookerEd.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                try {
                    Field mEditor = TextView.class.getDeclaredField("mEditor");//找到 TextView中的成员变量mEditor
                    mEditor.setAccessible(true);
                    Object object = mEditor.get(ibookerEd);//根具持有对象拿到mEditor变量里的值 （android.widget.Editor类的实例）
                    //--------------------显示选择控制工具------------------------------//
                    Class mClass = Class.forName("android.widget.Editor");//拿到隐藏类Editor；
                    Method method = mClass.getDeclaredMethod("getSelectionController");//取得方法  getSelectionController
                    method.setAccessible(true);//取消访问私有方法的合法性检查
                    Object resultobject = method.invoke(object);//调用方法，返回SelectionModifierCursorController类的实例

                    Method show = resultobject.getClass().getDeclaredMethod("show");//查找 SelectionModifierCursorController类中的show方法
                    show.invoke(resultobject);//执行SelectionModifierCursorController类的实例的show方法
                    ibookerEd.setHasTransientState(true);

                    //--------------------忽略最后一次TouchUP事件-----------------------------------------------//
                    Field mSelectionActionMode = mClass.getDeclaredField("mDiscardNextActionUp");//查找变量Editor类中mDiscardNextActionUp
                    mSelectionActionMode.setAccessible(true);
                    mSelectionActionMode.set(object, true);//赋值为true
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;// 返回false 就是屏蔽ActionMode菜单
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        addView(ibookerEd);

        ((Activity) ibookerEd.getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    // 设置输入框
    public IbookerEditorEditView setIbookerEdTextSize(float size) {
        ibookerEd.setTextSize(size);
        return this;
    }

    public IbookerEditorEditView setIbookerEdTextColor(@ColorInt int color) {
        ibookerEd.setTextColor(color);
        return this;
    }

    public IbookerEditorEditView setIbookerEdHint(CharSequence hint) {
        ibookerEd.setHint(hint);
        return this;
    }

    public IbookerEditorEditView setIbookerEdHintTextColor(@ColorInt int color) {
        ibookerEd.setHintTextColor(color);
        return this;
    }

    public IbookerEditorEditView setIbookerEdBackgroundColor(@ColorInt int color) {
        ibookerEd.setBackgroundColor(color);
        return this;
    }

}
