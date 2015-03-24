package com.example.guild.mychecklist;

/**
 * Created by Guild on 1/12/2015.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import com.example.guild.mychecklist.contextualvoicecommands.R;
import com.google.android.glass.view.WindowUtils;

public class ContextualMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Requests a voice menu on this activity. As for any other
        // window feature, be sure to request this before
        // setContentView() is called
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        setContentView(R.layout.activity_checklist);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        // Pass through to super to setup touch menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            switch (item.getItemId()) {
//                case R.id.dogs_menu_item:
//                    // handle top-level dogs menu item
//                    Log.d("Contextual", "dogs checks");
//                    break;
//                case R.id.cats_menu_item:
//                    // handle top-level cats menu item
//                    Log.d("Contextual", "cats checks");
//                    break;
//                case R.id.lab_menu_item:
//                    // handle second-level labrador menu item
//                    break;
//                case R.id.golden_menu_item:
//                    // handle second-level golden menu item
//                    Log.d("Contextual", "golden checks");
//                    break;
//                case R.id.calico_menu_item:
//                    // handle second-level calico menu item
//                    break;
//                case R.id.cheshire_menu_item:
//                    // handle second-level cheshire menu item
//                    break;
                case R.id.next_menu_item:
                    // handle second-level cheshire menu item
                    Log.d("Contextual", "go next checks");
                    break;
                default:
                    return true;
            }
            return true;
        }
        // Good practice to pass through to super if not handled
        return super.onMenuItemSelected(featureId, item);
    }
}