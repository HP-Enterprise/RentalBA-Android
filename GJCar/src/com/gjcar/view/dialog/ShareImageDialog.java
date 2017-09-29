package com.gjcar.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gjcar.app.R;

/**
 * 主页分享活动弹框
 * <p>Created by wangyang on 2017/9/22.</p>
 */
public class ShareImageDialog extends Dialog {
    private ImageView img_all;
    private ImageView img_close;
    private Context context;
    public ShareImageDialog(Context context) {
        super(context, R.style.share_dialog);
        this.context = context;
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_image,null);
        img_all = (ImageView) view.findViewById(R.id.img_all);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        img_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.allClick();
                }
            }
        });

        this.setContentView(view);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.getWindow().setLayout((int) (wm.getDefaultDisplay().getWidth()*0.8),(int) (wm.getDefaultDisplay().getHeight()*0.8));
    }

    private ClickListener listener;


    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener{
        void allClick();
    }
}
