package com.gjcar.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjcar.app.R;
import com.gjcar.data.data.Public_Api;
import com.gjcar.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 主页分享活动弹框
 * <p>Created by wangyang on 2017/9/22.</p>
 */
public class ShareImageDialog extends Dialog {
    private ImageView img_all;
    private TextView img_close;
    private TextView img_b;
    private Context context;
    public ShareImageDialog(Context context) {
        super(context, R.style.share_dialog);
        this.context = context;
//        initView();
    }

    public void initView(Bitmap bitmap){
    	
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_image,null);
        img_all = (ImageView) view.findViewById(R.id.img_all);
        
        img_all.setImageBitmap(bitmap);

        img_close = (TextView) view.findViewById(R.id.img_a);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        img_b= (TextView) view.findViewById(R.id.img_b);
        img_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        
//        img_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null){
//                    listener.allClick();
//                }
//            }
//        });

        this.setContentView(view);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = (int) (wm.getDefaultDisplay().getWidth()*0.8);
        int height = (int)(((float)width * 510f)/560f);
        this.getWindow().setLayout((int) (wm.getDefaultDisplay().getWidth()*0.8),height);
    }
    
    private ClickListener listener;


    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener{
        void allClick();
    }
}
