package hu.tvarga.capstone.cheaplist.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import hu.tvarga.capstone.cheaplist.R;
import hu.tvarga.capstone.cheaplist.ui.compare.CompareActivity;

public class WidgetProvider extends AppWidgetProvider {

	public static final String ACTION_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		String action = intent.getAction();
		if (ACTION_UPDATE.equals(action)) {
			int[] appWidgetIDs = appWidgetManager.getAppWidgetIds(
					new ComponentName(context, getClass()));
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.widgetShoppingList);
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent intent = new Intent(context, WidgetService.class);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
		rv.setRemoteAdapter(R.id.widgetShoppingList, intent);
		rv.setEmptyView(R.id.widgetShoppingList, R.id.widgetDefaultMessage);

		Intent openAppIntent = new Intent(context, CompareActivity.class);
		PendingIntent openAppPendingIntent = PendingIntent.getActivity(context, 0, openAppIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.widgetTitle, openAppPendingIntent);
		rv.setOnClickPendingIntent(R.id.widgetDefaultMessage, openAppPendingIntent);
		rv.setOnClickPendingIntent(R.id.widgetLayoutParent, openAppPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds, rv);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
