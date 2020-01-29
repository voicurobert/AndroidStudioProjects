package com.rwee.mobilegatewaywo.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Robert on 04/02/16.
 */
public class CustomEditText extends EditText{
    private TextView label;

    public CustomEditText( Context context, AttributeSet attrs ){
        super( context, attrs );
    }

    public void setLabel( TextView label ){
        this.label = label;
    }

    public TextView getLabel( ){
        return label;
    }
}
