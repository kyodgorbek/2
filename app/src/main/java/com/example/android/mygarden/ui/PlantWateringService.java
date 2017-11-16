package com.example.android.mygarden.ui;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.android.mygarden.utils.PlantUtils;
import com.example.android.mygarden.provider.PlantContract;

public class PlantWateringService extends IntentService {
    public static final String ACTION_WATER_PLANTS =
            "com.example.android.mygarden.action.water_plants";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PlantWateringService(String name) {
        super("PlantWateringService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param //to name the worker thread, important only for debugging.
     */
    public static void startActionWaterPlants(Context context) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_WATER_PLANTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WATER_PLANTS.equals(action)) {
                handleActionWaterPlants();
            }
        }
    }


    private void handleActionWaterPlants() {
                Uri PLANTS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();
                ContentValues contentValues = new ContentValues();
                long timeNow = System.currentTimeMillis();
                contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
               // Update only plants that are still alive
                        getContentResolver().update(
                                       PLANTS_URI,
                                       contentValues,
                                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME+">?",
                                       new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});
    }
}