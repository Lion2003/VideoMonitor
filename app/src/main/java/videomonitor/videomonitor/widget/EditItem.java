package videomonitor.videomonitor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.utils.StringUtil;

/**
 * @Description: 特殊编辑框,依次显示:开始icon，红点，第一标题(上)第二标题(下)，输入框/显示value，结束icon(默认为dk
 *               箭头)
 * @author 严冠川
 * 
 */
public class EditItem extends RelativeLayout {
	// 输入或显示文字的位置
	public static final int POS_LEFT = Gravity.LEFT;
	public static final int POS_CENTER = Gravity.CENTER;
	public static final int POS_RIGHT = Gravity.RIGHT;

	private TextView tvTitle, tvValue;
	private View we_lineBottom;

	private int startIconId, endIconId, valueColorId, textColorId, titleColorId, necessaryIcon;
	private int lineMode, arrMode, necessaryMode;
	private float valueSize, titleSize, textSize;
	private String title, secondTitle, hint, text, value;
	private int textPos, valuePos;// 文字的位置,默认为左
	private int inputMode = 0;// 输入模式，默认为编辑模式

	public EditItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MysItem, 0, 0);

		try {
			title = a.getString(R.styleable.MysItem_title);
			hint = a.getString(R.styleable.MysItem_hint);
			secondTitle = a.getString(R.styleable.MysItem_second_title);
			startIconId = a.getResourceId(R.styleable.MysItem_icon, 0);
			endIconId = a.getResourceId(R.styleable.MysItem_end_icon, 0);
			lineMode = a.getInt(R.styleable.MysItem_linemode, 2);
			arrMode = a.getInt(R.styleable.MysItem_arrmode, 2);
			valueColorId = a.getResourceId(R.styleable.MysItem_valueColor, R.color.text_color);
			titleColorId = a.getResourceId(R.styleable.MysItem_titleColor, R.color.color_333333);
			textColorId = a.getResourceId(R.styleable.MysItem_textColor, R.color.darker_gray1);
			necessaryMode = a.getInt(R.styleable.MysItem_necessarymode, 2);
			textPos = a.getInt(R.styleable.MysItem_textPos, Gravity.LEFT);
			valuePos = a.getInt(R.styleable.MysItem_valuePos, Gravity.LEFT);
			text = a.getString(R.styleable.MysItem_text);
			value = a.getString(R.styleable.MysItem_value);
			inputMode = a.getInt(R.styleable.MysItem_inputMode, 1);
			valueSize = a.getFloat(R.styleable.MysItem_valueSize, 17);
			titleSize = a.getFloat(R.styleable.MysItem_titleSize, 17);
			textSize = a.getFloat(R.styleable.MysItem_textSize, 17);
		} finally {
			a.recycle();
		}

		View contentview = LayoutInflater.from(context).inflate(R.layout.widget_edititem, this, true);

		tvTitle = (TextView) contentview.findViewById(R.id.tvTitle);
		tvValue = (TextView) contentview.findViewById(R.id.tvValue);
		we_lineBottom = (View) contentview.findViewById(R.id.we_lineBottom);

		tvValue.setTextColor(context.getResources().getColor(valueColorId));
		tvTitle.setText(title);
		tvTitle.setTextColor(context.getResources().getColor(titleColorId));
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
		tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, valueSize);
//		setTextPos(textPos);
//		setValuePos(valuePos);

//		if (startIconId == 0) {
//			imvStartIcon.setVisibility(View.GONE);
//		} else {
//			imvStartIcon.setVisibility(View.VISIBLE);
//			imvStartIcon.setImageResource(startIconId);
//		}
		
		if(lineMode == 2) {
			we_lineBottom.setVisibility(View.VISIBLE);
		} else {
			we_lineBottom.setVisibility(View.GONE);
		}

		if (StringUtil.isEmpty(value)) {
			tvValue.setVisibility(View.GONE);
		} else {
			tvValue.setVisibility(View.VISIBLE);
			tvValue.setText(value);
		}

		if (StringUtil.isEmpty(title)) {
			tvTitle.setVisibility(View.GONE);
		} else {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(title);
		}
//
//		if (endIconId != 0) {
//			imvArr.setVisibility(View.VISIBLE);
//			imvArr.setImageResource(endIconId);
//		}
	}

	// *************************************************************************
	/**
	 * 【】(清除值)
	 */
	// *************************************************************************
	public void cleanValue() {
		tvValue.setVisibility(View.GONE);
	}

	public TextView getTvTextValue() {
		return tvValue;
	}


	public void setValueColor(int valueColorId) {
		tvValue.setTextColor(getResources().getColor(valueColorId));
	}
	
	// *************************************************************************
	/**
	 * 【】(设置值)
	 * 
	 * @param str
	 */
	// *************************************************************************
	public void setValue(String str) {
		if (str == null) {
			str = "";
		}
		tvValue.setVisibility(View.VISIBLE);
		tvValue.setText(str);
	}

	public void setValueGravity(int gravity) {
		tvValue.setGravity(gravity);
	}

	// *************************************************************************
	/**
	 * 【】(获取值)
	 * 
	 * @return
	 */
	// *************************************************************************
	public String getValue() {
		return tvValue.getText().toString();
	}

	// *************************************************************************
	/**
	 * 【】(设置标题的值)
	 * 
	 * @param title
	 */
	// *************************************************************************
	public void setTitle(String title) {
		if (title == null) {
			title = "";
		}
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(title);
	}

	// *************************************************************************
	/**
	 * 【】(增加颜色标题)
	 * 
	 * @param title
	 */
	// *************************************************************************
	public void addColorTitle(String title, int colorId) {
		if (title == null) {
			title = "";
		}
		tvTitle.setVisibility(View.VISIBLE);
	}

	/**
	 * 【】(设置开始的ICON)
	 * 
	 * @param imgStartIconId
	 */
//	public void setStartIcon(int imgStartIconId) {
//		imvStartIcon.setImageResource(imgStartIconId);
//	}

	/**
	 * 【】(设置开始的ICON是否可见)
	 * 
	 * @param imgStartIconId
	 */
//	public void setStartIconVisible(boolean visi) {
//		if (visi) {
//			imvStartIcon.setVisibility(View.VISIBLE);
//		} else {
//			imvStartIcon.setVisibility(View.GONE);
//		}
//	}

	// *************************************************************************
	/**
	 * 【】(获取标题的值)
	 * 
	 * @return
	 */
	// *************************************************************************
	public String getTitle() {
		return tvTitle.getText().toString();
	}

	// *************************************************************************
	/**
	 * 【】(设置显示密码)
	 */
	// *************************************************************************
	public void setInputTypePassword() {
		tvValue.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	// *************************************************************************

	/**
	 * @Title: setDescColor
	 * @Description: 设置右边数量文字颜色
	 * @param @param colorRes
	 * @return void
	 * @throws
	 */
	public void setDescColor(int colorRes) {
		tvValue.setTextColor(getContext().getResources().getColor(colorRes));
	}

	public void setDescResColor(int colorRes) {
		tvValue.setTextColor(colorRes);
	}

	/**
	 * @Title: setDescBackground
	 * @Description: 设置右边文字背景
	 * @param @param drawableRes
	 * @return void
	 * @throws
	 */
	public void setDescBackground(int drawableRes) {
		tvValue.setTextSize(11);
		tvValue.setBackgroundResource(drawableRes);
		if (drawableRes != 0) {
			tvValue.setVisibility(View.VISIBLE);
		} else {
			tvValue.setVisibility(View.GONE);
		}
	}

	public void setValueHint(String hint) {
		tvValue.setHint(hint);
		tvValue.setHintTextColor(getResources().getColor(R.color.red));
	}

	// 设置显示文字的位置
	public void setValuePos(int pos) {
		switch (pos) {
		case POS_LEFT:
			tvValue.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			break;
		case POS_CENTER:
			tvValue.setGravity(Gravity.CENTER);
			break;
		case POS_RIGHT:
			tvValue.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			break;
		default:
			break;
		}
	}

//	public ImageView getImvArr() {
//		return imvArr;
//	}
//
//	public void setImvArr(ImageView imvArr) {
//		this.imvArr = imvArr;
//	}

	// 设置点击结果图标点击事件
//	public void setEndIconClickLisener(OnClickListener listener) {
//		imvArr.setOnClickListener(listener);
//	}

}
