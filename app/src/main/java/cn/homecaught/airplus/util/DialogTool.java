package cn.homecaught.airplus.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.View;

/** 
 * @author  作者:yangshouzhi E-mail:1554668839@qq.com
 * @date 创建时间：2015-7-8 下午3:15:16 
 * @version 1.0 
 * @parameter 
 * @since 
 * @return  
 */
public class DialogTool {

	public static final int NO_ICON = -1; // 无图标

	public static void dismissDialog(ProgressDialog pd) {
		try {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 创建消息对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createMessageDialog(Context context, String title,
			String message, String btnName, OnClickListener listener, int iconId) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置对话框消息
		builder.setMessage(message);
		// 设置按钮
		builder.setPositiveButton(btnName, listener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建警示（确认、取消）对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createConfirmDialog(Context context, String title,
			String message, String positiveBtnName, String negativeBtnName,
			OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener, int iconId) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置对话框消息
		builder.setMessage(message);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置取消按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建单选对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param itemsString
	 *            选择项 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param itemClickListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createSingleChoiceDialog(Context context,
			String title, String[] itemsString, String positiveBtnName,
			String negativeBtnName, OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener,
			OnClickListener itemClickListener, int iconId) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置单选选项, 参数0: 默认第一个单选按钮被选中
		builder.setSingleChoiceItems(itemsString, 0, itemClickListener);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建复选对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param itemsString
	 *            选择项 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param itemClickListener
	 *            监听器，需实现android.content.DialogInterface.
	 *            OnMultiChoiceClickListener;接口 必填
	 * @return
	 */
	public static Dialog createMultiChoiceDialog(Context context, String title,
			String[] itemsString, String positiveBtnName,
			String negativeBtnName, OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener,
			OnMultiChoiceClickListener itemClickListener, int iconId) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置选项
		builder.setMultiChoiceItems(itemsString, null, itemClickListener);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 创建列表对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param itemsString
	 *            列表项 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createListDialog(Context context, String title,
			String[] itemsString, String negativeBtnName,
			OnClickListener negativeBtnListener,
			OnClickListener itemClickListener, int iconId) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		// 设置列表选项
		builder.setItems(itemsString, itemClickListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

	/**
	 * 
	 * 创建进度对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param message
	 *            显示内容 必填
	 * @param cancelable
	 *            是否可以手动取消
	 * @return
	 */
	public static ProgressDialog createProgressDialog(Context context,
			String message, boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(message);
		dialog.setCancelable(cancelable);
		return dialog;
	}

	/**
	 * 创建进度对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param msgId
	 *            显示内容资源id 必填
	 * @param cancelable
	 *            是否可以手动取消
	 * @return
	 */
	public static ProgressDialog createProgressDialog(Context context,
			int msgId, boolean cancelable) {
		String message = context.getString(msgId);
		return createProgressDialog(context, message, cancelable);
	}

	/**
	 * 创建自定义（含确认、取消）对话框
	 * 
	 * @param context
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 或 DialogTool.NO_ICON 必填
	 * @param title
	 *            标题 必填
	 * @param positiveBtnName
	 *            确定按钮名称 必填
	 * @param negativeBtnName
	 *            取消按钮名称 必填
	 * @param positiveBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param negativeBtnListener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param view
	 *            对话框中自定义视图 必填
	 * @return
	 */
	public static Dialog createRandomDialog(Context context, String title,
			String positiveBtnName, String negativeBtnName,
			OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener, View view, int iconId) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (iconId != NO_ICON) {
			// 设置对话框图标
			builder.setIcon(iconId);
		}
		// 设置对话框标题
		builder.setTitle(title);
		builder.setView(view);
		// 设置确定按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		// 设置确定按钮
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		// 创建一个消息对话框
		dialog = builder.create();

		return dialog;
	}

}
