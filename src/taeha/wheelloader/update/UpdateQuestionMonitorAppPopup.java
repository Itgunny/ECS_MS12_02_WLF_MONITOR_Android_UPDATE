package taeha.wheelloader.update;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;


public class UpdateQuestionMonitorAppPopup extends Dialog{
	private static final String TAG = "UpdateQuestionPopup";

	// CAN1CommManager
	private static CAN1CommManager CAN1Comm;
	private static MainActivity ParentActivity;

	
	public UpdateQuestionMonitorAppPopup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public UpdateQuestionMonitorAppPopup(Context context, int theme) {
		super(context,theme);
		// TODO Auto-generated constructor stub
	}
		
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();	
		
	}

	public static class Builder{
		private Context context;
		
		private ImageButton imgbtnOK;
		private ImageButton imgbtnCancel;
		private TextView	textViewTitle;
	
		
		private DialogInterface.OnClickListener OKButtonClickListener;
		private DialogInterface.OnClickListener CancelButtonClickListener;
		private DialogInterface.OnDismissListener DismissListener;
			
		// Constructor
		public Builder(Context context){
			this.context = context;
			CAN1Comm = CAN1CommManager.getInstance();
			ParentActivity = (MainActivity)context;
		}
		
		// Button Function
		public Builder setOKButton(DialogInterface.OnClickListener listener){
			this.OKButtonClickListener = listener;
			return this;
		}
		
		// Button Function
		public Builder setCancelButton(DialogInterface.OnClickListener listener){
			this.CancelButtonClickListener = listener;
			return this;
		}
		
		// Dismiss
		public Builder setDismiss(DialogInterface.OnDismissListener listener){
			this.DismissListener = listener;
			return this;
		}
		
		// Create Dialog
		
		public UpdateQuestionMonitorAppPopup create(String strtitle){
			ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_STM32_QUESTION;
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			final UpdateQuestionMonitorAppPopup dialog = new UpdateQuestionMonitorAppPopup(context,R.style.Dialog);
			
			View layout = inflater.inflate(R.layout.popup_update_question_monitor_app, null);
			
			dialog.addContentView(layout, new LayoutParams(427,229));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			imgbtnOK = (ImageButton)layout.findViewById(R.id.imageButton_popup_update_question_monitor_app_ok);
			imgbtnCancel = (ImageButton)layout.findViewById(R.id.imageButton_popup_update_question_monitor_app_cancel);
			textViewTitle = (TextView)layout.findViewById(R.id.textView_popup_update_question_monitor_app_time);
			
			textViewTitle.setText(strtitle);
			if(OKButtonClickListener != null){
				
				imgbtnOK.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(TAG,"OKButtonClickListener");
						OKButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						
					}
				});
			}
			
			if(CancelButtonClickListener != null){
				
				imgbtnCancel.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(TAG,"CancelButtonClickListener");
						CancelButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						
					}
				});
			}
	    	if(DismissListener != null){
				DismissListener.onDismiss(dialog);
			}
			
			
			return dialog;
		}
		
	}
}