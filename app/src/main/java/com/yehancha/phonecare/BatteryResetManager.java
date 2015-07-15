package com.yehancha.phonecare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by yehancha on 2015-07-15.
 */
public class BatteryResetManager {

    /**
     * Battery reset procedure starts by charging the battery to 100% capacity. So this is the
     * Target#1. After charging the battery to 100%, it has to be discharged completely without
     * any charging attempts in between. It cannot be guaranteed that a Battery Changed intent will
     * be received when the capacity hit 0% since the phone can switched off before sending the
     * intent. So 1% capacity is targeted as the Target#2. Phase#1 is the time between Target#1 and
     * Target#2. Then the phone is fully charged while it is switched off. This cannot be recognised
     * directly since the phone is switched off. But if the phone is fully charged while switched
     * off, the capacity must jump to 100% at once when the phone is turned on. To tolerate any
     * capacity lose (phone can be turned on many hours after fully charged), <95% is taken as the
     * Target#3. But after the Target#2, there cannot be any capacity data received since that means
     * the phone is turned on before fully charged. Time between Target#2 and Target#3 is the Phase#2.
     * If all the targets are hit without any obligations, it is recorded as a battery reset.
     * Otherwise the procedure has to be done all over from the beginning.
     */

    private final int CAPACITY_TARGET_ONE = 100;
    private final int CAPACITY_TARGET_TWO = 1;
    private final int CAPACITY_BOUNDARY_TARGET_THREE = 95;
    private final int PHASE_NO_POSSIBLE_RESET = 0;
    private final int PHASE_ONE = 1;
    private final int PHASE_TWO = 2;
    private final String PREFERENCE_RESET_PHASE = "com.yehancha.phonecare.PREFERENCE_RESET_PHASE";
    private final String PREFERENCE_DATE_LAST_RESET = "com.yehancha.phonecare.PREFERENCE_DATE_LAST_RESET";

    private Context context;

    public BatteryResetManager(Context context) {
        this.context = context;
    }

    public void onBatteryChange(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentPhase = sharedPreferences.getInt(PREFERENCE_RESET_PHASE, PHASE_NO_POSSIBLE_RESET);

        int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryCapacity = Math.round(batteryLevel / (float) batteryScale * 100);

        int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;

        int newPhase = currentPhase;
        boolean resetCompleted = false;
        if (currentPhase == PHASE_NO_POSSIBLE_RESET && batteryCapacity == CAPACITY_TARGET_ONE) {
            // Entering procedure
            newPhase = PHASE_ONE;
            Toast.makeText(context, "Entering Phase#1...", Toast.LENGTH_SHORT).show();
        } else if (currentPhase == PHASE_ONE) {
            if (batteryCharging) {
                // Charging intervened Phase#1
                newPhase = PHASE_NO_POSSIBLE_RESET;
                Toast.makeText(context, "Battery is charging in Phase#1. Procedure terminating...", Toast.LENGTH_SHORT).show();
            } else if (batteryCapacity == CAPACITY_TARGET_TWO) {
                // Entering Phase#2
                newPhase = PHASE_TWO;
                Toast.makeText(context, "Entering Phase#2...", Toast.LENGTH_SHORT).show();
            }
        } else if (currentPhase == PHASE_TWO) {
            if (batteryCapacity > CAPACITY_TARGET_TWO && batteryCapacity <= CAPACITY_BOUNDARY_TARGET_THREE) {
                // Phone is switched on while in the Phase#2. Phone should be turned on after reaching
                // at least the Target#3 boundary. Procedure is intervened.
                newPhase = PHASE_NO_POSSIBLE_RESET;
                Toast.makeText(context, "Phone is switched on in Phase#1. Procedure terminating...", Toast.LENGTH_SHORT).show();
            } else if (batteryCapacity > CAPACITY_BOUNDARY_TARGET_THREE) {
                // Procedure completed successfully
                newPhase = PHASE_NO_POSSIBLE_RESET;
                Toast.makeText(context, "Procedure completed!", Toast.LENGTH_SHORT).show();
            }
        }

        if (newPhase != currentPhase) {
            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
            preferenceEditor.putInt(PREFERENCE_RESET_PHASE, newPhase);

            if (resetCompleted) {
                preferenceEditor.putLong(PREFERENCE_DATE_LAST_RESET, new Date().getTime());
            }

            preferenceEditor.commit();
        }
    }
}
