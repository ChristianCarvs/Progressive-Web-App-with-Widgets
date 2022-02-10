package pryercarvs.koykoy200078.myapp.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import pryercarvs.koykoy200078.myapp.R;

public class WidgetActivity extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews aa = new RemoteViews(context.getPackageName(), R.layout.activity_app_widget);
        String fetchME = "https://api.coingecko.com/api/v3/simple/price?ids=smooth-love-potion%2Cronin&vs_currencies=php";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, fetchME, null,
                response ->
                {
                    try {
                        Log.w("WidgetExample", "Response => " + response.toString());
                        aa.setTextViewText(R.id.roninLabel, response.getJSONObject("ronin").getString("php") + " PHP");
                        aa.setTextViewText(R.id.slpLabel, response.getJSONObject("smooth-love-potion").getString("php") + " PHP");
                        Toast.makeText(context, "Latest Price Updated !", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    appWidgetManager.updateAppWidget(appWidgetId, aa);
                }, error -> {
        });
        queue.add(jsObjRequest);

        // update widget on btn update
        Intent intentUpdate = new Intent(context, WidgetActivity.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);
        aa.setOnClickPendingIntent(R.id.button_update, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, aa);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}